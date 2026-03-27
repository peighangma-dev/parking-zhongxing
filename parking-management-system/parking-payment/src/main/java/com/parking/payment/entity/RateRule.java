package com.parking.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("rate_rule")
public class RateRule extends BaseEntity {

    private String ruleName;

    private String ruleType;

    private BigDecimal firstHourFee;

    private BigDecimal subsequentFee;

    private BigDecimal dailyMaxFee;

    private String vehicleCategory;

    private Integer priority;

    private String status;
}
