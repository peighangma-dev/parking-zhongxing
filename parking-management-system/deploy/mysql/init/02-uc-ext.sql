-- 多停车场管理表
CREATE TABLE IF NOT EXISTS `parking_lot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lot_name` VARCHAR(64) NOT NULL COMMENT '停车场名称',
  `lot_code` VARCHAR(32) NOT NULL COMMENT '停车场编码',
  `address` VARCHAR(256) DEFAULT NULL COMMENT '停车场地址',
  `total_spaces` INT NOT NULL DEFAULT 0 COMMENT '总车位数量',
  `occupied_spaces` INT NOT NULL DEFAULT 0 COMMENT '已占用车位数量',
  `contact_person` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
  `status` VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '状态 active/inactive',
  `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_lot_code` (`lot_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='停车场管理表';

-- 初始化管理员用户 (密码: admin123)
INSERT INTO `sys_user` (`username`, `password`, `nickname`, `status`, `created_at`, `updated_at`, `deleted`) 
VALUES ('admin', '7a1c0bf0b34fc17f5d511a8e1d1e0c8c', '系统管理员', 'normal', NOW(), NOW(), 0);

-- 初始化停车场数据
INSERT INTO `parking_lot` (`lot_name`, `lot_code`, `address`, `total_spaces`, `occupied_spaces`, `contact_person`, `contact_phone`, `status`, `created_at`, `updated_at`, `deleted`) 
VALUES ('中心停车场', 'PARKING_001', '北京市朝阳区建国路88号', 500, 120, '张三', '13800138001', 'active', NOW(), NOW(), 0);
