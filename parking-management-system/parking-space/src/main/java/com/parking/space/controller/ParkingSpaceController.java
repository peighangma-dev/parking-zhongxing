package com.parking.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.space.entity.ParkingSpace;
import com.parking.space.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/space/v1/spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService spaceService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService spaceService) {
        this.spaceService = spaceService;
    }

@GetMapping("/page")
    public Result<IPage<ParkingSpace>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) String spaceType,
            @RequestParam(required = false) String status) {
        return Result.success(spaceService.page(current, size, areaId, spaceType, status));
    }

    @GetMapping("/{id}")
    public Result<ParkingSpace> getById(@PathVariable Long id) {
        return Result.success(spaceService.getById(id));
    }

@PostMapping
    public Result<ParkingSpace> save(@RequestBody ParkingSpace space) {
        return Result.success(spaceService.save(space));
    }

    @PutMapping("/{id}")
    public Result<ParkingSpace> update(@PathVariable Long id, @RequestBody ParkingSpace space) {
        space.setId(id);
        return Result.success(spaceService.update(space));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(spaceService.delete(id));
    }

    @PutMapping("/{id}/occupy")
    public Result<Boolean> occupy(@PathVariable Long id, @RequestParam String plateNumber) {
        return Result.success(spaceService.occupy(id, plateNumber));
    }

    @PutMapping("/{id}/release")
    public Result<Boolean> release(@PathVariable Long id) {
        return Result.success(spaceService.release(id));
    }
}
