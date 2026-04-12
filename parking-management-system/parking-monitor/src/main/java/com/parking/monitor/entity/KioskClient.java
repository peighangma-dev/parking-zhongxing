package com.parking.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("kiosk_client")
public class KioskClient implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String kioskCode;

    private String kioskName;

    private Long barrierId;

    private Long laneId;

    private String status;

    private LocalDateTime lastHeartbeat;

    private String ipAddress;

    private String macAddress;

    private String remark;

    private Long tenantId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
