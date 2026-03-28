package com.parking.tenant.controller;

import com.parking.tenant.entity.Tenant;
import com.parking.tenant.service.PackageFeatureService;
import com.parking.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenant")
public class TenantFeatureController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private PackageFeatureService packageFeatureService;

    @GetMapping("/features/{tenantId}")
    public Map<String, Object> getTenantFeatures(@PathVariable Long tenantId) {
        Map<String, Object> result = new HashMap<>();
        
        Tenant tenant = tenantService.getById(tenantId);
        if (tenant == null) {
            result.put("success", false);
            result.put("message", "租户不存在");
            return result;
        }

        List<String> features = packageFeatureService.getEnabledFeatureCodes(tenant.getPackageId());
        result.put("success", true);
        result.put("features", features);
        result.put("packageId", tenant.getPackageId());
        
        return result;
    }

    @GetMapping("/features/check")
    public Map<String, Object> checkFeature(@RequestParam Long tenantId, @RequestParam String featureCode) {
        Map<String, Object> result = new HashMap<>();
        
        Tenant tenant = tenantService.getById(tenantId);
        if (tenant == null) {
            result.put("enabled", false);
            return result;
        }

        boolean enabled = packageFeatureService.hasFeature(tenant.getPackageId(), featureCode);
        result.put("enabled", enabled);
        
        return result;
    }
}
