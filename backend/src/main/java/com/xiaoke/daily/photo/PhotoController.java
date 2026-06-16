package com.xiaoke.daily.photo;

import com.xiaoke.daily.common.ApiResponse;
import com.xiaoke.daily.common.PageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService service;

    public PhotoController(PhotoService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResponse<Photo>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "24") int pageSize) {
        return ApiResponse.ok(service.page(page, pageSize));
    }

    @GetMapping("/grouped")
    public ApiResponse<List<PhotoGroupResponse>> grouped() {
        return ApiResponse.ok(service.grouped());
    }

    @GetMapping("/{id}")
    public ApiResponse<Photo> get(@PathVariable Long id) {
        return ApiResponse.ok(service.get(id));
    }
}

