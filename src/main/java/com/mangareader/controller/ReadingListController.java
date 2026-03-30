package com.mangareader.controller;

import com.mangareader.model.ReadingList;
import com.mangareader.service.ReadingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReadingListController {

    private final ReadingListService readingListService;

    @GetMapping
    public ResponseEntity<List<ReadingList>> getAllLists() {
        return ResponseEntity.ok(readingListService.getAllLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingList> getList(@PathVariable Long id) {
        return readingListService.getListById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReadingList> createList(@RequestBody ReadingList list) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readingListService.createList(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadingList> updateList(@PathVariable Long id, @RequestBody ReadingList list) {
        return ResponseEntity.ok(readingListService.updateList(id, list));
    }

    @PostMapping("/{listId}/mangas/{mangaId}")
    public ResponseEntity<ReadingList> addManga(@PathVariable Long listId, @PathVariable Long mangaId) {
        return ResponseEntity.ok(readingListService.addMangaToList(listId, mangaId));
    }

    @DeleteMapping("/{listId}/mangas/{mangaId}")
    public ResponseEntity<ReadingList> removeManga(@PathVariable Long listId, @PathVariable Long mangaId) {
        return ResponseEntity.ok(readingListService.removeMangaFromList(listId, mangaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        readingListService.deleteList(id);
        return ResponseEntity.noContent().build();
    }
}
