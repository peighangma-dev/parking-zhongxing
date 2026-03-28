package com.parking.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment")
public class Payment extends BaseEntity {

    private Long tenantId;

    private String orderNo;

    private Long passRecordId;

    private String plateNumber;

    private BigDecimal amountDue;

    private BigDecimal amountPaid;

    private Long paymentChannelId;

    private String paymentStatus;

    private LocalDateTime paymentTime;

    private String transactionId;
}
