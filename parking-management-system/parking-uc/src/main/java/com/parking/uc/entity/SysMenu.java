package com.parking.uc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    private Long parentId;

    private String menuName;

    private String menuType;

    private String path;

    private String component;

    private String icon;

    private String permission;

    private Integer sortOrder;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
