package com.mangareader.controller;

import com.mangareader.model.Manga;
import com.mangareader.service.MangaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/mangas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MangaController {

    private final MangaService mangaService;

    @GetMapping
    public ResponseEntity<List<Manga>> getAllMangas(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(mangaService.searchMangas(search));
        }
        if (genre != null && !genre.isBlank()) {
            return ResponseEntity.ok(mangaService.getMangasByGenre(genre));
        }
        return ResponseEntity.ok(mangaService.getAllMangas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manga> getManga(@PathVariable Long id) {
        return mangaService.getMangaById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Manga> createManga(
            @RequestPart("manga") Manga manga,
            @RequestPart(value = "cover", required = false) MultipartFile cover) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mangaService.createManga(manga, cover));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Manga> updateManga(
            @PathVariable Long id,
            @RequestPart("manga") Manga manga,
            @RequestPart(value = "cover", required = false) MultipartFile cover) throws IOException {
        return ResponseEntity.ok(mangaService.updateManga(id, manga, cover));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManga(@PathVariable Long id) {
        mangaService.deleteManga(id);
        return ResponseEntity.noContent().build();
    }
}
