package com.parking.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blacklist")
public class Blacklist extends BaseEntity {

    private Long vehicleId;

    private String reason;

    @TableField("created_at")
    private LocalDateTime createdAt;

    private Long createdBy;
}
