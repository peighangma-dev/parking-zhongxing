package com.parking.tenant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.tenant.entity.Tenant;
import com.parking.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/v1")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping("/page")
    public Result<IPage<Tenant>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String tenantName) {
        return Result.success(tenantService.page(current, size, tenantName));
    }

    @GetMapping("/{id}")
    public Result<Tenant> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Tenant> getByCode(@PathVariable String code) {
        return Result.success(tenantService.getByCode(code));
    }

    @PostMapping
    public Result<Tenant> create(@RequestBody Tenant tenant) {
        return Result.success(tenantService.create(tenant));
    }

    @PutMapping("/{id}")
    public Result<Tenant> update(@PathVariable Long id, @RequestBody Tenant tenant) {
        return Result.success(tenantService.update(id, tenant));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(tenantService.delete(id));
    }

    @GetMapping("/{id}/features")
    public Result<List<String>> getFeatures(@PathVariable Long id) {
        return Result.success(tenantService.getEnabledFeatures(id));
    }

    @GetMapping("/{id}/check/{featureCode}")
    public Result<Boolean> checkFeature(@PathVariable Long id, @PathVariable String featureCode) {
        return Result.success(tenantService.checkFeature(id, featureCode));
    }
}
