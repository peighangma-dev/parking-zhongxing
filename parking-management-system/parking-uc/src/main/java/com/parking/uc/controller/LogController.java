package com.parking.uc.controller;

import com.parking.common.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/uc/v1/logs")
@RequiredArgsConstructor
public class LogController {

    @GetMapping("/operation/page")
    public Result<Map<String, Object>> operationLogPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        List<Map<String, Object>> mockData = generateMockOperationLogs(current * size);
        int total = 100;
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", mockData);
        result.put("total", total);
        result.put("size", size);
        result.put("current", current);
        result.put("pages", (total + size - 1) / size);
        
        return Result.success(result);
    }

    @DeleteMapping("/operation/{id}")
    public Result<Boolean> deleteOperationLog(@PathVariable Long id) {
        return Result.success(true);
    }

    @PostMapping("/operation/batch-delete")
    public Result<Boolean> batchDeleteOperationLog(@RequestBody List<Long> ids) {
        return Result.success(true);
    }

    @GetMapping("/login/page")
    public Result<Map<String, Object>> loginLogPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        List<Map<String, Object>> mockData = generateMockLoginLogs(current * size);
        int total = 100;
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", mockData);
        result.put("total", total);
        result.put("size", size);
        result.put("current", current);
        result.put("pages", (total + size - 1) / size);
        
        return Result.success(result);
    }

    @DeleteMapping("/login/{id}")
    public Result<Boolean> deleteLoginLog(@PathVariable Long id) {
        return Result.success(true);
    }

    @PostMapping("/login/batch-delete")
    public Result<Boolean> batchDeleteLoginLog(@RequestBody List<Long> ids) {
        return Result.success(true);
    }

    @DeleteMapping("/login/clear")
    public Result<Boolean> clearLoginLogs() {
        return Result.success(true);
    }

    private List<Map<String, Object>> generateMockOperationLogs(int count) {
        List<Map<String, Object>> logs = new ArrayList<>();
        String[] modules = {"user", "role", "menu", "tenant", "barrier", "vehicle", "payment"};
        String[] actions = {"create", "update", "delete", "query"};
        String[] usernames = {"admin", "operator", "viewer"};
        String[] methods = {"GET", "POST", "PUT", "DELETE"};

        Random random = new Random();
        for (int i = 1; i <= Math.min(count, 10); i++) {
            Map<String, Object> log = new HashMap<>();
            log.put("id", (long) i);
            log.put("username", usernames[random.nextInt(usernames.length)]);
            log.put("module", modules[random.nextInt(modules.length)]);
            log.put("action", actions[random.nextInt(actions.length)]);
            log.put("description", "操作日志描述");
            log.put("method", methods[random.nextInt(methods.length)]);
            log.put("ipAddress", "192.168.1." + random.nextInt(255));
            log.put("createdAt", LocalDateTime.now().minusDays(random.nextInt(30)).toString());
            logs.add(log);
        }
        return logs;
    }

    private List<Map<String, Object>> generateMockLoginLogs(int count) {
        List<Map<String, Object>> logs = new ArrayList<>();
        String[] usernames = {"admin", "operator", "viewer", "manager"};
        String[] statuses = {"success", "fail"};
        String[] browsers = {"Chrome", "Firefox", "Safari", "Edge"};
        String[] osList = {"Windows 10", "macOS", "Ubuntu", "iOS", "Android"};
        String[] locations = {"北京市海淀区", "上海市浦东新区", "广州市天河区", "深圳市南山区", "杭州市西湖区"};

        Random random = new Random();
        for (int i = 1; i <= Math.min(count, 10); i++) {
            Map<String, Object> log = new HashMap<>();
            log.put("id", (long) i);
            log.put("username", usernames[random.nextInt(usernames.length)]);
            log.put("status", statuses[random.nextInt(statuses.length)]);
            log.put("ipAddress", "192.168." + random.nextInt(255) + "." + random.nextInt(255));
            log.put("ipLocation", locations[random.nextInt(locations.length)]);
            log.put("browser", browsers[random.nextInt(browsers.length)]);
            log.put("os", osList[random.nextInt(osList.length)]);
            log.put("message", "登录" + (random.nextBoolean() ? "成功" : "失败"));
            log.put("loginTime", LocalDateTime.now().minusDays(random.nextInt(30)).toString());
            logs.add(log);
        }
        return logs;
    }
}
