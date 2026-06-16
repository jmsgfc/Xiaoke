package com.xiaoke.daily.storage;

import com.xiaoke.daily.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/uploads")
public class UploadController {
    private final LocalFileStorageService storageService;

    public UploadController(LocalFileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/image")
    public ApiResponse<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.ok(storageService.save(file));
    }
}

