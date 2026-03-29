package com.parking.tenant.controller;

import com.parking.tenant.entity.Tenant;
import com.parking.tenant.service.TenantService;
import com.parking.tenant.service.PackageService;
import com.parking.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tenant")
public class TenantApiController {

    @Autowired
    private TenantService tenantService;
    
    @Autowired
    private PackageService packageService;

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
        
        if (success && password != null && !password.isEmpty()) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                Map<String, Object> userParams = new HashMap<>();
                userParams.put("username", tenantCode);
                userParams.put("password", password);
                userParams.put("nickname", contactName);
                userParams.put("tenantId", tenant.getId());
                userParams.put("phone", contactPhone);
                userParams.put("email", contactEmail);
                
                restTemplate.postForObject("http://localhost:8085/api/uc/v1/users", userParams, Map.class);
            } catch (Exception e) {
                result.put("userCreated", false);
            }
        }
        
        return Result.success(result);
    }
}
