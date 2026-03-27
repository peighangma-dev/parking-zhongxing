package com.parking.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("owner")
public class Owner extends BaseEntity {

    private String name;

    private String phone;

    private String idCard;

    private String address;
}
