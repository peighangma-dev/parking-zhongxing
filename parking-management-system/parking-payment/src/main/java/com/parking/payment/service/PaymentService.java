package com.parking.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.payment.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentService {

    IPage<Payment> page(Integer current, Integer size, Long tenantId, String orderNo, String plateNumber, String paymentStatus);

    Payment getById(Long id);

    Payment getByOrderNo(String orderNo);

    Payment getByTransactionId(String transactionId);

    Payment createOrder(String plateNumber, Long passRecordId, BigDecimal amountDue);

    Payment pay(String orderNo, Long channelId, String transactionId);

    boolean cancel(String orderNo);

    boolean reverse(String orderNo, String reason);

    boolean refund(String orderNo, BigDecimal amount);
}
