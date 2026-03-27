package com.parking.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.oss.entity.FileInfo;
import com.parking.oss.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/api/oss/v1")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "文件列表")
    @GetMapping("/files")
    public Result<IPage<FileInfo>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String storageType) {
        return Result.success(fileService.page(current, size, fileName, storageType));
    }

    @Operation(summary = "文件详情")
    @GetMapping("/files/{id}")
    public Result<FileInfo> getById(@PathVariable Long id) {
        return Result.success(fileService.getById(id));
    }

    @Operation(summary = "文件上传")
    @PostMapping("/files/upload")
    public Result<FileInfo> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "common") String path) {
        return Result.success(fileService.upload(file, path));
    }

    @Operation(summary = "文件删除")
    @DeleteMapping("/files/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(fileService.delete(id) ? null : false);
    }
}
