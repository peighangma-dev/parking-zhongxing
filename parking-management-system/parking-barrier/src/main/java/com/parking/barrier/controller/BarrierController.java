package com.parking.barrier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.barrier.entity.Barrier;
import com.parking.barrier.service.BarrierService;
import com.parking.common.core.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "道闸设备管理")
@RestController
@RequestMapping("/api/barrier/v1/devices")
@RequiredArgsConstructor
public class BarrierController {

    private final BarrierService barrierService;

    @Operation(summary = "设备列表")
    @GetMapping
    public Result<IPage<Barrier>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        return Result.success(barrierService.page(current, size, code, name, status));
    }

    @Operation(summary = "设备详情")
    @GetMapping("/{id}")
    public Result<Barrier> getById(@PathVariable Long id) {
        return Result.success(barrierService.getById(id));
    }

    @Operation(summary = "根据编码查询设备")
    @GetMapping("/code/{code}")
    public Result<Barrier> getByCode(@PathVariable String code) {
        return Result.success(barrierService.getByCode(code));
    }

    @Operation(summary = "注册设备")
    @PostMapping
    public Result<Barrier> save(@RequestBody Barrier barrier) {
        return Result.success(barrierService.save(barrier));
    }

    @Operation(summary = "更新设备")
    @PutMapping("/{id}")
    public Result<Barrier> update(@PathVariable Long id, @RequestBody Barrier barrier) {
        barrier.setId(id);
        return Result.success(barrierService.update(barrier));
    }

    @Operation(summary = "删除设备")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(barrierService.delete(id) ? null : false);
    }

    @Operation(summary = "更新设备状态")
    @PostMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(barrierService.updateStatus(id, status) ? null : false);
    }
}
