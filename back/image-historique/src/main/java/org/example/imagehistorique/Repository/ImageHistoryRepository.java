package org.example.imagehistorique.Repository;

import org.example.imagehistorique.Entity.ImageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageHistoryRepository
        extends JpaRepository<ImageHistory, Long> {

    List<ImageHistory> findByDisease(String disease);
}

