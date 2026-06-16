package com.xiaoke.daily.memory;

import com.xiaoke.daily.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryService {
    private final MemoryRepository repository;

    public MemoryService(MemoryRepository repository) {
        this.repository = repository;
    }

    public PageResponse<MemoryResponse> page(int page, int pageSize) {
        PageRequest request = PageRequest.of(Math.max(page - 1, 0), pageSize);
        Page<MemoryEntry> result = repository.findByVisibilityOrderByCreatedAtDesc("PUBLIC", request);
        return new PageResponse<>(
                result.getContent().stream().map(MemoryResponse::from).toList(),
                page,
                pageSize,
                result.getTotalElements()
        );
    }

    public MemoryResponse get(Long id) {
        return MemoryResponse.from(repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回忆不存在")));
    }

    public MemoryEntry create(MemoryRequest request) {
        MemoryEntry entry = new MemoryEntry();
        apply(entry, request);
        return repository.save(entry);
    }

    public MemoryEntry update(Long id, MemoryRequest request) {
        MemoryEntry entry = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回忆不存在"));
        apply(entry, request);
        return repository.save(entry);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public MemoryResponse updateFavorite(Long id, boolean favorite) {
        MemoryEntry entry = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回忆不存在"));
        int nextCount = favorite
                ? entry.getFavoriteCount() + 1
                : Math.max(0, entry.getFavoriteCount() - 1);
        entry.setFavoriteCount(nextCount);
        return MemoryResponse.from(repository.save(entry));
    }

    public List<MemoryResponse> featured() {
        List<MemoryEntry> entries = repository.findTop3ByVisibilityAndFeaturedTrueOrderByCreatedAtDesc("PUBLIC");
        if (entries.isEmpty()) {
            entries = repository.findTop3ByVisibilityOrderByCreatedAtDesc("PUBLIC");
        }
        return entries.stream().map(MemoryResponse::from).toList();
    }

    private void apply(MemoryEntry entry, MemoryRequest request) {
        entry.setTitle(blankToDefault(request.title(), "未命名回忆"));
        entry.setSummary(blankToDefault(request.summary(), "这段日常被认真写进了手账。"));
        entry.setContent(blankToDefault(request.content(), entry.getSummary()));
        entry.setMemoryDate(request.memoryDate());
        entry.setCoverImageUrl(blankToDefault(request.coverImageUrl(), "/images/xiaoke.png"));
        entry.setCoverStoragePath(request.coverStoragePath());
        entry.setMoodTags(request.moodTags() == null ? "" : String.join(",", request.moodTags()));
        entry.setFeatured(Boolean.TRUE.equals(request.isFeatured()) || Boolean.TRUE.equals(request.featured()));
        entry.setVisibility(blankToDefault(request.visibility(), "PUBLIC"));
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
