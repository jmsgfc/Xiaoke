package com.xiaoke.daily.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String uploadDir;
    private String publicUploadBaseUrl;
    private String adminUsername;
    private String adminPassword;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getPublicUploadBaseUrl() {
        return publicUploadBaseUrl;
    }

    public void setPublicUploadBaseUrl(String publicUploadBaseUrl) {
        this.publicUploadBaseUrl = publicUploadBaseUrl;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}

