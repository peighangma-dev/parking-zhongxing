package com.parking.uc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.uc.entity.ParkingLot;
import com.parking.uc.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uc/v1/lots")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @GetMapping("/page")
    public Result<IPage<ParkingLot>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String lotName) {
        return Result.success(parkingLotService.page(current, size, lotName));
    }

    @GetMapping("/{id}")
    public Result<ParkingLot> getById(@PathVariable Long id) {
        return Result.success(parkingLotService.getById(id));
    }

    @PostMapping
    public Result<ParkingLot> create(@RequestBody ParkingLot lot) {
        return Result.success(parkingLotService.create(lot));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody ParkingLot lot) {
        return Result.success(parkingLotService.update(id, lot));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(parkingLotService.delete(id));
    }
}
