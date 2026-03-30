package com.mangareader.service;

import com.mangareader.model.Manga;
import com.mangareader.model.ReadingList;
import com.mangareader.repository.MangaRepository;
import com.mangareader.repository.ReadingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadingListService {

    private final ReadingListRepository readingListRepository;
    private final MangaRepository mangaRepository;

    public List<ReadingList> getAllLists() {
        return readingListRepository.findAll();
    }

    public Optional<ReadingList> getListById(Long id) {
        return readingListRepository.findById(id);
    }

    @Transactional
    public ReadingList createList(ReadingList list) {
        return readingListRepository.save(list);
    }

    @Transactional
    public ReadingList updateList(Long id, ReadingList updated) {
        ReadingList existing = readingListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("List not found: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setColor(updated.getColor());
        return readingListRepository.save(existing);
    }

    @Transactional
    public ReadingList addMangaToList(Long listId, Long mangaId) {
        ReadingList list = readingListRepository.findById(listId)
            .orElseThrow(() -> new RuntimeException("List not found: " + listId));
        Manga manga = mangaRepository.findById(mangaId)
            .orElseThrow(() -> new RuntimeException("Manga not found: " + mangaId));
        if (!list.getMangas().contains(manga)) {
            list.getMangas().add(manga);
        }
        return readingListRepository.save(list);
    }

    @Transactional
    public ReadingList removeMangaFromList(Long listId, Long mangaId) {
        ReadingList list = readingListRepository.findById(listId)
            .orElseThrow(() -> new RuntimeException("List not found: " + listId));
        list.getMangas().removeIf(m -> m.getId().equals(mangaId));
        return readingListRepository.save(list);
    }

    @Transactional
    public void deleteList(Long id) {
        readingListRepository.deleteById(id);
    }
}
