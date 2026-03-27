package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Owner;
import com.parking.vehicle.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// @RestController
@RestController
@RequestMapping("/api/vehicle/v1/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public Result<IPage<Owner>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {
        return Result.success(ownerService.page(current, size, name, phone));
    }

    @GetMapping
    public Result<Owner> getById(@PathVariable Long id) {
        return Result.success(ownerService.getById(id));
    }

    @PostMapping
    public Result<Owner> save(@RequestBody Owner owner) {
        return Result.success(ownerService.save(owner));
    }

    @GetMapping
    public Result<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        owner.setId(id);
        return Result.success(ownerService.update(owner));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(ownerService.delete(id));
    }
}
