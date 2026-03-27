package com.parking.payment.adapter;

public interface PaymentChannelAdapter {

    String getChannelCode();

    PaymentResponse pay(PaymentRequest request);

    PaymentResponse query(String transactionId);

    PaymentResponse refund(String transactionId, java.math.BigDecimal amount);
}
