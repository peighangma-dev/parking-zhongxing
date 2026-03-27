package com.parking.payment.adapter;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private boolean success;
    private String message;
    private String orderNo;
    private String transactionId;
    private String qrCode;
    private BigDecimal amount;
}
