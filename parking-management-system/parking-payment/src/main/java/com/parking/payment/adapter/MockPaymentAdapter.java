package com.parking.payment.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
public class MockPaymentAdapter implements PaymentChannelAdapter {

    @Override
    public String getChannelCode() {
        return "MOCK";
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        log.info("模拟支付开始, orderNo={}, amount={}", request.getOrderNo(), request.getAmount());
        PaymentResponse response = new PaymentResponse();
        response.setSuccess(true);
        response.setMessage("支付成功(模拟)");
        response.setOrderNo(request.getOrderNo());
        response.setTransactionId(UUID.randomUUID().toString());
        response.setAmount(request.getAmount());
        response.setQrCode("https://mock.qrcode.com/" + request.getOrderNo());
        log.info("模拟支付完成, transactionId={}", response.getTransactionId());
        return response;
    }

    @Override
    public PaymentResponse query(String transactionId) {
        log.info("模拟查询交易, transactionId={}", transactionId);
        PaymentResponse response = new PaymentResponse();
        response.setSuccess(true);
        response.setMessage("交易已支付(模拟)");
        response.setTransactionId(transactionId);
        return response;
    }

    @Override
    public PaymentResponse refund(String transactionId, BigDecimal amount) {
        log.info("模拟退款, transactionId={}, amount={}", transactionId, amount);
        PaymentResponse response = new PaymentResponse();
        response.setSuccess(true);
        response.setMessage("退款成功(模拟)");
        response.setTransactionId(transactionId);
        response.setAmount(amount);
        return response;
    }
}
