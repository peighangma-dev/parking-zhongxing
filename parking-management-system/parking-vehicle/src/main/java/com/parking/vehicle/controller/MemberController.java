package com.parking.vehicle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.vehicle.entity.Member;
import com.parking.vehicle.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/vehicle/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/page")
    public Result<IPage<Member>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String memberType,
            @RequestParam(required = false) String status) {
        return Result.success(memberService.page(current, size, vehicleId, memberType, status));
    }

    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        return Result.success(memberService.getById(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public Result<Member> getByVehicleId(@PathVariable Long vehicleId) {
        return Result.success(memberService.getByVehicleId(vehicleId));
    }

    @GetMapping("/check/{vehicleId}")
    public Result<Boolean> isValid(@PathVariable Long vehicleId) {
        return Result.success(memberService.isValid(vehicleId));
    }

    @PostMapping
    public Result<Member> save(@RequestBody Member member) {
        return Result.success(memberService.save(member));
    }

    @PutMapping("/{id}/renew")
    public Result<Member> renew(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat LocalDate newEndDate) {
        return Result.success(memberService.renew(id, newEndDate));
    }

    @PutMapping("/{id}")
    public Result<Member> update(@PathVariable Long id, @RequestBody Member member) {
        member.setId(id);
        return Result.success(memberService.update(member));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(memberService.delete(id));
    }
}
