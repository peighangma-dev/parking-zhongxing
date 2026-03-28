package com.parking.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_package")
public class Package extends BaseEntity {

    private String packageCode;

    private String packageName;

    private String packageType;

    private String description;

    private Integer maxUsers;

    private Integer maxSpaces;

    private Integer maxDevices;

    private BigDecimal price;

    private Integer sortOrder;

    private String status;
}
