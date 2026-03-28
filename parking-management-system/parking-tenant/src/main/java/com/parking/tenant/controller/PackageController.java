package com.parking.tenant.controller;

import com.parking.tenant.entity.Package;
import com.parking.tenant.entity.PackageFeature;
import com.parking.tenant.service.PackageFeatureService;
import com.parking.tenant.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private PackageFeatureService packageFeatureService;

    @GetMapping("/list")
    public List<Package> list() {
        return packageService.list();
    }

    @GetMapping("/enabled")
    public List<Package> getEnabledPackages() {
        return packageService.getEnabledPackages();
    }

    @GetMapping("/features/{packageId}")
    public List<PackageFeature> getFeatures(@PathVariable Long packageId) {
        return packageFeatureService.getFeaturesByPackageId(packageId);
    }

    @GetMapping("/features/enabled/{packageId}")
    public List<String> getEnabledFeatureCodes(@PathVariable Long packageId) {
        return packageFeatureService.getEnabledFeatureCodes(packageId);
    }
}
