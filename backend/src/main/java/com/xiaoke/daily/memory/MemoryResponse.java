package com.xiaoke.daily.memory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record MemoryResponse(
        Long id,
        String title,
        String summary,
        String content,
        LocalDate memoryDate,
        String coverImageUrl,
        String coverStoragePath,
        List<String> moodTags,
        boolean featured,
        int favoriteCount,
        String visibility,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MemoryResponse from(MemoryEntry entry) {
        List<String> tags = entry.getMoodTags() == null || entry.getMoodTags().isBlank()
                ? List.of()
                : Arrays.stream(entry.getMoodTags().split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isBlank())
                    .toList();
        return new MemoryResponse(
                entry.getId(),
                entry.getTitle(),
                entry.getSummary(),
                entry.getContent(),
                entry.getMemoryDate(),
                entry.getCoverImageUrl(),
                entry.getCoverStoragePath(),
                tags,
                entry.isFeatured(),
                entry.getFavoriteCount(),
                entry.getVisibility(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }
}
