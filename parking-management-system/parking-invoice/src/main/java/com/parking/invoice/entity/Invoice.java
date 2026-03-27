package com.parking.invoice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("invoice")
public class Invoice extends BaseEntity {

    private String invoiceNo;

    private Long userId;

    private Long titleId;

    private String plateNumber;

    private BigDecimal amount;

    private BigDecimal taxAmount;

    private String status;

    private String invoiceType;

    private LocalDateTime billingTime;

    private String sendingType;

    private String invoiceUrl;
}
