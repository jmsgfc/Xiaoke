package com.xiaoke.daily.photo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "diary_note", length = 1000)
    private String diaryNote;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name = "storage_path")
    private String storagePath;
    @Column(name = "thumbnail_storage_path")
    private String thumbnailStoragePath;
    @Column(name = "taken_date")
    private LocalDate takenDate;
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @Column(name = "is_featured")
    private boolean featured;
    private String visibility = "PUBLIC";
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (uploadDate == null) uploadDate = now;
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
    public String getDiaryNote() { return diaryNote; }
    public void setDiaryNote(String diaryNote) { this.diaryNote = diaryNote; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
    public String getThumbnailStoragePath() { return thumbnailStoragePath; }
    public void setThumbnailStoragePath(String thumbnailStoragePath) { this.thumbnailStoragePath = thumbnailStoragePath; }
    public LocalDate getTakenDate() { return takenDate; }
    public void setTakenDate(LocalDate takenDate) { this.takenDate = takenDate; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
