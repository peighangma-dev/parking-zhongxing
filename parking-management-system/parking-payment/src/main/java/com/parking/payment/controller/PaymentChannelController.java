package com.parking.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.payment.entity.PaymentChannel;
import com.parking.payment.mapper.PaymentChannelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "支付渠道管理")
@RestController
@RequestMapping("/api/payment/v1/channels")
@RequiredArgsConstructor
public class PaymentChannelController {

    private final PaymentChannelMapper channelMapper;

    @Operation(summary = "渠道列表")
    @GetMapping
    public Result<IPage<PaymentChannel>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<PaymentChannel> page = new Page<>(current, size);
        return Result.success(channelMapper.selectPage(page, null));
    }

    @Operation(summary = "渠道详情")
    @GetMapping("/{id}")
    public Result<PaymentChannel> getById(@PathVariable Long id) {
        return Result.success(channelMapper.selectById(id));
    }

    @Operation(summary = "创建渠道")
    @PostMapping
    public Result<PaymentChannel> save(@RequestBody PaymentChannel channel) {
        channelMapper.insert(channel);
        return Result.success(channel);
    }

    @Operation(summary = "更新渠道")
    @PutMapping("/{id}")
    public Result<PaymentChannel> update(@PathVariable Long id, @RequestBody PaymentChannel channel) {
        channel.setId(id);
        channelMapper.updateById(channel);
        return Result.success(channel);
    }

    @Operation(summary = "删除渠道")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(channelMapper.deleteById(id) > 0 ? null : false);
    }
}
