package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Member;
import com.parking.vehicle.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "月卡管理")
@RestController
@RequestMapping("/api/vehicle/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "月卡列表")
    @GetMapping
    public Result<IPage<Member>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String memberType,
            @RequestParam(required = false) String status) {
        return Result.success(memberService.page(current, size, vehicleId, memberType, status));
    }

    @Operation(summary = "月卡详情")
    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        return Result.success(memberService.getById(id));
    }

    @Operation(summary = "根据车辆ID查询有效月卡")
    @GetMapping("/vehicle/{vehicleId}")
    public Result<Member> getByVehicleId(@PathVariable Long vehicleId) {
        return Result.success(memberService.getByVehicleId(vehicleId));
    }

    @Operation(summary = "检查月卡是否有效")
    @GetMapping("/valid/{vehicleId}")
    public Result<Boolean> isValid(@PathVariable Long vehicleId) {
        return Result.success(memberService.isValid(vehicleId));
    }

    @Operation(summary = "开通月卡")
    @PostMapping
    public Result<Member> save(@RequestBody Member member) {
        return Result.success(memberService.save(member));
    }

    @Operation(summary = "月卡续期")
    @PostMapping("/{id}/renew")
    public Result<Member> renew(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat LocalDate newEndDate) {
        return Result.success(memberService.renew(id, newEndDate));
    }

    @Operation(summary = "更新月卡")
    @PutMapping("/{id}")
    public Result<Member> update(@PathVariable Long id, @RequestBody Member member) {
        member.setId(id);
        return Result.success(memberService.update(member));
    }

    @Operation(summary = "删除月卡")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(memberService.delete(id) ? null : false);
    }
}
