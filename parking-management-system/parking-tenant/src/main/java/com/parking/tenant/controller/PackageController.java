package com.parking.tenant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.tenant.entity.Package;
import com.parking.tenant.entity.PackageFeature;
import com.parking.tenant.service.PackageFeatureService;
import com.parking.tenant.service.PackageService;
import com.parking.common.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/package/v1")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private PackageFeatureService packageFeatureService;

    @GetMapping("/page")
    public Result<IPage<Package>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String packageName,
            @RequestParam(required = false) String status) {
        return Result.success(packageService.page(current, size, packageName, status));
    }

    @GetMapping("/list")
    public Result<List<Package>> list() {
        return Result.success(packageService.list());
    }

    @GetMapping("/enabled")
    public Result<List<Package>> getEnabledPackages() {
        return Result.success(packageService.getEnabledPackages());
    }

    @GetMapping("/{id}")
    public Result<Package> getById(@PathVariable Long id) {
        return Result.success(packageService.getById(id));
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody Package pkg) {
        return Result.success(packageService.createPackage(pkg));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Package pkg) {
        pkg.setId(id);
        return Result.success(packageService.updatePackage(pkg));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(packageService.deletePackage(id));
    }

    @GetMapping("/features/{packageId}")
    public Result<List<PackageFeature>> getFeatures(@PathVariable Long packageId) {
        return Result.success(packageFeatureService.getFeaturesByPackageId(packageId));
    }

    @GetMapping("/features/enabled/{packageId}")
    public Result<List<String>> getEnabledFeatureCodes(@PathVariable Long packageId) {
        return Result.success(packageFeatureService.getEnabledFeatureCodes(packageId));
    }

    @GetMapping("/features/list/{packageId}")
    public Result<List<PackageFeature>> getFeatureList(@PathVariable Long packageId) {
        return Result.success(packageFeatureService.listByPackageId(packageId));
    }

    @PostMapping("/features")
    public Result<Boolean> addFeature(@RequestBody PackageFeature feature) {
        packageFeatureService.addFeature(feature);
        return Result.success(true);
    }

    @PutMapping("/features/{id}")
    public Result<Boolean> updateFeature(@PathVariable Long id, @RequestBody PackageFeature feature) {
        feature.setId(id);
        packageFeatureService.updateFeature(feature);
        return Result.success(true);
    }

    @DeleteMapping("/features/{id}")
    public Result<Boolean> deleteFeature(@PathVariable Long id) {
        packageFeatureService.deleteFeature(id);
        return Result.success(true);
    }
}
