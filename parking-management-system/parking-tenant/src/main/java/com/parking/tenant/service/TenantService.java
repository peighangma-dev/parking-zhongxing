package com.parking.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.tenant.entity.Tenant;
import com.parking.tenant.mapper.TenantMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TenantService extends ServiceImpl<TenantMapper, Tenant> {

    public Tenant getByTenantCode(String tenantCode) {
        if (!StringUtils.hasText(tenantCode)) {
            return null;
        }
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getTenantCode, tenantCode);
        return this.getOne(wrapper);
    }

    public List<Tenant> getActiveTenants() {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getStatus, "active");
        return this.list(wrapper);
    }

    public boolean registerTenant(Tenant tenant) {
        tenant.setStatus("active");
        return this.save(tenant);
    }

    public boolean updateTenantStatus(Long id, String status) {
        Tenant tenant = this.getById(id);
        if (tenant != null) {
            tenant.setStatus(status);
            return this.updateById(tenant);
        }
        return false;
    }
}
