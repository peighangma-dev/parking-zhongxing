package com.parking.barrier.component;

public interface BarrierControl {

    BarrierResult raise(Long laneId);

    BarrierResult lower(Long laneId);

    String getStatus(Long laneId);

    boolean connect(String deviceId);

    void disconnect(String deviceId);
}
