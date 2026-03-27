package com.parking.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.space.entity.ParkingArea;
import com.parking.space.service.ParkingAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "停车场区域管理")
@RestController
@RequestMapping("/api/space/v1/areas")
@RequiredArgsConstructor
public class ParkingAreaController {

    private final ParkingAreaService areaService;

    @Operation(summary = "区域列表")
    @GetMapping
    public Result<IPage<ParkingArea>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String areaName,
            @RequestParam(required = false) String floor) {
        return Result.success(areaService.page(current, size, areaName, floor));
    }

    @Operation(summary = "区域详情")
    @GetMapping("/{id}")
    public Result<ParkingArea> getById(@PathVariable Long id) {
        return Result.success(areaService.getById(id));
    }

    @Operation(summary = "创建区域")
    @PostMapping
    public Result<ParkingArea> save(@RequestBody ParkingArea area) {
        return Result.success(areaService.save(area));
    }

    @Operation(summary = "更新区域")
    @PutMapping("/{id}")
    public Result<ParkingArea> update(@PathVariable Long id, @RequestBody ParkingArea area) {
        area.setId(id);
        return Result.success(areaService.update(area));
    }

    @Operation(summary = "删除区域")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(areaService.delete(id) ? null : false);
    }

    @Operation(summary = "获取统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        return Result.success(areaService.getStatistics());
    }
}
