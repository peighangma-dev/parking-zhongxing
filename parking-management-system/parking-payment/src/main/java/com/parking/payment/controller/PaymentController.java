package com.parking.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.payment.entity.Payment;
import com.parking.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "支付交易管理")
@RestController
@RequestMapping("/api/payment/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "订单列表")
    @GetMapping("/orders")
    public Result<IPage<Payment>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String paymentStatus) {
        return Result.success(paymentService.page(current, size, orderNo, plateNumber, paymentStatus));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/orders/{orderNo}")
    public Result<Payment> getByOrderNo(@PathVariable String orderNo) {
        return Result.success(paymentService.getByOrderNo(orderNo));
    }

    @Operation(summary = "创建订单")
    @PostMapping("/orders")
    public Result<Payment> createOrder(
            @RequestParam String plateNumber,
            @RequestParam(required = false) Long passRecordId,
            @RequestParam BigDecimal amountDue) {
        return Result.success(paymentService.createOrder(plateNumber, passRecordId, amountDue));
    }

    @Operation(summary = "支付成功回调")
    @PostMapping("/callback/{channel}")
    public Result<Void> callback(
            @PathVariable String channel,
            @RequestParam String orderNo,
            @RequestParam String transactionId) {
        Payment payment = paymentService.pay(orderNo, null, transactionId);
        return Result.success(payment);
    }

    @Operation(summary = "取消订单")
    @PostMapping("/orders/{orderNo}/cancel")
    public Result<Void> cancel(@PathVariable String orderNo) {
        return Result.success(paymentService.cancel(orderNo) ? null : false);
    }

    @Operation(summary = "冲正订单")
    @PostMapping("/orders/{orderNo}/reverse")
    public Result<Void> reverse(
            @PathVariable String orderNo,
            @RequestParam(required = false) String reason) {
        return Result.success(paymentService.reverse(orderNo, reason) ? null : false);
    }

    @Operation(summary = "退款")
    @PostMapping("/orders/{orderNo}/refund")
    public Result<Void> refund(
            @PathVariable String orderNo,
            @RequestParam BigDecimal amount) {
        return Result.success(paymentService.refund(orderNo, amount) ? null : false);
    }
}
