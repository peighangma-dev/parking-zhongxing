-- 停车场管理系统数据库初始化脚本
-- 数据库版本: 1.0.0
-- 创建时间: 2026-03-29

CREATE DATABASE IF NOT EXISTS parking_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parking_db;

-- ==================== 系统基础表 ====================

-- 租户表
CREATE TABLE IF NOT EXISTS sys_tenant (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_code` VARCHAR(32) NOT NULL COMMENT '租户编码',
    `database_name` VARCHAR(64) DEFAULT NULL COMMENT '数据库名称',
    `tenant_name` VARCHAR(64) NOT NULL COMMENT '租户名称',
    `contact_name` VARCHAR(64) DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone` VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(128) DEFAULT NULL COMMENT '联系邮箱',
    `domain` VARCHAR(128) DEFAULT NULL COMMENT '域名',
    `package_id` BIGINT DEFAULT NULL COMMENT '套餐ID',
    `expire_time` DATETIME DEFAULT NULL COMMENT '到期时间',
    `max_users` INT DEFAULT NULL COMMENT '最大用户数',
    `max_spaces` INT DEFAULT NULL COMMENT '最大车位数',
    `status` VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '状态: active/inactive/suspended',
    `created_at` DATETIME DEFAULT NULL COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(32) NOT NULL COMMENT '用户名',
    `password` VARCHAR(128) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `phone` VARCHAR(16) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(256) DEFAULT NULL COMMENT '头像URL',
    `status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `created_at` DATETIME DEFAULT NULL COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_code` VARCHAR(32) NOT NULL COMMENT '角色编码',
    `role_name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(256) DEFAULT NULL COMMENT '描述',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 菜单权限表
CREATE TABLE IF NOT EXISTS sys_menu (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `menu_name` VARCHAR(64) NOT NULL COMMENT '菜单名称',
    `menu_type` VARCHAR(16) NOT NULL COMMENT '类型: directory/menu/button',
    `path` VARCHAR(128) DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(256) DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(64) DEFAULT NULL COMMENT '图标',
    `permission` VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 套餐表
CREATE TABLE IF NOT EXISTS sys_package (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `package_code` VARCHAR(32) NOT NULL COMMENT '套餐编码',
    `package_name` VARCHAR(64) NOT NULL COMMENT '套餐名称',
    `package_type` VARCHAR(16) NOT NULL COMMENT '套餐类型',
    `description` VARCHAR(256) DEFAULT NULL COMMENT '描述',
    `max_users` INT DEFAULT NULL COMMENT '最大用户数',
    `max_spaces` INT DEFAULT NULL COMMENT '最大车位数',
    `max_devices` INT DEFAULT NULL COMMENT '最大设备数',
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '价格',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` VARCHAR(16) NOT NULL DEFAULT 'enabled' COMMENT '状态: enabled/disabled',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_package_code` (`package_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐表';

-- 套餐功能表
CREATE TABLE IF NOT EXISTS sys_package_feature (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `package_id` BIGINT NOT NULL COMMENT '套餐ID',
    `feature_code` VARCHAR(64) NOT NULL COMMENT '功能代码',
    `feature_name` VARCHAR(128) NOT NULL COMMENT '功能名称',
    `enabled` TINYINT DEFAULT 1 COMMENT '是否启用: 0-否 1-是',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_package_id` (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套餐功能表';

-- ==================== 停车场业务表 ====================

-- 停车场表
CREATE TABLE IF NOT EXISTS parking_lot (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `lot_name` VARCHAR(64) NOT NULL COMMENT '停车场名称',
    `lot_code` VARCHAR(32) NOT NULL COMMENT '停车场编码',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '地址',
    `total_spaces` INT NOT NULL DEFAULT 0 COMMENT '总车位数',
    `occupied_spaces` INT NOT NULL DEFAULT 0 COMMENT '已占用车位数',
    `contact_person` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
    `status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_lot_code` (`lot_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='停车场表';

-- 停车场区域表
CREATE TABLE IF NOT EXISTS parking_area (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `area_name` VARCHAR(64) NOT NULL COMMENT '区域名称',
    `total_spaces` INT NOT NULL COMMENT '总车位数量',
    `occupied_spaces` INT DEFAULT 0 COMMENT '已占用数量',
    `floor` VARCHAR(32) DEFAULT NULL COMMENT '楼层',
    `status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/maintenance/closed',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='停车场区域表';

-- 车位表
CREATE TABLE IF NOT EXISTS parking_space (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `space_number` VARCHAR(32) NOT NULL COMMENT '车位编号',
    `area_id` BIGINT NOT NULL COMMENT '所属区域ID',
    `space_type` VARCHAR(16) NOT NULL DEFAULT 'standard' COMMENT '类型: standard/large/disabled/electric',
    `status` VARCHAR(16) NOT NULL DEFAULT 'empty' COMMENT '状态: empty/occupied/reserved',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `vehicle_plate` VARCHAR(16) DEFAULT NULL COMMENT '当前车牌号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_space_number` (`space_number`),
    KEY `idx_area_id` (`area_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车位表';

-- ==================== 道闸设备表 ====================

-- 道闸设备表
CREATE TABLE IF NOT EXISTS barrier (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `code` VARCHAR(32) NOT NULL COMMENT '设备编码',
    `name` VARCHAR(64) NOT NULL COMMENT '设备名称',
    `location` VARCHAR(128) DEFAULT NULL COMMENT '安装位置',
    `total_lanes` INT DEFAULT 1 COMMENT '车道数量',
    `status` VARCHAR(16) NOT NULL DEFAULT 'offline' COMMENT '状态: online/offline/fault',
    `raise_timeout` INT DEFAULT 10 COMMENT '抬杆超时秒数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='道闸设备表';

-- 车道表
CREATE TABLE IF NOT EXISTS lane (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `barrier_id` BIGINT NOT NULL COMMENT '所属道闸ID',
    `lane_name` VARCHAR(32) NOT NULL COMMENT '车道名称',
    `lane_type` VARCHAR(16) NOT NULL COMMENT '类型: entry/exit',
    `camera_id` VARCHAR(64) DEFAULT NULL COMMENT '相机设备ID',
    `barrier_controller_id` VARCHAR(64) DEFAULT NULL COMMENT '道闸控制器ID',
    `status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/maintenance/fault',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_barrier_id` (`barrier_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车道表';

-- 摄像头表
CREATE TABLE IF NOT EXISTS camera (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `camera_code` VARCHAR(64) NOT NULL COMMENT '摄像头编码',
    `camera_name` VARCHAR(128) NOT NULL COMMENT '摄像头名称',
    `lane_id` BIGINT DEFAULT NULL COMMENT '关联车道ID',
    `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    `port` INT DEFAULT NULL COMMENT '端口',
    `username` VARCHAR(64) DEFAULT NULL COMMENT '用户名',
    `password` VARCHAR(128) DEFAULT NULL COMMENT '密码',
    `channel` INT DEFAULT 1 COMMENT '通道号',
    `stream_url` VARCHAR(512) DEFAULT NULL COMMENT '流地址',
    `status` VARCHAR(16) NOT NULL DEFAULT 'offline' COMMENT '状态: online/offline/fault',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_camera_code` (`camera_code`),
    KEY `idx_lane_id` (`lane_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄像头表';

-- ==================== 车辆管理表 ====================

-- 车主信息表
CREATE TABLE IF NOT EXISTS owner (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(64) NOT NULL COMMENT '车主姓名',
    `phone` VARCHAR(16) NOT NULL COMMENT '联系电话',
    `id_card` VARCHAR(32) DEFAULT NULL COMMENT '身份证号',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '联系地址',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车主信息表';

-- 车辆表
CREATE TABLE IF NOT EXISTS vehicle (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `plate_number` VARCHAR(16) NOT NULL COMMENT '车牌号',
    `plate_color` VARCHAR(16) DEFAULT NULL COMMENT '车牌颜色',
    `vehicle_brand` VARCHAR(64) DEFAULT NULL COMMENT '车辆品牌',
    `vehicle_type` VARCHAR(16) DEFAULT 'sedan' COMMENT '车型: sedan/suv/truck',
    `owner_id` BIGINT DEFAULT NULL COMMENT '车主ID',
    `vehicle_type_cat` VARCHAR(16) NOT NULL DEFAULT 'temp' COMMENT '分类: monthly/temp/vip/blacklist',
    `status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    `created_at` DATETIME DEFAULT NULL COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_plate_number` (`plate_number`),
    KEY `idx_owner_id` (`owner_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆表';

-- 黑名单表
CREATE TABLE IF NOT EXISTS blacklist (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
    `reason` VARCHAR(256) DEFAULT NULL COMMENT '加入原因',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑名单表';

-- 月卡会员表
CREATE TABLE IF NOT EXISTS member (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `owner_id` BIGINT NOT NULL COMMENT '车主ID',
    `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
    `member_type` VARCHAR(16) NOT NULL DEFAULT 'monthly' COMMENT '类型: monthly/seasonal/annual/vip',
    `start_date` DATE NOT NULL COMMENT '生效日期',
    `end_date` DATE NOT NULL COMMENT '到期日期',
    `balance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    `status` VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '状态: active/expired/cancelled',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_owner_id` (`owner_id`),
    KEY `idx_vehicle_id` (`vehicle_id`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月卡会员表';

-- ==================== 通行记录表 ====================

-- 通行记录表
CREATE TABLE IF NOT EXISTS pass_record (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `plate_number` VARCHAR(16) NOT NULL COMMENT '车牌号',
    `lane_id` BIGINT NOT NULL COMMENT '车道ID',
    `pass_type` VARCHAR(16) NOT NULL COMMENT '类型: entry/exit',
    `pass_status` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/abnormal',
    `recognition_confidence` DECIMAL(5,4) DEFAULT NULL COMMENT '识别置信度',
    `image_url` VARCHAR(256) DEFAULT NULL COMMENT '抓拍图片URL',
    `fee_calculated` DECIMAL(10,2) DEFAULT NULL COMMENT '计算费用',
    `pass_time` DATETIME NOT NULL COMMENT '通行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_plate_number` (`plate_number`),
    KEY `idx_lane_id` (`lane_id`),
    KEY `idx_pass_time` (`pass_time`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通行记录表';

-- ==================== 支付相关表 ====================

-- 费率规则表
CREATE TABLE IF NOT EXISTS rate_rule (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `rule_name` VARCHAR(64) NOT NULL COMMENT '规则名称',
    `rule_type` VARCHAR(16) NOT NULL DEFAULT 'default' COMMENT '类型: default/weekend/holiday/night',
    `first_hour_fee` DECIMAL(10,2) NOT NULL COMMENT '首小时费用',
    `subsequent_fee` DECIMAL(10,2) NOT NULL COMMENT '续费费用',
    `daily_max_fee` DECIMAL(10,2) DEFAULT NULL COMMENT '24小时封顶',
    `vehicle_category` VARCHAR(16) DEFAULT 'all' COMMENT '适用车型: all/small/large',
    `priority` INT DEFAULT 0 COMMENT '优先级',
    `status` VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '状态: active/inactive',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='费率规则表';

-- 支付渠道表
CREATE TABLE IF NOT EXISTS payment_channel (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `channel_code` VARCHAR(32) NOT NULL COMMENT '渠道编码',
    `channel_name` VARCHAR(64) NOT NULL COMMENT '渠道名称',
    `fee_rate` DECIMAL(5,4) DEFAULT 0.0000 COMMENT '手续费率',
    `config` TEXT DEFAULT NULL COMMENT '渠道配置JSON',
    `description` VARCHAR(256) DEFAULT NULL COMMENT '描述',
    `status` VARCHAR(16) NOT NULL DEFAULT 'enabled' COMMENT '状态: enabled/disabled',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_channel_code` (`channel_code`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付渠道表';

-- 支付记录表
CREATE TABLE IF NOT EXISTS payment (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
    `pass_record_id` BIGINT DEFAULT NULL COMMENT '关联通行记录ID',
    `plate_number` VARCHAR(16) NOT NULL COMMENT '车牌号',
    `amount_due` DECIMAL(10,2) NOT NULL COMMENT '应付金额',
    `amount_paid` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    `payment_channel_id` BIGINT DEFAULT NULL COMMENT '支付渠道ID',
    `payment_status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/paid/refunded/reversed',
    `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `transaction_id` VARCHAR(64) DEFAULT NULL COMMENT '第三方交易号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_plate_number` (`plate_number`),
    KEY `idx_payment_status` (`payment_status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- 发票抬头表
CREATE TABLE IF NOT EXISTS invoice_title (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title_type` VARCHAR(16) NOT NULL DEFAULT 'personal' COMMENT '类型: personal/enterprise',
    `company_name` VARCHAR(128) DEFAULT NULL COMMENT '公司名称',
    `tax_number` VARCHAR(64) DEFAULT NULL COMMENT '税号',
    `bank_name` VARCHAR(128) DEFAULT NULL COMMENT '开户银行',
    `bank_account` VARCHAR(64) DEFAULT NULL COMMENT '银行账号',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '地址',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '电话',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认: 0否/1是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发票抬头表';

-- 发票表
CREATE TABLE IF NOT EXISTS invoice (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `invoice_no` VARCHAR(32) NOT NULL COMMENT '发票号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title_id` BIGINT NOT NULL COMMENT '发票抬头ID',
    `plate_number` VARCHAR(16) DEFAULT NULL COMMENT '车牌号',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '发票金额',
    `tax_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '税额',
    `status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/approved/issued/rejected',
    `invoice_type` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '类型: normal/special',
    `billing_time` DATETIME DEFAULT NULL COMMENT '开票时间',
    `sending_type` VARCHAR(16) DEFAULT 'email' COMMENT '发送方式: email/快递',
    `invoice_url` VARCHAR(512) DEFAULT NULL COMMENT '发票PDF URL',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_invoice_no` (`invoice_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发票表';

-- ==================== 报警记录表 ====================

-- 报警记录表
CREATE TABLE IF NOT EXISTS alarm (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `alarm_type` VARCHAR(32) NOT NULL COMMENT '报警类型',
    `camera_id` BIGINT DEFAULT NULL COMMENT '摄像头ID',
    `lane_id` BIGINT DEFAULT NULL COMMENT '车道ID',
    `alarm_time` DATETIME NOT NULL COMMENT '报警时间',
    `alarm_level` VARCHAR(16) NOT NULL DEFAULT 'medium' COMMENT '级别: low/medium/high',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `image_url` VARCHAR(512) DEFAULT NULL COMMENT '抓拍图片',
    `video_url` VARCHAR(512) DEFAULT NULL COMMENT '关联视频',
    `status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/confirmed/resolved',
    `confirmed_by` BIGINT DEFAULT NULL COMMENT '确认人',
    `confirmed_time` DATETIME DEFAULT NULL COMMENT '确认时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_camera_id` (`camera_id`),
    KEY `idx_lane_id` (`lane_id`),
    KEY `idx_alarm_time` (`alarm_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报警记录表';

-- ==================== 文件存储表 ====================

-- 文件信息表
CREATE TABLE IF NOT EXISTS file_info (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `file_name` VARCHAR(256) NOT NULL COMMENT '文件名',
    `file_path` VARCHAR(512) NOT NULL COMMENT '文件路径',
    `file_url` VARCHAR(512) DEFAULT NULL COMMENT '访问URL',
    `file_size` BIGINT(20) DEFAULT NULL COMMENT '文件大小(字节)',
    `file_type` VARCHAR(64) DEFAULT NULL COMMENT '文件类型/MIME',
    `storage_type` VARCHAR(16) NOT NULL COMMENT '存储类型: local/aliyun/qiniu/tencent',
    `bucket_name` VARCHAR(128) DEFAULT NULL COMMENT '存储桶名称',
    `created_by` BIGINT DEFAULT NULL COMMENT '上传人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_created_by` (`created_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

-- 存储配置表
CREATE TABLE IF NOT EXISTS storage_config (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_key` VARCHAR(64) NOT NULL COMMENT '配置键',
    `provider` VARCHAR(16) NOT NULL COMMENT '提供商: aliyun/qiniu/tencent',
    `access_key` VARCHAR(256) DEFAULT NULL COMMENT '访问密钥',
    `secret_key` VARCHAR(256) DEFAULT NULL COMMENT '私有密钥',
    `bucket_name` VARCHAR(128) DEFAULT NULL COMMENT '存储桶名称',
    `endpoint` VARCHAR(256) DEFAULT NULL COMMENT '接入点',
    `domain` VARCHAR(256) DEFAULT NULL COMMENT '自定义域名',
    `status` VARCHAR(16) NOT NULL DEFAULT 'disabled' COMMENT '状态: enabled/disabled',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='存储配置表';

-- ==================== 视频录像表 ====================

-- 视频录像表
CREATE TABLE IF NOT EXISTS video_record (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `camera_id` BIGINT NOT NULL COMMENT '摄像头ID',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `file_path` VARCHAR(512) DEFAULT NULL COMMENT '文件路径',
    `file_size` BIGINT(20) DEFAULT NULL COMMENT '文件大小',
    `record_type` VARCHAR(16) NOT NULL DEFAULT 'continuous' COMMENT '类型: continuous/motion/alarm',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_camera_id` (`camera_id`),
    KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频录像表';

-- ==================== 日志表 ====================

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `username` VARCHAR(64) DEFAULT NULL COMMENT '操作人用户名',
    `operation` VARCHAR(64) DEFAULT NULL COMMENT '操作类型',
    `module` VARCHAR(64) DEFAULT NULL COMMENT '操作模块',
    `method` VARCHAR(128) DEFAULT NULL COMMENT '方法名',
    `request_url` VARCHAR(256) DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(16) DEFAULT NULL COMMENT '请求方法',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
    `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
    `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` TEXT DEFAULT NULL COMMENT '用户代理',
    `execution_time` BIGINT(20) DEFAULT NULL COMMENT '执行时长(毫秒)',
    `status` VARCHAR(16) DEFAULT NULL COMMENT '状态: success/failed',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ==================== 初始数据 ====================

-- 插入超级管理员角色
INSERT INTO sys_role (role_code, role_name, description, status) VALUES
('SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 'normal'),
('TENANT_ADMIN', '租户管理员', '租户管理员，拥有租户内所有权限', 'normal'),
('OPERATOR', '操作员', '普通操作员', 'normal');

-- 插入超级管理员用户 (密码: admin123)
INSERT INTO sys_user (username, password, nickname, status) VALUES
('admin', '$2b$10$N.zmdr9k7uOCQb376Ccx6eQyKHcR6gKtmY7lhBqHqFYQe8cUjBNm', '超级管理员', 'normal');

-- 绑定管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 插入默认套餐
INSERT INTO sys_package (package_code, package_name, package_type, description, max_users, max_spaces, max_devices, price, sort_order, status) VALUES
('BASIC', '基础版', 'basic', '适合小型停车场', 50, 100, 5, 299.00, 1, 'enabled'),
('STANDARD', '标准版', 'standard', '适合中型停车场', 200, 500, 20, 599.00, 2, 'enabled'),
('PROFESSIONAL', '专业版', 'professional', '适合大型停车场', 500, 2000, 50, 1299.00, 3, 'enabled'),
('ENTERPRISE', '企业版', 'enterprise', '适合集团客户', -1, -1, -1, 2999.00, 4, 'enabled');

-- 插入默认租户
INSERT INTO sys_tenant (tenant_code, tenant_name, status) VALUES
('DEMO', '演示租户', 'active');
