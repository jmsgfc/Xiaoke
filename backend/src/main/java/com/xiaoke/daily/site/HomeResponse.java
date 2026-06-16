package com.xiaoke.daily.site;

import com.xiaoke.daily.memory.MemoryResponse;
import com.xiaoke.daily.photo.Photo;

import java.util.List;

public record HomeResponse(
        String heroTitle,
        String heroSubtitle,
        String todayNote,
        String heroImageUrl,
        List<Photo> latestPhotos,
        List<MemoryResponse> featuredMemories,
        String anniversaryText
) {
}

