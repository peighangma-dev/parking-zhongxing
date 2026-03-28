package com.parking.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class Tenant extends BaseEntity {

    private String tenantCode;

    private String tenantName;

    private String contactName;

    private String contactPhone;

    private String contactEmail;

    private String domain;

    private Long packageId;

    private LocalDateTime expireTime;

    private Integer maxUsers;

    private Integer maxSpaces;

    private String status;
}
