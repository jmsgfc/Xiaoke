package com.xiaoke.daily.photo;

import com.xiaoke.daily.common.PageResponse;
import com.xiaoke.daily.storage.LocalFileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhotoService {
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy年M月");
    private final PhotoRepository repository;
    private final LocalFileStorageService storageService;

    public PhotoService(PhotoRepository repository, LocalFileStorageService storageService) {
        this.repository = repository;
        this.storageService = storageService;
    }

    public PageResponse<Photo> page(int page, int pageSize) {
        PageRequest request = PageRequest.of(Math.max(page - 1, 0), pageSize);
        Page<Photo> result = repository.findByVisibilityOrderByUploadDateDesc("PUBLIC", request);
        return new PageResponse<>(result.getContent(), page, pageSize, result.getTotalElements());
    }

    public List<PhotoGroupResponse> grouped() {
        Map<String, List<Photo>> groups = new LinkedHashMap<>();
        for (Photo photo : repository.findByVisibilityOrderByUploadDateDesc("PUBLIC")) {
            String key = photo.getUploadDate().format(MONTH_FORMAT);
            groups.computeIfAbsent(key, ignored -> new ArrayList<>()).add(photo);
        }
        return groups.entrySet().stream()
                .map(entry -> new PhotoGroupResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    public Photo get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("照片不存在"));
    }

    public Photo create(PhotoRequest request) {
        Photo photo = new Photo();
        apply(photo, request);
        return repository.save(photo);
    }

    public Photo update(Long id, PhotoRequest request) {
        Photo photo = get(id);
        apply(photo, request);
        return repository.save(photo);
    }

    public void delete(Long id) {
        Photo photo = get(id);
        try {
            storageService.deleteIfExists(photo.getStoragePath());
            storageService.deleteIfExists(photo.getThumbnailStoragePath());
        } catch (IOException e) {
            throw new IllegalStateException("删除图片文件失败", e);
        }
        repository.delete(photo);
    }

    public List<Photo> latest() {
        return repository.findTop6ByVisibilityOrderByUploadDateDesc("PUBLIC");
    }

    private void apply(Photo photo, PhotoRequest request) {
        photo.setTitle(blankToDefault(request.title(), "未命名照片"));
        photo.setDiaryNote(blankToDefault(request.diaryNote(), "这一天也被轻轻保存下来了。"));
        photo.setImageUrl(blankToDefault(request.imageUrl(), "/images/xiaoke.png"));
        photo.setThumbnailUrl(blankToDefault(request.thumbnailUrl(), photo.getImageUrl()));
        photo.setStoragePath(request.storagePath());
        photo.setThumbnailStoragePath(request.thumbnailStoragePath());
        photo.setTakenDate(request.takenDate());
        photo.setFeatured(Boolean.TRUE.equals(request.isFeatured()) || Boolean.TRUE.equals(request.featured()));
        photo.setVisibility(blankToDefault(request.visibility(), "PUBLIC"));
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
