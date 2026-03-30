package com.mangareader.controller;

import com.mangareader.model.Chapter;
import com.mangareader.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/mangas/{mangaId}/chapters")
    public ResponseEntity<List<Chapter>> getChapters(@PathVariable Long mangaId) {
        return ResponseEntity.ok(chapterService.getChaptersByManga(mangaId));
    }

    @GetMapping("/chapters/{id}")
    public ResponseEntity<Chapter> getChapter(@PathVariable Long id) {
        return chapterService.getChapterById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/mangas/{mangaId}/chapters", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Chapter> uploadChapter(
            @PathVariable Long mangaId,
            @RequestParam Double chapterNumber,
            @RequestParam(required = false) String title,
            @RequestPart("pages") List<MultipartFile> pages) throws IOException {
        Chapter chapter = chapterService.uploadChapter(mangaId, chapterNumber, title, pages);
        return ResponseEntity.status(HttpStatus.CREATED).body(chapter);
    }

    @DeleteMapping("/chapters/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chapters/{id}/download")
    public ResponseEntity<byte[]> downloadChapter(@PathVariable Long id) throws IOException {
        byte[] zip = chapterService.downloadChapterZip(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "chapter_" + id + ".zip");
        return ResponseEntity.ok().headers(headers).body(zip);
    }

    @GetMapping("/mangas/{mangaId}/download")
    public ResponseEntity<byte[]> downloadManga(@PathVariable Long mangaId) throws IOException {
        byte[] zip = chapterService.downloadMangaZip(mangaId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "manga_" + mangaId + ".zip");
        return ResponseEntity.ok().headers(headers).body(zip);
    }
}
