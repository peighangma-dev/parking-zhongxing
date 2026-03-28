package com.parking.tenant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.tenant.dto.TenantLoginDTO;
import com.parking.tenant.dto.TenantRegisterDTO;
import com.parking.tenant.entity.Package;
import com.parking.tenant.entity.Tenant;
import com.parking.tenant.service.PackageService;
import com.parking.tenant.service.TenantService;
import com.parking.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenant/v1")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private PackageService packageService;

    @GetMapping("/packages")
    public Result<List<Package>> getPackages() {
        return Result.success(packageService.list());
    }

    @GetMapping("/page")
    public Result<IPage<Tenant>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String tenantName,
            @RequestParam(required = false) String status) {
        return Result.success(tenantService.page(current, size, tenantName, status));
    }

    @GetMapping("/list")
    public Result<List<Tenant>> list() {
        return Result.success(tenantService.list());
    }

    @GetMapping("/{id}")
    public Result<Tenant> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
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

    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setStatus(status);
        tenantService.update(id, tenant);
        return Result.success(true);
    }

    @PutMapping("/expire/{id}")
    public Result<Boolean> updateExpireTime(@PathVariable Long id, @RequestParam String expireTime) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setExpireTime(LocalDateTime.parse(expireTime));
        tenantService.update(id, tenant);
        return Result.success(true);
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody TenantRegisterDTO dto) {
        Map<String, Object> result = new HashMap<>();
        
        Tenant existing = tenantService.getByCode(dto.getTenantCode());
        if (existing != null) {
            return Result.error("租户编码已存在");
        }

        Tenant tenant = new Tenant();
        tenant.setTenantCode(dto.getTenantCode());
        tenant.setTenantName(dto.getTenantName());
        tenant.setContactName(dto.getContactName());
        tenant.setContactPhone(dto.getContactPhone());
        tenant.setContactEmail(dto.getContactEmail());
        tenant.setPackageId(dto.getPackageId());
        tenant.setStatus("active");
        tenant.setMaxUsers(10);
        tenant.setMaxSpaces(100);

        Tenant created = tenantService.create(tenant);
        result.put("tenantId", created.getId());
        result.put("tenantCode", created.getTenantCode());
        return Result.success(result);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody TenantLoginDTO dto) {
        Tenant tenant = tenantService.getByCode(dto.getTenantCode());
        if (tenant == null) {
            return Result.error("租户不存在");
        }

        if (!"active".equals(tenant.getStatus())) {
            return Result.error("租户已被禁用");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("tenantId", tenant.getId());
        result.put("tenantCode", tenant.getTenantCode());
        result.put("tenantName", tenant.getTenantName());
        result.put("packageId", tenant.getPackageId());
        return Result.success(result);
    }

    @GetMapping("/info/{tenantCode}")
    public Result<Tenant> getTenantInfo(@PathVariable String tenantCode) {
        return Result.success(tenantService.getByCode(tenantCode));
    }

    @GetMapping("/features/{tenantId}")
    public Result<List<String>> getFeatures(@PathVariable Long tenantId) {
        return Result.success(tenantService.getEnabledFeatures(tenantId));
    }
}
