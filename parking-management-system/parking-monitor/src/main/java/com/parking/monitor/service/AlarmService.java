package com.parking.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.monitor.entity.Alarm;
import java.time.LocalDateTime;

public interface AlarmService {

    IPage<Alarm> page(Integer current, Integer size, String alarmType, String alarmLevel, String status);

    Alarm getById(Long id);

    Alarm createAlarm(String alarmType, Long cameraId, Long laneId, String alarmLevel, String description);

    boolean confirmAlarm(Long id, Long confirmedBy);

    boolean resolveAlarm(Long id);
}
