package com.parking.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_tenant")
public class Tenant {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String tenantCode;
    
    private String databaseName;
    
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
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
}
