package com.parking.barrier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.barrier.entity.Barrier;
import com.parking.barrier.service.BarrierService;
import com.parking.common.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barrier/v1/devices")
@RequiredArgsConstructor
public class BarrierController {

    private final BarrierService barrierService;

@GetMapping("/page")
    public Result<IPage<Barrier>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        return Result.success(barrierService.page(current, size, code, name, status));
    }

    @GetMapping("/{id}")
    public Result<Barrier> getById(@PathVariable Long id) {
        return Result.success(barrierService.getById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Barrier> getByCode(@PathVariable String code) {
        return Result.success(barrierService.getByCode(code));
    }

@PostMapping
    public Result<Barrier> save(@RequestBody Barrier barrier) {
        return Result.success(barrierService.save(barrier));
    }

    @PutMapping("/{id}")
    public Result<Barrier> update(@PathVariable Long id, @RequestBody Barrier barrier) {
        barrier.setId(id);
        return Result.success(barrierService.update(barrier));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(barrierService.delete(id));
    }

    @PostMapping("/{id}/status")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(barrierService.updateStatus(id, status));
    }
}
