package com.xiaoke.daily.site;

import org.springframework.stereotype.Service;

@Service
public class SiteConfigService {
    private final SiteConfigRepository repository;

    public SiteConfigService(SiteConfigRepository repository) {
        this.repository = repository;
    }

    public String value(String key, String fallback) {
        return repository.findByConfigKey(key)
                .map(SiteConfig::getConfigValue)
                .filter(value -> !value.isBlank())
                .orElse(fallback);
    }
}
