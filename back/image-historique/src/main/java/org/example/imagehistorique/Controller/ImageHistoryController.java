package org.example.imagehistorique.Controller;

import org.example.imagehistorique.Entity.ImageHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.imagehistorique.service.ImageHistoryService;

import java.util.List;

@RestController
@RequestMapping("/history")
public class ImageHistoryController {

    private final ImageHistoryService service;

    public ImageHistoryController(ImageHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ImageHistory> saveHistory(
            @RequestBody ImageHistory history) {
        return ResponseEntity.ok(service.save(history));
    }

    @GetMapping
    public ResponseEntity<List<ImageHistory>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}

