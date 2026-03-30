package com.mangareader.service;

import com.mangareader.model.Manga;
import com.mangareader.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MangaService {

    private final MangaRepository mangaRepository;
    private final FileStorageService fileStorageService;

    public List<Manga> getAllMangas() {
        return mangaRepository.findAllOrderByUpdatedAtDesc();
    }

    public Optional<Manga> getMangaById(Long id) {
        return mangaRepository.findById(id);
    }

    public List<Manga> searchMangas(String title) {
        return mangaRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Manga> getMangasByGenre(String genre) {
        return mangaRepository.findByGenre(genre);
    }

    @Transactional
    public Manga createManga(Manga manga, MultipartFile coverImage) throws IOException {
        if (coverImage != null && !coverImage.isEmpty()) {
            String coverPath = fileStorageService.saveCoverImage(coverImage, manga.getTitle());
            manga.setCoverImagePath(coverPath);
        }
        return mangaRepository.save(manga);
    }

    @Transactional
    public Manga updateManga(Long id, Manga updatedManga, MultipartFile coverImage) throws IOException {
        Manga existing = mangaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Manga not found: " + id));

        existing.setTitle(updatedManga.getTitle());
        existing.setDescription(updatedManga.getDescription());
        existing.setAuthor(updatedManga.getAuthor());
        existing.setArtist(updatedManga.getArtist());
        existing.setStatus(updatedManga.getStatus());
        existing.setGenres(updatedManga.getGenres());

        if (coverImage != null && !coverImage.isEmpty()) {
            String coverPath = fileStorageService.saveCoverImage(coverImage, existing.getTitle());
            existing.setCoverImagePath(coverPath);
        }

        return mangaRepository.save(existing);
    }

    @Transactional
    public void deleteManga(Long id) {
        mangaRepository.deleteById(id);
    }
}
