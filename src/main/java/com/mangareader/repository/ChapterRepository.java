package com.mangareader.repository;

import com.mangareader.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByMangaIdOrderByChapterNumberAsc(Long mangaId);

    Optional<Chapter> findByMangaIdAndChapterNumber(Long mangaId, Double chapterNumber);

    void deleteByMangaId(Long mangaId);
}
