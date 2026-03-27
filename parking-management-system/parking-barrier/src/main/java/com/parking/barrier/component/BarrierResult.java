package com.parking.barrier.component;

import lombok.Data;

@Data
public class BarrierResult {

    private boolean success;
    private String message;
    private Long laneId;
    private Long timeout;

    public static BarrierResult success(Long laneId) {
        BarrierResult result = new BarrierResult();
        result.setSuccess(true);
        result.setMessage("success");
        result.setLaneId(laneId);
        return result;
    }

    public static BarrierResult success(Long laneId, Long timeout) {
        BarrierResult result = success(laneId);
        result.setTimeout(timeout);
        return result;
    }

    public static BarrierResult fail(String message) {
        BarrierResult result = new BarrierResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
