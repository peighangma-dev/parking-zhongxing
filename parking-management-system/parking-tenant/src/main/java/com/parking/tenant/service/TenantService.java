package com.parking.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    public Tenant getByCode(String tenantCode) {
        return getByTenantCode(tenantCode);
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

    public IPage<Tenant> page(Integer current, Integer size, String tenantName, String status) {
        Page<Tenant> page = new Page<>(current, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(tenantName)) {
            wrapper.like(Tenant::getTenantName, tenantName);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Tenant::getStatus, status);
        }
        wrapper.orderByDesc(Tenant::getId);
        return this.page(page, wrapper);
    }

    public Tenant getByIdTenant(Long id) {
        Tenant tenant = this.getById(id);
        if (tenant == null) {
            throw new RuntimeException("租户不存在");
        }
        return tenant;
    }

    public Tenant create(Tenant tenant) {
        if (getByTenantCode(tenant.getTenantCode()) != null) {
            throw new RuntimeException("租户编码已存在");
        }
        if (tenant.getStatus() == null) {
            tenant.setStatus("active");
        }
        this.save(tenant);
        return tenant;
    }

    public Tenant update(Long id, Tenant tenant) {
        Tenant existing = this.getById(id);
        if (existing == null) {
            throw new RuntimeException("租户不存在");
        }
        tenant.setId(id);
        tenant.setTenantCode(null);
        tenant.setCreatedAt(null);
        this.updateById(tenant);
        return this.getByIdTenant(id);
    }

    public Boolean delete(Long id) {
        this.removeById(id);
        return true;
    }
}
