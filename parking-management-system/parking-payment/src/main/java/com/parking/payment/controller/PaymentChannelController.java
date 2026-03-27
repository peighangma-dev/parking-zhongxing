package com.parking.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.Result;
import com.parking.payment.entity.PaymentChannel;
import com.parking.payment.mapper.PaymentChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/v1/channels")
public class PaymentChannelController {

    private final PaymentChannelMapper channelMapper;

    @Autowired
    public PaymentChannelController(PaymentChannelMapper channelMapper) {
        this.channelMapper = channelMapper;
    }

    @GetMapping
    public Result<IPage<PaymentChannel>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<PaymentChannel> page = new Page<>(current, size);
        return Result.success(channelMapper.selectPage(page, null));
    }

    @GetMapping
    public Result<PaymentChannel> getById(@PathVariable Long id) {
        return Result.success(channelMapper.selectById(id));
    }

    @PostMapping
    public Result<PaymentChannel> save(@RequestBody PaymentChannel channel) {
        channelMapper.insert(channel);
        return Result.success(channel);
    }

    @PutMapping
    public Result<PaymentChannel> update(@PathVariable Long id, @RequestBody PaymentChannel channel) {
        channel.setId(id);
        channelMapper.updateById(channel);
        return Result.success(channel);
    }

    @DeleteMapping
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(channelMapper.deleteById(id) > 0);
    }
}
