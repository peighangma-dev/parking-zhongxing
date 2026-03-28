package com.parking.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_package_feature")
public class PackageFeature {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long packageId;
    
    private String featureCode;
    
    private String featureName;
    
    private Integer enabled;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
