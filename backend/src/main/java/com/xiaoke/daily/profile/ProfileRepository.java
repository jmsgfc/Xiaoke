package com.xiaoke.daily.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<ProfileSection, Long> {
    List<ProfileSection> findByEnabledTrueOrderBySortOrderAsc();
}

