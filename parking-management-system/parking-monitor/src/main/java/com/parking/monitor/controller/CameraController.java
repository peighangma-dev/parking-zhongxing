package com.parking.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.monitor.entity.Camera;
import com.parking.monitor.service.CameraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "摄像头管理")
@RestController
@RequestMapping("/api/monitor/v1/cameras")
@RequiredArgsConstructor
public class CameraController {

    private final CameraService cameraService;

    @Operation(summary = "摄像头列表")
    @GetMapping
    public Result<IPage<Camera>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String cameraCode,
            @RequestParam(required = false) String cameraName,
            @RequestParam(required = false) String status) {
        return Result.success(cameraService.page(current, size, cameraCode, cameraName, status));
    }

    @Operation(summary = "摄像头详情")
    @GetMapping("/{id}")
    public Result<Camera> getById(@PathVariable Long id) {
        return Result.success(cameraService.getById(id));
    }

    @Operation(summary = "创建摄像头")
    @PostMapping
    public Result<Camera> save(@RequestBody Camera camera) {
        return Result.success(cameraService.save(camera));
    }

    @Operation(summary = "更新摄像头")
    @PutMapping("/{id}")
    public Result<Camera> update(@PathVariable Long id, @RequestBody Camera camera) {
        camera.setId(id);
        return Result.success(cameraService.update(camera));
    }

    @Operation(summary = "删除摄像头")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(cameraService.delete(id) ? null : false);
    }

    @Operation(summary = "获取视频流地址")
    @GetMapping("/{id}/stream")
    public Result<String> getStreamUrl(@PathVariable Long id) {
        return Result.success(cameraService.getStreamUrl(id));
    }

    @Operation(summary = "抓图")
    @GetMapping("/{id}/capture")
    public Result<String> captureImage(@PathVariable Long id) {
        return Result.success(cameraService.captureImage(id));
    }
}
