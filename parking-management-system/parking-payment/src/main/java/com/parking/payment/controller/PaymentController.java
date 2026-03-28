package com.parking.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.BusinessException;
import com.parking.common.core.Result;
import com.parking.common.core.context.TenantContext;
import com.parking.payment.entity.Payment;
import com.parking.payment.service.PaymentService;
import com.parking.payment.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment/v1")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentServiceImpl paymentServiceImpl;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentServiceImpl paymentServiceImpl) {
        this.paymentService = paymentService;
        this.paymentServiceImpl = paymentServiceImpl;
    }

    @GetMapping("/page")
    public Result<IPage<Payment>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String paymentStatus) {
        if (tenantId == null && TenantContext.canViewAllTenants()) {
            tenantId = null;
        } else if (tenantId == null) {
            tenantId = TenantContext.getTenantId();
        }
        return Result.success(paymentService.page(current, size, tenantId, orderNo, plateNumber, paymentStatus));
    }

    @GetMapping("/order/{orderNo}")
    public Result<Payment> getByOrderNo(@PathVariable String orderNo) {
        return Result.success(paymentService.getByOrderNo(orderNo));
    }

    @PostMapping("/create")
    public Result<Payment> createOrder(
            @RequestParam String plateNumber,
            @RequestParam(required = false) Long passRecordId,
            @RequestParam BigDecimal amountDue) {
        return Result.success(paymentService.createOrder(plateNumber, passRecordId, amountDue));
    }

    @PostMapping("/callback/{channel}")
    public Result<Payment> callback(
            @PathVariable String channel,
            @RequestParam String orderNo,
            @RequestParam String transactionId,
            @RequestParam(required = false) String signature) {
        if (!paymentServiceImpl.validateCallbackSignature(orderNo, transactionId, signature)) {
            throw new BusinessException(403, "签名校验失败");
        }
        Payment payment = paymentService.pay(orderNo, null, transactionId);
        return Result.success(payment);
    }

    @PostMapping("/cancel/{orderNo}")
    public Result<Boolean> cancel(@PathVariable String orderNo) {
        return Result.success(paymentService.cancel(orderNo));
    }

    @PostMapping("/reverse/{orderNo}")
    public Result<Boolean> reverse(
            @PathVariable String orderNo,
            @RequestParam(required = false) String reason) {
        return Result.success(paymentService.reverse(orderNo, reason));
    }

    @PostMapping("/refund/{orderNo}")
    public Result<Boolean> refund(
            @PathVariable String orderNo,
            @RequestParam BigDecimal amount) {
        return Result.success(paymentService.refund(orderNo, amount));
    }
}
