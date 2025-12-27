package com.example.image_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final String UPLOAD_DIR = "uploads/";

    private final WebClient webClient;

    public ImageController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8005") // URL de ton AI-Service FastAPI
                .build();
    }

@PostMapping("/upload")
public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

    // 1. Sauvegarde
    File uploadDir = new File(UPLOAD_DIR);
    if (!uploadDir.exists()) uploadDir.mkdirs();

    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path filePath = Paths.get(UPLOAD_DIR + fileName);
    Files.write(filePath, file.getBytes());

    String imageUrl = "http://localhost:8082/images/" + fileName;

    // 2. Appel AI-Service
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", new FileSystemResource(filePath.toFile()));

    Map<String, Object> aiResult = webClient.post()
            .uri("/predict")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Map.class)
            .block(); // blocage pour récupérer la réponse

    // 3. Retour combiné
    Map<String, Object> response = new HashMap<>();
    response.put("imageUrl", imageUrl);
    response.put("prediction", aiResult);

    return ResponseEntity.ok(response);
}


    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}