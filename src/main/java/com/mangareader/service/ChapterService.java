package com.mangareader.service;

import com.mangareader.model.Chapter;
import com.mangareader.model.Manga;
import com.mangareader.repository.ChapterRepository;
import com.mangareader.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final MangaRepository mangaRepository;
    private final FileStorageService fileStorageService;

    public List<Chapter> getChaptersByManga(Long mangaId) {
        return chapterRepository.findByMangaIdOrderByChapterNumberAsc(mangaId);
    }

    public Optional<Chapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }

    @Transactional
    public Chapter uploadChapter(Long mangaId, Double chapterNumber, String title,
                                  List<MultipartFile> pages) throws IOException {
        Manga manga = mangaRepository.findById(mangaId)
            .orElseThrow(() -> new RuntimeException("Manga not found: " + mangaId));

        List<String> pagePaths = fileStorageService.saveChapterPages(pages, mangaId, chapterNumber);

        Chapter chapter = new Chapter();
        chapter.setManga(manga);
        chapter.setChapterNumber(chapterNumber);
        chapter.setTitle(title);
        chapter.setPages(pagePaths);
        chapter.setPageCount(pagePaths.size());

        return chapterRepository.save(chapter);
    }

    @Transactional
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }

    public byte[] downloadChapterZip(Long chapterId) throws IOException {
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> new RuntimeException("Chapter not found: " + chapterId));
        return fileStorageService.createChapterZip(
            chapter.getManga().getId(),
            chapter.getChapterNumber(),
            chapter.getPages()
        );
    }

    public byte[] downloadMangaZip(Long mangaId) throws IOException {
        Manga manga = mangaRepository.findById(mangaId)
            .orElseThrow(() -> new RuntimeException("Manga not found: " + mangaId));
        List<Chapter> chapters = chapterRepository.findByMangaIdOrderByChapterNumberAsc(mangaId);
        List<String> allPages = chapters.stream()
            .flatMap(c -> c.getPages().stream())
            .toList();
        return fileStorageService.createMangaZip(mangaId, manga.getTitle(), allPages);
    }
}
