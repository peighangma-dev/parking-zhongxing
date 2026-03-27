package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Owner;
import com.parking.vehicle.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "车主管理")
@RestController
@RequestMapping("/api/vehicle/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @Operation(summary = "车主列表")
    @GetMapping
    public Result<IPage<Owner>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {
        return Result.success(ownerService.page(current, size, name, phone));
    }

    @Operation(summary = "车主详情")
    @GetMapping("/{id}")
    public Result<Owner> getById(@PathVariable Long id) {
        return Result.success(ownerService.getById(id));
    }

    @Operation(summary = "创建车主")
    @PostMapping
    public Result<Owner> save(@RequestBody Owner owner) {
        return Result.success(ownerService.save(owner));
    }

    @Operation(summary = "更新车主")
    @PutMapping("/{id}")
    public Result<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        owner.setId(id);
        return Result.success(ownerService.update(owner));
    }

    @Operation(summary = "删除车主")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(ownerService.delete(id) ? null : false);
    }
}
