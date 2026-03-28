package com.parking.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.tenant.entity.PackageFeature;
import com.parking.tenant.entity.Tenant;
import com.parking.tenant.mapper.PackageFeatureMapper;
import com.parking.tenant.mapper.TenantMapper;
import com.parking.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantMapper tenantMapper;
    private final PackageFeatureMapper featureMapper;

    @Override
    public IPage<Tenant> page(Integer current, Integer size, String tenantName) {
        Page<Tenant> page = new Page<>(current, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        if (tenantName != null && !tenantName.isEmpty()) {
            wrapper.like(Tenant::getTenantName, tenantName);
        }
        wrapper.orderByDesc(Tenant::getId);
        return tenantMapper.selectPage(page, wrapper);
    }

    @Override
    public Tenant getById(Long id) {
        Tenant tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "租户不存在");
        }
        return tenant;
    }

    @Override
    public Tenant getByCode(String tenantCode) {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getTenantCode, tenantCode);
        return tenantMapper.selectOne(wrapper);
    }

    @Override
    public Tenant create(Tenant tenant) {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getTenantCode, tenant.getTenantCode());
        if (tenantMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "租户编码已存在");
        }
        if (tenant.getStatus() == null) {
            tenant.setStatus("active");
        }
        tenantMapper.insert(tenant);
        return tenant;
    }

    @Override
    public Tenant update(Long id, Tenant tenant) {
        Tenant existing = getById(id);
        tenant.setId(id);
        tenant.setTenantCode(null);
        tenant.setCreatedAt(null);
        tenantMapper.updateById(tenant);
        return getById(id);
    }

    @Override
    public Boolean delete(Long id) {
        getById(id);
        return tenantMapper.deleteById(id) > 0;
    }

    @Override
    public List<String> getEnabledFeatures(Long tenantId) {
        Tenant tenant = getById(tenantId);
        if ("active".equals(tenant.getStatus()) && 
            tenant.getExpireTime() != null && 
            tenant.getExpireTime().isAfter(LocalDateTime.now())) {
            LambdaQueryWrapper<PackageFeature> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PackageFeature::getPackageId, tenant.getPackageId());
            wrapper.eq(PackageFeature::getEnabled, true);
            return featureMapper.selectList(wrapper).stream()
                    .map(PackageFeature::getFeatureCode)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public Boolean checkFeature(Long tenantId, String featureCode) {
        return getEnabledFeatures(tenantId).contains(featureCode);
    }
}
