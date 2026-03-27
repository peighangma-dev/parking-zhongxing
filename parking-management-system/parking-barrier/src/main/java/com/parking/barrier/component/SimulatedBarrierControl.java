package com.parking.barrier.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SimulatedBarrierControl implements BarrierControl {

    private final Map<Long, String> deviceStatus = new ConcurrentHashMap<>();

    @Override
    public BarrierResult raise(Long laneId) {
        log.info("执行抬杆操作, laneId={}", laneId);
        deviceStatus.put(laneId, "raising");
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        deviceStatus.put(laneId, "raised");
        log.info("抬杆完成, laneId={}", laneId);
        return BarrierResult.success(laneId, 10L);
    }

    @Override
    public BarrierResult lower(Long laneId) {
        log.info("执行落杆操作, laneId={}", laneId);
        deviceStatus.put(laneId, "lowering");
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        deviceStatus.put(laneId, "lowered");
        log.info("落杆完成, laneId={}", laneId);
        return BarrierResult.success(laneId);
    }

    @Override
    public String getStatus(Long laneId) {
        return deviceStatus.getOrDefault(laneId, "unknown");
    }

    @Override
    public boolean connect(String deviceId) {
        log.info("连接道闸设备, deviceId={}", deviceId);
        return true;
    }

    @Override
    public void disconnect(String deviceId) {
        log.info("断开道闸设备连接, deviceId={}", deviceId);
    }
}
