package com.parking.space.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("parking_space")
public class ParkingSpace extends BaseEntity {

    private String spaceNumber;

    private Long areaId;

    private String spaceType;

    private String status;

    private String vehiclePlate;
}
