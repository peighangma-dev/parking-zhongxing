package com.parking.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.monitor.entity.Camera;
import com.parking.monitor.service.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitor/v1/cameras")
public class CameraController {

    private final CameraService cameraService;

    @Autowired
    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @GetMapping
    public Result<IPage<Camera>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String cameraCode,
            @RequestParam(required = false) String cameraName,
            @RequestParam(required = false) String status) {
        return Result.success(cameraService.page(current, size, cameraCode, cameraName, status));
    }

    @GetMapping("/{id}")
    public Result<Camera> getById(@PathVariable Long id) {
        return Result.success(cameraService.getById(id));
    }

    @PostMapping
    public Result<Camera> save(@RequestBody Camera camera) {
        return Result.success(cameraService.save(camera));
    }

    @PutMapping("/{id}")
    public Result<Camera> update(@PathVariable Long id, @RequestBody Camera camera) {
        camera.setId(id);
        return Result.success(cameraService.update(camera));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(cameraService.delete(id));
    }

    @GetMapping("/{id}/stream")
    public Result<String> getStreamUrl(@PathVariable Long id) {
        return Result.success(cameraService.getStreamUrl(id));
    }

    @GetMapping("/{id}/capture")
    public Result<String> captureImage(@PathVariable Long id) {
        return Result.success(cameraService.captureImage(id));
    }
}
