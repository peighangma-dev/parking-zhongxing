package com.parking.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.tenant.entity.Package;
import com.parking.tenant.mapper.PackageMapper;
import org.springframework.stereotype.Service;

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
}
