package com.xiaoke.daily.auth;

import com.xiaoke.daily.config.AppProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final AppProperties properties;
    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Map<String, TokenSession> tokens = new ConcurrentHashMap<>();

    public AuthService(AppProperties properties, AdminUserRepository adminUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.properties = properties;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    void initializeAdminUser() {
        validateRequiredProperty(properties.getAdminUsername(), "ADMIN_INITIAL_USERNAME");
        validateRequiredProperty(properties.getAdminPassword(), "ADMIN_INITIAL_PASSWORD");
        adminUserRepository.findByUsername(properties.getAdminUsername())
                .orElseGet(() -> {
                    AdminUser user = new AdminUser();
                    user.setUsername(properties.getAdminUsername());
                    user.setPasswordHash(passwordEncoder.encode(properties.getAdminPassword()));
                    user.setNickname("管理员");
                    user.setEnabled(true);
                    return adminUserRepository.save(user);
                });
    }

    public Optional<LoginResponse> login(LoginRequest request) {
        if (request == null || request.username() == null || request.password() == null) {
            return Optional.empty();
        }
        Optional<AdminUser> matchedUser = adminUserRepository.findByUsername(request.username())
                .filter(AdminUser::isEnabled)
                .filter(user -> passwordEncoder.matches(request.password(), user.getPasswordHash()));
        if (matchedUser.isPresent()) {
            AdminUser user = matchedUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            adminUserRepository.save(user);
            String token = UUID.randomUUID().toString().replace("-", "");
            tokens.put(token, new TokenSession(user.getUsername(), Instant.now().plusSeconds(60L * 60 * 24)));
            return Optional.of(new LoginResponse(token, user.getNickname()));
        }
        return Optional.empty();
    }

    public boolean isValid(String token) {
        TokenSession session = tokens.get(token);
        if (session == null || session.expireAt().isBefore(Instant.now())) {
            tokens.remove(token);
            return false;
        }
        return true;
    }

    public AdminUser currentAdmin() {
        return adminUserRepository.findByUsername(properties.getAdminUsername())
                .orElseThrow(() -> new IllegalStateException("管理员账号尚未初始化"));
    }

    private void validateRequiredProperty(String value, String envName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + envName);
        }
    }

    private record TokenSession(String username, Instant expireAt) {
    }
}
