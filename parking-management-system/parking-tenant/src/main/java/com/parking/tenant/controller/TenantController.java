package com.parking.tenant.controller;

import com.parking.tenant.dto.TenantLoginDTO;
import com.parking.tenant.dto.TenantRegisterDTO;
import com.parking.tenant.entity.Package;
import com.parking.tenant.entity.Tenant;
import com.parking.tenant.service.PackageService;
import com.parking.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private PackageService packageService;

    @GetMapping("/packages")
    public List<Package> getPackages() {
        return packageService.getEnabledPackages();
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody TenantRegisterDTO dto) {
        Map<String, Object> result = new HashMap<>();
        
        Tenant existing = tenantService.getByTenantCode(dto.getTenantCode());
        if (existing != null) {
            result.put("success", false);
            result.put("message", "租户编码已存在");
            return result;
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

        boolean success = tenantService.registerTenant(tenant);
        result.put("success", success);
        result.put("message", success ? "注册成功" : "注册失败");
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody TenantLoginDTO dto) {
        Map<String, Object> result = new HashMap<>();
        
        Tenant tenant = tenantService.getByTenantCode(dto.getTenantCode());
        if (tenant == null) {
            result.put("success", false);
            result.put("message", "租户不存在");
            return result;
        }

        if (!"active".equals(tenant.getStatus())) {
            result.put("success", false);
            result.put("message", "租户已被禁用");
            return result;
        }

        result.put("success", true);
        result.put("tenantId", tenant.getId());
        result.put("tenantCode", tenant.getTenantCode());
        result.put("tenantName", tenant.getTenantName());
        result.put("packageId", tenant.getPackageId());
        return result;
    }

    @GetMapping("/info/{tenantCode}")
    public Tenant getTenantInfo(@PathVariable String tenantCode) {
        return tenantService.getByTenantCode(tenantCode);
    }

    @GetMapping("/list")
    public List<Tenant> list() {
        return tenantService.list();
    }

    @PutMapping("/status/{id}")
    public boolean updateStatus(@PathVariable Long id, @RequestParam String status) {
        return tenantService.updateTenantStatus(id, status);
    }
}
