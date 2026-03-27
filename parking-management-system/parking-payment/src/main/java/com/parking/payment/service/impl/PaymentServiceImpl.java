package com.parking.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.payment.adapter.PaymentChannelAdapter;
import com.parking.payment.adapter.PaymentRequest;
import com.parking.payment.adapter.PaymentResponse;
import com.parking.payment.entity.Payment;
import com.parking.payment.mapper.PaymentMapper;
import com.parking.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final Map<String, PaymentChannelAdapter> adapters = new ConcurrentHashMap<>();

    @Override
    public IPage<Payment> page(Integer current, Integer size, String orderNo, String plateNumber, String paymentStatus) {
        Page<Payment> page = new Page<>(current, size);
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(orderNo)) {
            wrapper.eq(Payment::getOrderNo, orderNo);
        }
        if (StringUtils.hasText(plateNumber)) {
            wrapper.eq(Payment::getPlateNumber, plateNumber);
        }
        if (StringUtils.hasText(paymentStatus)) {
            wrapper.eq(Payment::getPaymentStatus, paymentStatus);
        }
        wrapper.orderByDesc(Payment::getCreatedAt);
        return paymentMapper.selectPage(page, wrapper);
    }

    @Override
    public Payment getById(Long id) {
        Payment payment = paymentMapper.selectById(id);
        if (payment == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return payment;
    }

    @Override
    public Payment getByOrderNo(String orderNo) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getOrderNo, orderNo);
        Payment payment = paymentMapper.selectOne(wrapper);
        if (payment == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return payment;
    }

    @Override
    public Payment getByTransactionId(String transactionId) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getTransactionId, transactionId);
        return paymentMapper.selectOne(wrapper);
    }

    @Override
    public Payment createOrder(String plateNumber, Long passRecordId, BigDecimal amountDue) {
        Payment payment = new Payment();
        payment.setOrderNo(generateOrderNo());
        payment.setPlateNumber(plateNumber);
        payment.setPassRecordId(passRecordId);
        payment.setAmountDue(amountDue);
        payment.setAmountPaid(BigDecimal.ZERO);
        payment.setPaymentStatus("pending");
        paymentMapper.insert(payment);
        log.info("创建支付订单, orderNo={}, amount={}", payment.getOrderNo(), amountDue);
        return payment;
    }

    @Override
    public Payment pay(String orderNo, Long channelId, String transactionId) {
        Payment payment = getByOrderNo(orderNo);
        if (!"pending".equals(payment.getPaymentStatus())) {
            throw new BusinessException(ErrorCode.ORDER_CANCELLED);
        }
        payment.setPaymentChannelId(channelId);
        payment.setTransactionId(transactionId);
        payment.setPaymentStatus("paid");
        payment.setPaymentTime(LocalDateTime.now());
        payment.setAmountPaid(payment.getAmountDue());
        paymentMapper.updateById(payment);
        log.info("支付成功, orderNo={}, transactionId={}", orderNo, transactionId);
        return payment;
    }

    @Override
    public boolean cancel(String orderNo) {
        Payment payment = getByOrderNo(orderNo);
        if (!"pending".equals(payment.getPaymentStatus())) {
            throw new BusinessException(ErrorCode.ORDER_CANCELLED);
        }
        payment.setPaymentStatus("cancelled");
        paymentMapper.updateById(payment);
        log.info("取消订单, orderNo={}", orderNo);
        return true;
    }

    @Override
    public boolean reverse(String orderNo, String reason) {
        Payment payment = getByOrderNo(orderNo);
        payment.setPaymentStatus("reversed");
        paymentMapper.updateById(payment);
        log.info("冲正订单, orderNo={}, reason={}", orderNo, reason);
        return true;
    }

    @Override
    public boolean refund(String orderNo, BigDecimal amount) {
        Payment payment = getByOrderNo(orderNo);
        if (!"paid".equals(payment.getPaymentStatus())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER, "只能退款已支付的订单");
        }
        payment.setPaymentStatus("refunded");
        paymentMapper.updateById(payment);
        log.info("退款订单, orderNo={}, amount={}", orderNo, amount);
        return true;
    }

    private String generateOrderNo() {
        return "P" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
