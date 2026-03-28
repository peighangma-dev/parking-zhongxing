package com.parking.uc.controller;

import com.parking.common.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uc/v1/config")
@RequiredArgsConstructor
public class ConfigController {

    @GetMapping
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        
        Map<String, Object> basic = new HashMap<>();
        basic.put("systemName", "停车场管理系统");
        basic.put("systemLogo", "/logo.png");
        basic.put("systemDescription", "一套功能完善的停车场管理系统");
        basic.put("version", "v2.0.26");
        
        Map<String, Object> security = new HashMap<>();
        security.put("passwordMinLength", 8);
        security.put("passwordRequireLetter", true);
        security.put("passwordRequireNumber", true);
        security.put("maxLoginFailCount", 5);
        security.put("loginFailLockMinutes", 30);
        security.put("tokenExpirationHours", 24);
        security.put("allowMultiLogin", false);
        
        Map<String, Object> business = new HashMap<>();
        business.put("defaultHourlyRate", 5.0);
        business.put("dailyMaxFee", 50.0);
        business.put("freeParkingMinutes", 15);
        business.put("defaultMemberValidDays", 30);
        business.put("autoAddToBlacklist", false);
        business.put("blacklistRule", "连续3次入场失败自动加入黑名单");
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("emailEnabled", false);
        notification.put("smsEnabled", false);
        
        Map<String, Object> storage = new HashMap<>();
        storage.put("type", "local");
        storage.put("localPath", "/data/uploads");
        storage.put("maxFileSize", 10);
        storage.put("allowedFileTypes", "jpg,png,pdf,doc,docx,xls,xlsx");
        
        config.put("basic", basic);
        config.put("security", security);
        config.put("business", business);
        config.put("notification", notification);
        config.put("storage", storage);
        
        return Result.success(config);
    }

    @PutMapping
    public Result<Boolean> saveConfig(@RequestBody Map<String, Object> config) {
        return Result.success(true);
    }
}
