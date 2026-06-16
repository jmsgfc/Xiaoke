package com.xiaoke.daily.profile;

import com.xiaoke.daily.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileRepository repository;

    public ProfileController(ProfileRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResponse<List<ProfileSection>> list() {
        return ApiResponse.ok(repository.findByEnabledTrueOrderBySortOrderAsc());
    }
}

