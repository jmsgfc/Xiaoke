package com.xiaoke.daily.memory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "memory_entry")
public class MemoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String summary;
    @Column(length = 6000)
    private String content;
    @Column(name = "memory_date")
    private LocalDate memoryDate;
    @Column(name = "cover_image_url")
    private String coverImageUrl;
    @Column(name = "cover_storage_path")
    private String coverStoragePath;
    @Column(name = "mood_tags")
    private String moodTags;
    @Column(name = "is_featured")
    private boolean featured;
    @Column(name = "favorite_count")
    private Integer favoriteCount;
    private String visibility = "PUBLIC";
    @Column(name = "sort_order")
    private int sortOrder;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (visibility == null) visibility = "PUBLIC";
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDate getMemoryDate() { return memoryDate; }
    public void setMemoryDate(LocalDate memoryDate) { this.memoryDate = memoryDate; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getCoverStoragePath() { return coverStoragePath; }
    public void setCoverStoragePath(String coverStoragePath) { this.coverStoragePath = coverStoragePath; }
    public String getMoodTags() { return moodTags; }
    public void setMoodTags(String moodTags) { this.moodTags = moodTags; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public int getFavoriteCount() { return favoriteCount == null ? 0 : favoriteCount; }
    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
