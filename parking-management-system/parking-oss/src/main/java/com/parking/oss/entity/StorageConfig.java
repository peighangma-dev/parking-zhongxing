package com.parking.oss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("storage_config")
public class StorageConfig extends BaseEntity {

    private String configKey;

    private String provider;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private String endpoint;

    private String domain;

    private String status;
}
