package com.parking.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pass_record")
public class PassRecord extends BaseEntity {

    private String plateNumber;

    private Long laneId;

    private String passType;

    private String passStatus;

    private BigDecimal recognitionConfidence;

    private String imageUrl;

    private BigDecimal feeCalculated;

    private LocalDateTime passTime;

    private LocalDateTime createdAt;
}
