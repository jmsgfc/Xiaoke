package com.xiaoke.daily.storage;

public record UploadResponse(
        String imageUrl,
        String thumbnailUrl,
        String storagePath,
        String thumbnailStoragePath
) {
}

