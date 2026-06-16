package com.xiaoke.daily.site;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteConfigRepository extends JpaRepository<SiteConfig, Long> {
    Optional<SiteConfig> findByConfigKey(String configKey);
}
