package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Vehicle;
import com.parking.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/page")
    public Result<IPage<Vehicle>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String vehicleTypeCat,
            @RequestParam(required = false) String status) {
        return Result.success(vehicleService.page(current, size, plateNumber, vehicleTypeCat, status));
    }

    @GetMapping("/{id}")
    public Result<Vehicle> getById(@PathVariable Long id) {
        return Result.success(vehicleService.getById(id));
    }

    @GetMapping("/plate/{plateNumber}")
    public Result<Vehicle> getByPlateNumber(@PathVariable String plateNumber) {
        return Result.success(vehicleService.getByPlateNumber(plateNumber));
    }

    @PostMapping
    public Result<Vehicle> save(@RequestBody Vehicle vehicle) {
        return Result.success(vehicleService.save(vehicle));
    }

    @PutMapping("/{id}")
    public Result<Vehicle> update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        return Result.success(vehicleService.update(vehicle));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(vehicleService.delete(id));
    }

    @PutMapping("/{id}/enable")
    public Result<Boolean> enable(@PathVariable Long id) {
        return Result.success(vehicleService.enable(id));
    }

    @PutMapping("/{id}/disable")
    public Result<Boolean> disable(@PathVariable Long id) {
        return Result.success(vehicleService.disable(id));
    }
}
