package com.parking.oss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.oss.entity.FileInfo;
import com.parking.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/v1")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public Result<IPage<FileInfo>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String storageType) {
        return Result.success(fileService.page(current, size, fileName, storageType));
    }

    @GetMapping("/{id}")
    public Result<FileInfo> getById(@PathVariable Long id) {
        return Result.success(fileService.getById(id));
    }

    @PostMapping("/upload")
    public Result<FileInfo> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "common") String path) {
        return Result.success(fileService.upload(file, path));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(fileService.delete(id));
    }
}
