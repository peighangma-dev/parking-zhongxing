package com.parking.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member")
public class Member extends BaseEntity {

    private Long ownerId;

    private Long vehicleId;

    private String memberType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal balance;

    private String status;
}
