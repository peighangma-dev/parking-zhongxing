package com.parking.uc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.uc.entity.SysRole;
import com.parking.uc.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uc/v1/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @GetMapping("/page")
    public Result<IPage<SysRole>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String roleName) {
        return Result.success(roleService.page(current, size, roleName));
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/all")
    public Result<List<SysRole>> getAll() {
        return Result.success(roleService.getAll());
    }

    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        return Result.success(roleService.create(role));
    }

    @PutMapping("/{id}")
    public Result<SysRole> update(@PathVariable Long id, @RequestBody SysRole role) {
        return Result.success(roleService.update(id, role));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(roleService.delete(id));
    }
}
