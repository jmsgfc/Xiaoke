package com.xiaoke.daily.memory;

import java.time.LocalDate;
import java.util.List;

public record MemoryRequest(
        String title,
        String summary,
        String content,
        LocalDate memoryDate,
        String coverImageUrl,
        String coverStoragePath,
        List<String> moodTags,
        Boolean isFeatured,
        Boolean featured,
        String visibility
) {
}

