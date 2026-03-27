package com.parking.uc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("parking_lot")
public class ParkingLot extends BaseEntity {

    private String lotName;

    private String lotCode;

    private String address;

    private Integer totalSpaces;

    private Integer occupiedSpaces;

    private String contactPerson;

    private String contactPhone;

    private String status;

    private String description;
}
