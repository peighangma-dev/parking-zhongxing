package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Blacklist;
import com.parking.vehicle.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// @RestController
@RestController
@RequestMapping("/api/vehicle/v1/blacklist")
public class BlacklistController {

    private final BlacklistService blacklistService;

    @Autowired
    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @GetMapping
    public Result<IPage<Blacklist>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long vehicleId) {
        return Result.success(blacklistService.page(current, size, vehicleId));
    }

    @GetMapping
    public Result<Blacklist> getById(@PathVariable Long id) {
        return Result.success(blacklistService.getById(id));
    }

    @GetMapping
    public Result<Boolean> isBlacklisted(@PathVariable Long vehicleId) {
        return Result.success(blacklistService.isBlacklisted(vehicleId));
    }

    @PostMapping
    public Result<Blacklist> add(
            @RequestParam Long vehicleId,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) Long operatorId) {
        return Result.success(blacklistService.add(vehicleId, reason, operatorId));
    }

    @GetMapping
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.success(blacklistService.remove(id));
    }
}
