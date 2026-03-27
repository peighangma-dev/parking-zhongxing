package com.parking.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("camera")
public class Camera extends BaseEntity {

    private String cameraCode;

    private String cameraName;

    private Long laneId;

    private String ipAddress;

    private Integer port;

    private String username;

    private String password;

    private Integer channel;

    private String streamUrl;

    private String status;
}
