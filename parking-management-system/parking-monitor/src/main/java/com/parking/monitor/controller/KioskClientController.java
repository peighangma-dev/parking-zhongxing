package com.parking.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.monitor.entity.KioskClient;
import com.parking.monitor.service.KioskClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor/v1/kiosk")
public class KioskClientController {

    @Autowired
    private KioskClientService kioskClientService;

    @GetMapping("/page")
    public Result<IPage<KioskClient>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(kioskClientService.page(current, size, null));
    }

    @GetMapping("/list")
    public Result<List<KioskClient>> list() {
        return Result.success(kioskClientService.list(null));
    }

    @GetMapping("/{id}")
    public Result<KioskClient> getById(@PathVariable Long id) {
        return Result.success(kioskClientService.getById(id));
    }

    @PostMapping
    public Result<KioskClient> create(@RequestBody KioskClient kiosk) {
        return Result.success(kioskClientService.create(kiosk));
    }

    @PutMapping("/{id}")
    public Result<KioskClient> update(@PathVariable Long id, @RequestBody KioskClient kiosk) {
        return Result.success(kioskClientService.update(id, kiosk));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(kioskClientService.delete(id));
    }

    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(kioskClientService.updateStatus(id, status));
    }

    @PostMapping("/heartbeat")
    public Result<Boolean> heartbeat(@RequestBody KioskClient kiosk) {
        return Result.success(kioskClientService.heartbeat(kiosk.getKioskCode(), kiosk.getIpAddress()));
    }
}
