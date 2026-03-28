package com.parking.tenant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_package")
public class Package {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String packageCode;
    
    private String packageName;
    
    private String packageType;
    
    private String description;
    
    private Integer maxUsers;
    
    private Integer maxSpaces;
    
    private Integer maxDevices;
    
    private BigDecimal price;
    
    private Integer sortOrder;
    
    private String status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
}
