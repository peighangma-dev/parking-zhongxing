package com.parking.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_channel")
public class PaymentChannel extends BaseEntity {

    private String channelCode;

    private String channelName;

    private BigDecimal feeRate;

    private Map<String, String> config;

    private String description;

    private String status;

    private Integer sortOrder;
}
