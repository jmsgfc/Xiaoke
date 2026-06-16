package com.xiaoke.daily.photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findByVisibilityOrderByUploadDateDesc(String visibility, Pageable pageable);
    List<Photo> findTop6ByVisibilityAndFeaturedTrueOrderByUploadDateDesc(String visibility);
    List<Photo> findTop6ByVisibilityOrderByUploadDateDesc(String visibility);
    List<Photo> findByVisibilityOrderByUploadDateDesc(String visibility);
}
