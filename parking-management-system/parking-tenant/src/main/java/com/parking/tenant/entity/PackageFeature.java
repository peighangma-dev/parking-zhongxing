package com.parking.tenant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_package_feature")
public class PackageFeature implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long packageId;

    private String featureCode;

    private String featureName;

    private Boolean enabled;

    private LocalDateTime createdAt;
}
