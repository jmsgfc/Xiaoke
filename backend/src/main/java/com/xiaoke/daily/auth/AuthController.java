package com.xiaoke.daily.auth;

import com.xiaoke.daily.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request)
                .map(ApiResponse::ok)
                .orElseThrow(() -> new SecurityException("账号或密码错误"));
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, String>> me() {
        AdminUser admin = authService.currentAdmin();
        return ApiResponse.ok(Map.of(
                "username", admin.getUsername(),
                "nickname", admin.getNickname()
        ));
    }
}
