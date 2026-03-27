package com.parking.barrier.controller;

import com.parking.barrier.component.BarrierControl;
import com.parking.barrier.component.BarrierResult;
import com.parking.common.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barrier/v1/control")
@RequiredArgsConstructor
public class BarrierControlController {

    private final BarrierControl barrierControl;

    @PostMapping("/raise")
    public Result<BarrierResult> raise(@RequestParam Long laneId) {
        BarrierResult result = barrierControl.raise(laneId);
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error(result.getMessage());
        }
    }

    @PostMapping("/lower")
    public Result<BarrierResult> lower(@RequestParam Long laneId) {
        BarrierResult result = barrierControl.lower(laneId);
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error(result.getMessage());
        }
    }

    @GetMapping("/status/{laneId}")
    public Result<String> getStatus(@PathVariable Long laneId) {
        return Result.success(barrierControl.getStatus(laneId));
    }
}
