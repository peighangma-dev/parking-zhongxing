package com.parking.payment.adapter;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String orderNo;
    private String plateNumber;
    private BigDecimal amount;
    private String subject;
    private String channelCode;
}
