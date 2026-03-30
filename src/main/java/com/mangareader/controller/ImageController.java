package com.mangareader.controller;

import com.mangareader.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageController {

    private final FileStorageService fileStorageService;

    @GetMapping("/**")
    public ResponseEntity<byte[]> serveImage(jakarta.servlet.http.HttpServletRequest request) throws IOException {
        String path = request.getRequestURI().substring("/api/images/".length());
        Path filePath = fileStorageService.getFilePath(path);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(filePath);
        String filename = filePath.getFileName().toString().toLowerCase();

        MediaType mediaType = MediaType.IMAGE_JPEG;
        if (filename.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
        else if (filename.endsWith(".gif")) mediaType = MediaType.IMAGE_GIF;
        else if (filename.endsWith(".webp")) mediaType = MediaType.parseMediaType("image/webp");

        return ResponseEntity.ok()
            .contentType(mediaType)
            .cacheControl(CacheControl.maxAge(java.time.Duration.ofDays(7)))
            .body(imageBytes);
    }
}
