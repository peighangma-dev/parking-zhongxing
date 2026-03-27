package com.parking.space.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.space.entity.ParkingSpace;
import com.parking.space.service.ParkingSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "车位管理")
@RestController
@RequestMapping("/api/space/v1/spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService spaceService;

    @Operation(summary = "车位列表")
    @GetMapping
    public Result<IPage<ParkingSpace>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) String spaceType,
            @RequestParam(required = false) String status) {
        return Result.success(spaceService.page(current, size, areaId, spaceType, status));
    }

    @Operation(summary = "车位详情")
    @GetMapping("/{id}")
    public Result<ParkingSpace> getById(@PathVariable Long id) {
        return Result.success(spaceService.getById(id));
    }

    @Operation(summary = "创建车位")
    @PostMapping
    public Result<ParkingSpace> save(@RequestBody ParkingSpace space) {
        return Result.success(spaceService.save(space));
    }

    @Operation(summary = "更新车位")
    @PutMapping("/{id}")
    public Result<ParkingSpace> update(@PathVariable Long id, @RequestBody ParkingSpace space) {
        space.setId(id);
        return Result.success(spaceService.update(space));
    }

    @Operation(summary = "删除车位")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(spaceService.delete(id) ? null : false);
    }

    @Operation(summary = "车位占用")
    @PostMapping("/{id}/occupy")
    public Result<Void> occupy(@PathVariable Long id, @RequestParam String plateNumber) {
        return Result.success(spaceService.occupy(id, plateNumber) ? null : false);
    }

    @Operation(summary = "车位释放")
    @PostMapping("/{id}/release")
    public Result<Void> release(@PathVariable Long id) {
        return Result.success(spaceService.release(id) ? null : false);
    }
}
