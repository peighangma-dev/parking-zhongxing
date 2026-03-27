package com.parking.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.monitor.entity.Alarm;
import com.parking.monitor.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitor/v1/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping
    public Result<IPage<Alarm>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) String alarmLevel,
            @RequestParam(required = false) String status) {
        return Result.success(alarmService.page(current, size, alarmType, alarmLevel, status));
    }

    @GetMapping("/{id}")
    public Result<Alarm> getById(@PathVariable Long id) {
        return Result.success(alarmService.getById(id));
    }

    @PostMapping
    public Result<Alarm> create(
            @RequestParam String alarmType,
            @RequestParam(required = false) Long cameraId,
            @RequestParam(required = false) Long laneId,
            @RequestParam(required = false, defaultValue = "medium") String alarmLevel,
            @RequestParam(required = false) String description) {
        return Result.success(alarmService.createAlarm(alarmType, cameraId, laneId, alarmLevel, description));
    }

    @PutMapping("/{id}/confirm")
    public Result<Boolean> confirm(
            @PathVariable Long id,
            @RequestParam(required = false) Long confirmedBy) {
        return Result.success(alarmService.confirmAlarm(id, confirmedBy));
    }

    @PutMapping("/{id}/resolve")
    public Result<Boolean> resolve(@PathVariable Long id) {
        return Result.success(alarmService.resolveAlarm(id));
    }
}
