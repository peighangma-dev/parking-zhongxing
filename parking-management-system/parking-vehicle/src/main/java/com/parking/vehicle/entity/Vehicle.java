package com.parking.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vehicle")
public class Vehicle extends BaseEntity {

    private String plateNumber;

    private String plateColor;

    private String vehicleBrand;

    private String vehicleType;

    private Long ownerId;

    private String vehicleTypeCat;

    private String status;
}
