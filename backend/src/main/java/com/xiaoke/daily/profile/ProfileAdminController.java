package com.xiaoke.daily.profile;

import com.xiaoke.daily.common.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/profile-sections")
public class ProfileAdminController {
    private final ProfileRepository repository;

    public ProfileAdminController(ProfileRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{id}")
    public ApiResponse<ProfileSection> update(@PathVariable Long id, @RequestBody ProfileSection request) {
        ProfileSection section = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("档案模块不存在"));
        section.setSectionTitle(request.getSectionTitle());
        section.setContent(request.getContent());
        section.setSortOrder(request.getSortOrder());
        section.setEnabled(request.isEnabled());
        return ApiResponse.ok(repository.save(section));
    }
}

