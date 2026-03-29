package com.parking.tenant.controller;

import com.parking.tenant.entity.Tenant;
import com.parking.tenant.entity.Package;
import com.parking.tenant.service.TenantService;
import com.parking.tenant.service.PackageService;
import com.parking.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String tenantName,
            @RequestParam(required = false) String status) {
        
        List<Tenant> allTenants = tenantService.list();
        
        // 过滤
        if (tenantName != null && !tenantName.isEmpty()) {
            allTenants = allTenants.stream()
                .filter(t -> t.getTenantName() != null && t.getTenantName().contains(tenantName))
                .toList();
        }
        if (status != null && !status.isEmpty()) {
            allTenants = allTenants.stream()
                .filter(t -> status.equals(t.getStatus()))
                .toList();
        }
        
        int total = allTenants.size();
        int fromIndex = (current - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);
        List<Tenant> paged = fromIndex < total ? allTenants.subList(fromIndex, toIndex) : List.of();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", paged);
        result.put("total", total);
        result.put("size", size);
        result.put("current", current);
        result.put("pages", (total + size - 1) / size);
        
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result<List<Tenant>> list() {
        return Result.success(tenantService.list());
    }

    @GetMapping("/{id}")
    public Result<Tenant> getById(@PathVariable Long id) {
        return Result.success(tenantService.getByIdTenant(id));
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
        tenantService.delete(id);
        return Result.success(true);
    }

    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        tenantService.updateTenantStatus(id, status);
        return Result.success(true);
    }

    @GetMapping("/packages")
    public Result<List<Package>> getPackages() {
        return Result.success(packageService.getEnabledPackages());
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, Object> params) {
        String tenantCode = (String) params.get("tenantCode");
        String tenantName = (String) params.get("tenantName");
        String contactName = (String) params.get("contactName");
        String contactPhone = (String) params.get("contactPhone");
        String contactEmail = (String) params.get("contactEmail");
        String password = (String) params.get("password");
        Object packageIdObj = params.get("packageId");
        Long packageId = packageIdObj != null ? Long.valueOf(packageIdObj.toString()) : null;
        
        Tenant tenant = new Tenant();
        tenant.setTenantCode(tenantCode);
        tenant.setTenantName(tenantName);
        tenant.setContactName(contactName);
        tenant.setContactPhone(contactPhone);
        tenant.setContactEmail(contactEmail);
        tenant.setPackageId(packageId);
        tenant.setStatus("active");
        
        boolean success = tenantService.registerTenant(tenant);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("tenantId", tenant.getId());
        
        return Result.success(result);
    }
}
