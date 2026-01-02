package org.example.imagehistorique.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "image_history")
@Data
public class ImageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String imageUrl;
    private String plant;
    private String disease;
    private String treatment;
    private LocalDateTime createdAt;
}