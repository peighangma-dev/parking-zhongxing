package com.parking.barrier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.barrier.entity.Lane;
import com.parking.barrier.service.LaneService;
import com.parking.common.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barrier/v1/lanes")
@RequiredArgsConstructor
public class LaneController {

    private final LaneService laneService;

    @GetMapping("/page")
    public Result<IPage<Lane>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long barrierId) {
        return Result.success(laneService.page(current, size, barrierId));
    }

    @GetMapping("/{id}")
    public Result<Lane> getById(@PathVariable Long id) {
        return Result.success(laneService.getById(id));
    }

    @PostMapping
    public Result<Lane> save(@RequestBody Lane lane) {
        return Result.success(laneService.save(lane));
    }

    @PutMapping("/{id}")
    public Result<Lane> update(@PathVariable Long id, @RequestBody Lane lane) {
        return Result.success(laneService.update(id, lane));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(laneService.delete(id));
    }
}
