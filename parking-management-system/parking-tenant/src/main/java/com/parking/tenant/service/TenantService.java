package com.parking.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.tenant.entity.Tenant;

import java.util.List;

public interface TenantService {
    IPage<Tenant> page(Integer current, Integer size, String tenantName);
    Tenant getById(Long id);
    Tenant getByCode(String tenantCode);
    Tenant create(Tenant tenant);
    Tenant update(Long id, Tenant tenant);
    Boolean delete(Long id);
    List<String> getEnabledFeatures(Long tenantId);
    Boolean checkFeature(Long tenantId, String featureCode);
}
