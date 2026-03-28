package com.parking.barrier.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("barrier")
public class Barrier extends BaseEntity {

    private Long tenantId;

    private String code;

    private String name;

    private String location;

    private Integer totalLanes;

    private String status;

    private Integer raiseTimeout;
}
