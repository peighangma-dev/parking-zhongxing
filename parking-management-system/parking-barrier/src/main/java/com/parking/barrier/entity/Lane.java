package com.parking.barrier.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lane")
public class Lane extends BaseEntity {

    private Long barrierId;

    private String laneName;

    private String laneType;

    private String cameraId;

    private String barrierControllerId;

    private String status;
}
