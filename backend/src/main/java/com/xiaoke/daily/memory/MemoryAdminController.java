package com.xiaoke.daily.memory;

import com.xiaoke.daily.common.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/memories")
public class MemoryAdminController {
    private final MemoryService service;

    public MemoryAdminController(MemoryService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<MemoryResponse> create(@RequestBody MemoryRequest request) {
        return ApiResponse.ok(MemoryResponse.from(service.create(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<MemoryResponse> update(@PathVariable Long id, @RequestBody MemoryRequest request) {
        return ApiResponse.ok(MemoryResponse.from(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}

