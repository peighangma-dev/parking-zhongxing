package com.parking.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.monitor.entity.Alarm;
import com.parking.monitor.mapper.AlarmMapper;
import com.parking.monitor.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmMapper alarmMapper;

    @Override
    public IPage<Alarm> page(Integer current, Integer size, String alarmType, String alarmLevel, String status) {
        Page<Alarm> page = new Page<>(current, size);
        LambdaQueryWrapper<Alarm> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(alarmType)) {
            wrapper.eq(Alarm::getAlarmType, alarmType);
        }
        if (StringUtils.hasText(alarmLevel)) {
            wrapper.eq(Alarm::getAlarmLevel, alarmLevel);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Alarm::getStatus, status);
        }
        wrapper.orderByDesc(Alarm::getAlarmTime);
        return alarmMapper.selectPage(page, wrapper);
    }

    @Override
    public Alarm getById(Long id) {
        Alarm alarm = alarmMapper.selectById(id);
        if (alarm == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return alarm;
    }

    @Override
    public Alarm createAlarm(String alarmType, Long cameraId, Long laneId, String alarmLevel, String description) {
        Alarm alarm = new Alarm();
        alarm.setAlarmType(alarmType);
        alarm.setCameraId(cameraId);
        alarm.setLaneId(laneId);
        alarm.setAlarmLevel(alarmLevel);
        alarm.setDescription(description);
        alarm.setAlarmTime(LocalDateTime.now());
        alarm.setStatus("pending");
        alarmMapper.insert(alarm);
        log.warn("报警记录创建, type={}, level={}, description={}", alarmType, alarmLevel, description);
        return alarm;
    }

    @Override
    public boolean confirmAlarm(Long id, Long confirmedBy) {
        Alarm alarm = getById(id);
        if (!"pending".equals(alarm.getStatus())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER, "只能确认待处理的报警");
        }
        alarm.setStatus("confirmed");
        alarm.setConfirmedBy(confirmedBy);
        alarm.setConfirmedTime(LocalDateTime.now());
        return alarmMapper.updateById(alarm) > 0;
    }

    @Override
    public boolean resolveAlarm(Long id) {
        Alarm alarm = getById(id);
        alarm.setStatus("resolved");
        return alarmMapper.updateById(alarm) > 0;
    }
}
