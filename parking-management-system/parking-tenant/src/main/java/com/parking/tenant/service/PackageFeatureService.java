package com.parking.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.tenant.entity.PackageFeature;
import com.parking.tenant.mapper.PackageFeatureMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageFeatureService extends ServiceImpl<PackageFeatureMapper, PackageFeature> {

    public List<PackageFeature> getFeaturesByPackageId(Long packageId) {
        LambdaQueryWrapper<PackageFeature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PackageFeature::getPackageId, packageId);
        return this.list(wrapper);
    }

    public boolean hasFeature(Long packageId, String featureCode) {
        LambdaQueryWrapper<PackageFeature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PackageFeature::getPackageId, packageId)
               .eq(PackageFeature::getFeatureCode, featureCode)
               .eq(PackageFeature::getEnabled, 1);
        return this.count(wrapper) > 0;
    }

    public List<String> getEnabledFeatureCodes(Long packageId) {
        LambdaQueryWrapper<PackageFeature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PackageFeature::getPackageId, packageId)
               .eq(PackageFeature::getEnabled, 1);
        List<PackageFeature> features = this.list(wrapper);
        return features.stream()
                .map(PackageFeature::getFeatureCode)
                .toList();
    }
}
