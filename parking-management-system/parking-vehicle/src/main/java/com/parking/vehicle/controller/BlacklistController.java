package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Blacklist;
import com.parking.vehicle.service.BlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "黑名单管理")
@RestController
@RequestMapping("/api/vehicle/v1/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService blacklistService;

    @Operation(summary = "黑名单列表")
    @GetMapping
    public Result<IPage<Blacklist>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long vehicleId) {
        return Result.success(blacklistService.page(current, size, vehicleId));
    }

    @Operation(summary = "黑名单详情")
    @GetMapping("/{id}")
    public Result<Blacklist> getById(@PathVariable Long id) {
        return Result.success(blacklistService.getById(id));
    }

    @Operation(summary = "检查车辆是否在黑名单")
    @GetMapping("/check/{vehicleId}")
    public Result<Boolean> isBlacklisted(@PathVariable Long vehicleId) {
        return Result.success(blacklistService.isBlacklisted(vehicleId));
    }

    @Operation(summary = "加入黑名单")
    @PostMapping
    public Result<Blacklist> add(
            @RequestParam Long vehicleId,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) Long operatorId) {
        return Result.success(blacklistService.add(vehicleId, reason, operatorId));
    }

    @Operation(summary = "移出黑名单")
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        return Result.success(blacklistService.remove(id) ? null : false);
    }
}
