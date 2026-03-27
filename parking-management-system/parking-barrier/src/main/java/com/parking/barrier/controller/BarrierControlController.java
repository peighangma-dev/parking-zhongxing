package com.parking.barrier.controller;

import com.parking.barrier.component.BarrierControl;
import com.parking.barrier.component.BarrierResult;
import com.parking.common.core.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "道闸控制")
@RestController
@RequestMapping("/api/barrier/v1/control")
@RequiredArgsConstructor
public class BarrierControlController {

    private final BarrierControl barrierControl;

    @Operation(summary = "抬杆")
    @PostMapping("/raise")
    public Result<BarrierResult> raise(@RequestParam Long laneId) {
        BarrierResult result = barrierControl.raise(laneId);
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error(result.getMessage());
        }
    }

    @Operation(summary = "落杆")
    @PostMapping("/lower")
    public Result<BarrierResult> lower(@RequestParam Long laneId) {
        BarrierResult result = barrierControl.lower(laneId);
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error(result.getMessage());
        }
    }

    @Operation(summary = "查询状态")
    @GetMapping("/status/{laneId}")
    public Result<String> getStatus(@PathVariable Long laneId) {
        return Result.success(barrierControl.getStatus(laneId));
    }
}
