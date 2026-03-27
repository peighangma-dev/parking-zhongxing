package com.parking.space.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("parking_area")
public class ParkingArea extends BaseEntity {

    private String areaName;

    private Integer totalSpaces;

    private Integer occupiedSpaces;

    private String floor;

    private String status;
}
