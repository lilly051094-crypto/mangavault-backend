package com.mangareader.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    @Column(nullable = false)
    private Double chapterNumber;

    private String title;

    @ElementCollection
    @CollectionTable(name = "chapter_pages", joinColumns = @JoinColumn(name = "chapter_id"))
    @Column(name = "page_path")
    @OrderColumn(name = "page_order")
    private List<String> pages = new ArrayList<>();

    private Integer pageCount;

    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
        pageCount = pages != null ? pages.size() : 0;
    }
}
