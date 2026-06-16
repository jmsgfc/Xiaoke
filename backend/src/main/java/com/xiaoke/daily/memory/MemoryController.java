package com.xiaoke.daily.memory;

import com.xiaoke.daily.common.ApiResponse;
import com.xiaoke.daily.common.PageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {
    private final MemoryService service;

    public MemoryController(MemoryService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResponse<MemoryResponse>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "24") int pageSize) {
        return ApiResponse.ok(service.page(page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<MemoryResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(service.get(id));
    }

    @PostMapping("/{id}/favorite")
    public ApiResponse<MemoryResponse> favorite(@PathVariable Long id, @RequestBody MemoryFavoriteRequest request) {
        return ApiResponse.ok(service.updateFavorite(id, request.favorite()));
    }
}
