package com.parking.uc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.uc.entity.SysMenu;
import com.parking.uc.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uc/v1/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @GetMapping("/page")
    public Result<IPage<SysMenu>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "100") Integer size) {
        return Result.success(menuService.page(current, size));
    }

    @GetMapping("/tree")
    public Result<List<SysMenu>> getTree() {
        return Result.success(menuService.getTree());
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/by-role/{roleId}")
    public Result<List<Long>> getByRoleId(@PathVariable Long roleId) {
        return Result.success(menuService.getMenuIdsByRoleId(roleId));
    }

    @PostMapping
    public Result<SysMenu> create(@RequestBody SysMenu menu) {
        return Result.success(menuService.create(menu));
    }

    @PutMapping("/{id}")
    public Result<SysMenu> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        return Result.success(menuService.update(id, menu));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(menuService.delete(id));
    }

    @PutMapping("/role/{roleId}/menus")
    public Result<Boolean> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        return Result.success(menuService.assignMenus(roleId, menuIds));
    }
}
