package com.mangareader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public String saveCoverImage(MultipartFile file, String mangaTitle) throws IOException {
        String sanitized = sanitize(mangaTitle);
        Path dir = Paths.get(uploadDir, "covers");
        Files.createDirectories(dir);
        String ext = getExtension(file.getOriginalFilename());
        String filename = sanitized + "_cover_" + System.currentTimeMillis() + ext;
        Path dest = dir.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return "covers/" + filename;
    }

    public List<String> saveChapterPages(List<MultipartFile> files, Long mangaId, Double chapterNumber) throws IOException {
        Path dir = Paths.get(uploadDir, "mangas", String.valueOf(mangaId), "chapter_" + chapterNumber);
        Files.createDirectories(dir);
        List<String> paths = new ArrayList<>();
        // Sort files by name to maintain page order
        List<MultipartFile> sorted = files.stream()
            .sorted(Comparator.comparing(f -> Objects.requireNonNull(f.getOriginalFilename())))
            .toList();
        int i = 1;
        for (MultipartFile file : sorted) {
            String ext = getExtension(file.getOriginalFilename());
            String filename = String.format("page_%03d%s", i++, ext);
            Path dest = dir.resolve(filename);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            paths.add("mangas/" + mangaId + "/chapter_" + chapterNumber + "/" + filename);
        }
        return paths;
    }

    public byte[] createMangaZip(Long mangaId, String mangaTitle, List<String> allPages) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String pagePath : allPages) {
                Path fullPath = Paths.get(uploadDir, pagePath);
                if (Files.exists(fullPath)) {
                    ZipEntry entry = new ZipEntry(pagePath);
                    zos.putNextEntry(entry);
                    Files.copy(fullPath, zos);
                    zos.closeEntry();
                }
            }
        }
        return baos.toByteArray();
    }

    public byte[] createChapterZip(Long mangaId, Double chapterNumber, List<String> pages) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            int i = 1;
            for (String pagePath : pages) {
                Path fullPath = Paths.get(uploadDir, pagePath);
                if (Files.exists(fullPath)) {
                    String ext = getExtension(pagePath);
                    ZipEntry entry = new ZipEntry(String.format("chapter_%s/page_%03d%s", chapterNumber, i++, ext));
                    zos.putNextEntry(entry);
                    Files.copy(fullPath, zos);
                    zos.closeEntry();
                }
            }
        }
        return baos.toByteArray();
    }

    public Path getFilePath(String relativePath) {
        return Paths.get(uploadDir, relativePath);
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_").toLowerCase();
    }

    private String getExtension(String filename) {
        if (filename == null) return ".jpg";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".jpg";
    }
}
