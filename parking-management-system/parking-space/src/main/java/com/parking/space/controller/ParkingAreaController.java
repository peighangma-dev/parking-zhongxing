package com.parking.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.space.entity.ParkingArea;
import com.parking.space.service.ParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/space/v1/areas")
public class ParkingAreaController {

    private final ParkingAreaService areaService;

    @Autowired
    public ParkingAreaController(ParkingAreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping
    public Result<IPage<ParkingArea>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String areaName,
            @RequestParam(required = false) String floor) {
        return Result.success(areaService.page(current, size, areaName, floor));
    }

    @GetMapping("/{id}")
    public Result<ParkingArea> getById(@PathVariable Long id) {
        return Result.success(areaService.getById(id));
    }

    @PostMapping
    public Result<ParkingArea> save(@RequestBody ParkingArea area) {
        return Result.success(areaService.save(area));
    }

    @PutMapping("/{id}")
    public Result<ParkingArea> update(@PathVariable Long id, @RequestBody ParkingArea area) {
        area.setId(id);
        return Result.success(areaService.update(area));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(areaService.delete(id));
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        return Result.success(areaService.getStatistics());
    }
}
