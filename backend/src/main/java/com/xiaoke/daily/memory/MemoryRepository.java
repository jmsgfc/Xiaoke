package com.xiaoke.daily.memory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<MemoryEntry, Long> {
    Page<MemoryEntry> findByVisibilityOrderByCreatedAtDesc(String visibility, Pageable pageable);
    List<MemoryEntry> findTop3ByVisibilityAndFeaturedTrueOrderByCreatedAtDesc(String visibility);
    List<MemoryEntry> findTop3ByVisibilityOrderByCreatedAtDesc(String visibility);
}
