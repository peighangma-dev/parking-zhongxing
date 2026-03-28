package com.parking.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.tenant.entity.Package;
import com.parking.tenant.mapper.PackageMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PackageService extends ServiceImpl<PackageMapper, Package> {

    public List<Package> getEnabledPackages() {
        LambdaQueryWrapper<Package> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Package::getStatus, "enabled");
        wrapper.orderByAsc(Package::getSortOrder);
        return this.list(wrapper);
    }

    public Package getByPackageCode(String packageCode) {
        LambdaQueryWrapper<Package> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Package::getPackageCode, packageCode);
        return this.getOne(wrapper);
    }

    public IPage<Package> page(Integer current, Integer size, String packageName, String status) {
        Page<Package> page = new Page<>(current, size);
        LambdaQueryWrapper<Package> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(packageName)) {
            wrapper.like(Package::getPackageName, packageName);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Package::getStatus, status);
        }
        wrapper.orderByAsc(Package::getSortOrder);
        return this.page(page, wrapper);
    }

    public boolean createPackage(Package pkg) {
        if (getByPackageCode(pkg.getPackageCode()) != null) {
            throw new RuntimeException("套餐编码已存在");
        }
        if (pkg.getStatus() == null) {
            pkg.setStatus("enabled");
        }
        if (pkg.getSortOrder() == null) {
            pkg.setSortOrder(0);
        }
        return this.save(pkg);
    }

    public boolean updatePackage(Package pkg) {
        Package existing = this.getById(pkg.getId());
        if (existing == null) {
            throw new RuntimeException("套餐不存在");
        }
        pkg.setPackageCode(null);
        pkg.setCreatedAt(null);
        return this.updateById(pkg);
    }

    public boolean deletePackage(Long id) {
        return this.removeById(id);
    }
}
