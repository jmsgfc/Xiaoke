package com.xiaoke.daily.photo;

import java.time.LocalDate;

public record PhotoRequest(
        String title,
        String diaryNote,
        String imageUrl,
        String thumbnailUrl,
        String storagePath,
        String thumbnailStoragePath,
        LocalDate takenDate,
        Boolean isFeatured,
        Boolean featured,
        String visibility
) {
}

