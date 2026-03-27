package com.parking.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("alarm")
public class Alarm extends BaseEntity {

    private String alarmType;

    private Long cameraId;

    private Long laneId;

    private LocalDateTime alarmTime;

    private String alarmLevel;

    private String description;

    private String imageUrl;

    private String videoUrl;

    private String status;

    private Long confirmedBy;

    private LocalDateTime confirmedTime;
}
