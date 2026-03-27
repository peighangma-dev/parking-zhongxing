package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Vehicle;
import com.parking.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "车辆管理")
@RestController
@RequestMapping("/api/vehicle/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "车辆列表")
    @GetMapping
    public Result<IPage<Vehicle>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String vehicleTypeCat,
            @RequestParam(required = false) String status) {
        return Result.success(vehicleService.page(current, size, plateNumber, vehicleTypeCat, status));
    }

    @Operation(summary = "车辆详情")
    @GetMapping("/{id}")
    public Result<Vehicle> getById(@PathVariable Long id) {
        return Result.success(vehicleService.getById(id));
    }

    @Operation(summary = "根据车牌号查询")
    @GetMapping("/plate/{plateNumber}")
    public Result<Vehicle> getByPlateNumber(@PathVariable String plateNumber) {
        return Result.success(vehicleService.getByPlateNumber(plateNumber));
    }

    @Operation(summary = "注册车辆")
    @PostMapping
    public Result<Vehicle> save(@RequestBody Vehicle vehicle) {
        return Result.success(vehicleService.save(vehicle));
    }

    @Operation(summary = "更新车辆")
    @PutMapping("/{id}")
    public Result<Vehicle> update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        return Result.success(vehicleService.update(vehicle));
    }

    @Operation(summary = "删除车辆")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(vehicleService.delete(id) ? null : false);
    }

    @Operation(summary = "启用车辆")
    @PostMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        return Result.success(vehicleService.enable(id) ? null : false);
    }

    @Operation(summary = "禁用车辆")
    @PostMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        return Result.success(vehicleService.disable(id) ? null : false);
    }
}
