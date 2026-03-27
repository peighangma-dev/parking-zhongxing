package com.parking.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.monitor.entity.Alarm;
import com.parking.monitor.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "报警管理")
@RestController
@RequestMapping("/api/monitor/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(summary = "报警列表")
    @GetMapping
    public Result<IPage<Alarm>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) String alarmLevel,
            @RequestParam(required = false) String status) {
        return Result.success(alarmService.page(current, size, alarmType, alarmLevel, status));
    }

    @Operation(summary = "报警详情")
    @GetMapping("/{id}")
    public Result<Alarm> getById(@PathVariable Long id) {
        return Result.success(alarmService.getById(id));
    }

    @Operation(summary = "创建报警")
    @PostMapping
    public Result<Alarm> create(
            @RequestParam String alarmType,
            @RequestParam(required = false) Long cameraId,
            @RequestParam(required = false) Long laneId,
            @RequestParam(required = false, defaultValue = "medium") String alarmLevel,
            @RequestParam(required = false) String description) {
        return Result.success(alarmService.createAlarm(alarmType, cameraId, laneId, alarmLevel, description));
    }

    @Operation(summary = "确认报警")
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(
            @PathVariable Long id,
            @RequestParam(required = false) Long confirmedBy) {
        return Result.success(alarmService.confirmAlarm(id, confirmedBy) ? null : false);
    }

    @Operation(summary = "处理报警")
    @PutMapping("/{id}/resolve")
    public Result<Void> resolve(@PathVariable Long id) {
        return Result.success(alarmService.resolveAlarm(id) ? null : false);
    }
}
