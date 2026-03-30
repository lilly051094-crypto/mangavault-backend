package com.mangareader.repository;

import com.mangareader.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    List<Manga> findByTitleContainingIgnoreCase(String title);

    List<Manga> findByStatus(String status);

    @Query("SELECT m FROM Manga m JOIN m.genres g WHERE g = :genre")
    List<Manga> findByGenre(@Param("genre") String genre);

    @Query("SELECT m FROM Manga m ORDER BY m.updatedAt DESC")
    List<Manga> findAllOrderByUpdatedAtDesc();
}
