-- 停车场管理系统 - 初始化数据
-- 数据库版本: 2.0.0
-- 生成时间: 2026-04-10

CREATE DATABASE IF NOT EXISTS parking_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parking_db;

-- ============================================
-- 1. 租户数据 (sys_tenant)
-- ============================================
INSERT INTO sys_tenant (tenant_code, tenant_name, contact_name, contact_phone, contact_email, max_users, max_spaces, status, created_at, updated_at) VALUES
('DEMO', '智慧停车场管理有限公司', '张三', '13800138001', 'zhangsan@example.com', 50, 500, 'active', NOW(), NOW()),
('TENANT001', '星河购物中心停车场', '李四', '13800138002', 'lisi@example.com', 30, 300, 'active', NOW(), NOW()),
('TENANT002', '市民公园停车场', '王五', '13800138003', 'wangwu@example.com', 20, 200, 'active', NOW(), NOW());

-- ============================================
-- 2. 用户数据 (sys_user)
-- 密码统一为: admin123 (BCrypt加密)
-- ============================================
INSERT INTO sys_user (username, password, nickname, phone, email, status, tenant_id, created_at, updated_at) VALUES
('admin', '$2b$10$2ygC4JoFO89uDipKUrAtAuWgK3Zl6nQyqEMmKg.Ebr8frCJX0HjGu', '超级管理员', NULL, NULL, 'normal', NULL, NOW(), NOW()),
('zhangsan', '$2b$10$2ygC4JoFO89uDipKUrAtAuWgK3Zl6nQyqEMmKg.Ebr8frCJX0HjGu', '张三', '13800138011', 'zhangsan@example.com', 'normal', 2, NOW(), NOW()),
('lisi', '$2b$10$2ygC4JoFO89uDipKUrAtAuWgK3Zl6nQyqEMmKg.Ebr8frCJX0HjGu', '李四', '13800138012', 'lisi@example.com', 'normal', 3, NOW(), NOW()),
('wangwu', '$2b$10$2ygC4JoFO89uDipKUrAtAuWgK3Zl6nQyqEMmKg.Ebr8frCJX0HjGu', '王五', '13800138013', 'wangwu@example.com', 'normal', 2, NOW(), NOW());

-- ============================================
-- 3. 角色数据 (sys_role)
-- ============================================
INSERT INTO sys_role (role_code, role_name, description, tenant_id, status, created_at, updated_at) VALUES
('SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', NULL, 'normal', NOW(), NOW()),
('TENANT_ADMIN', '租户管理员', '租户管理员，管理本租户所有业务', 2, 'normal', NOW(), NOW()),
('OPERATOR', '操作员', '日常操作人员', 2, 'normal', NOW(), NOW());

-- ============================================
-- 4. 用户角色关联 (sys_user_role)
-- ============================================
INSERT INTO sys_user_role (user_id, role_id, tenant_id, created_at, updated_at) VALUES
(1, 1, NULL, NOW(), NOW()),
(2, 2, 2, NOW(), NOW()),
(3, 2, 3, NOW(), NOW()),
(4, 3, 2, NOW(), NOW());

-- ============================================
-- 5. 停车场 (parking_lot)
-- ============================================
INSERT INTO parking_lot (lot_name, lot_code, address, total_spaces, occupied_spaces, contact_person, contact_phone, status, tenant_id, created_at, updated_at) VALUES
('星河购物中心地下停车场', 'LOT001', '北京市朝阳区星河购物中心B2-B3层', 300, 156, '张经理', '13800138100', 'normal', 2, NOW(), NOW()),
('市民公园停车场', 'LOT002', '上海市浦东新区市民公园南门', 200, 89, '李经理', '13800138101', 'normal', 3, NOW(), NOW()),
('商业广场停车场', 'LOT003', '广州市天河区商业广场', 500, 312, '王经理', '13800138102', 'normal', 2, NOW(), NOW());

-- ============================================
-- 6. 停车场区域 (parking_area)
-- ============================================
INSERT INTO parking_area (area_name, total_spaces, occupied_spaces, floor, status, tenant_id, created_at, updated_at) VALUES
('A区', 100, 52, 'B2', 'normal', 2, NOW(), NOW()),
('B区', 100, 48, 'B2', 'normal', 2, NOW(), NOW()),
('C区', 100, 56, 'B3', 'normal', 2, NOW(), NOW()),
('D区', 200, 89, '地面', 'normal', 3, NOW(), NOW());

-- ============================================
-- 7. 车位 (parking_space)
-- ============================================
INSERT INTO parking_space (space_number, area_id, space_type, status, tenant_id, created_at, updated_at) VALUES
('A001', 1, 'standard', 'empty', 2, NOW(), NOW()),
('A002', 1, 'standard', 'occupied', 2, NOW(), NOW()),
('A003', 1, 'standard', 'empty', 2, NOW(), NOW()),
('A004', 1, 'large', 'occupied', 2, NOW(), NOW()),
('A005', 1, 'standard', 'empty', 2, NOW(), NOW()),
('B001', 2, 'standard', 'occupied', 2, NOW(), NOW()),
('B002', 2, 'standard', 'empty', 2, NOW(), NOW()),
('B003', 2, 'electric', 'occupied', 2, NOW(), NOW()),
('C001', 3, 'standard', 'occupied', 2, NOW(), NOW()),
('C002', 3, 'standard', 'empty', 2, NOW(), NOW()),
('D001', 4, 'standard', 'empty', 3, NOW(), NOW()),
('D002', 4, 'standard', 'occupied', 3, NOW(), NOW()),
('D003', 4, 'standard', 'empty', 3, NOW(), NOW());

-- ============================================
-- 8. 车主 (owner)
-- ============================================
INSERT INTO owner (name, phone, id_card, address, created_at, updated_at) VALUES
('赵六', '13900139001', '110101199001011234', '北京市朝阳区建国路1号', NOW(), NOW()),
('钱七', '13900139002', '310101199002022345', '上海市浦东新区世纪大道100号', NOW(), NOW()),
('孙八', '13900139003', '440101199003033456', '广州市天河区天河路200号', NOW(), NOW()),
('周九', '13900139004', '510101199004044567', '深圳市南山区科技园路50号', NOW(), NOW()),
('吴十', '13900139005', '320101199005055678', '杭州市西湖区文一路80号', NOW(), NOW());

-- ============================================
-- 9. 车辆 (vehicle)
-- ============================================
INSERT INTO vehicle (plate_number, plate_color, vehicle_brand, vehicle_type, owner_id, vehicle_type_cat, status, tenant_id, created_at, updated_at) VALUES
('京A12345', '蓝色', '宝马BMW', 'sedan', 1, 'monthly', 'normal', 2, NOW(), NOW()),
('沪B67890', '黑色', '奔驰Mercedes', 'sedan', 2, 'vip', 'normal', 2, NOW(), NOW()),
('粤C11111', '白色', '奥迪Audi', 'sedan', 3, 'monthly', 'normal', 2, NOW(), NOW()),
('京D22222', '灰色', '特斯拉Tesla', 'sedan', 4, 'monthly', 'normal', 2, NOW(), NOW()),
('沪E33333', '红色', '丰田Toyota', 'suv', 5, 'temp', 'normal', 3, NOW(), NOW()),
('粤F44444', '黑色', '本田Honda', 'sedan', 1, 'temp', 'normal', 3, NOW(), NOW());

-- ============================================
-- 10. 道闸设备 (barrier)
-- ============================================
INSERT INTO barrier (code, name, location, total_lanes, status, tenant_id, created_at, updated_at) VALUES
('BAR001', '入口道闸A', '星河购物中心入口A', 2, 'online', 2, NOW(), NOW()),
('BAR002', '出口道闸A', '星河购物中心出口A', 2, 'online', 2, NOW(), NOW()),
('BAR003', '入口道闸B', '市民公园入口', 1, 'online', 3, NOW(), NOW()),
('BAR004', '出口道闸B', '市民公园出口', 1, 'online', 3, NOW(), NOW());

-- ============================================
-- 11. 车道 (lane)
-- ============================================
INSERT INTO lane (barrier_id, lane_name, lane_type, status, tenant_id, created_at, updated_at) VALUES
(1, '入口车道1', 'entry', 'normal', 2, NOW(), NOW()),
(1, '入口车道2', 'entry', 'normal', 2, NOW(), NOW()),
(2, '出口车道1', 'exit', 'normal', 2, NOW(), NOW()),
(2, '出口车道2', 'exit', 'normal', 2, NOW(), NOW()),
(3, '入口车道', 'entry', 'normal', 3, NOW(), NOW()),
(4, '出口车道', 'exit', 'normal', 3, NOW(), NOW());

-- ============================================
-- 12. 摄像头 (camera)
-- ============================================
INSERT INTO camera (camera_code, camera_name, lane_id, ip_address, port, username, password, channel, status, created_at, updated_at) VALUES
('CAM001', '入口摄像头A1', 1, '192.168.1.101', 8000, 'admin', 'admin123', 1, 'online', NOW(), NOW()),
('CAM002', '出口摄像头A1', 3, '192.168.1.102', 8000, 'admin', 'admin123', 1, 'online', NOW(), NOW()),
('CAM003', '入口摄像头B', 5, '192.168.1.103', 8000, 'admin', 'admin123', 1, 'online', NOW(), NOW());

-- ============================================
-- 13. 费率规则 (rate_rule)
-- ============================================
INSERT INTO rate_rule (rule_name, rule_type, first_hour_fee, subsequent_fee, daily_max_fee, vehicle_category, priority, status, tenant_id, created_at, updated_at) VALUES
('标准收费', 'default', 5.00, 3.00, 50.00, 'all', 1, 'active', 2, NOW(), NOW()),
('周末收费', 'weekend', 6.00, 4.00, 60.00, 'all', 2, 'active', 2, NOW(), NOW()),
('夜间收费', 'night', 10.00, 2.00, 20.00, 'all', 3, 'active', 2, NOW(), NOW());

-- ============================================
-- 14. 通行记录 (pass_record)
-- ============================================
INSERT INTO pass_record (plate_number, lane_id, pass_type, pass_status, recognition_confidence, fee_calculated, pass_time, tenant_id, created_at, updated_at) VALUES
('京A12345', 1, 'entry', 'normal', 0.98, 0, '2026-04-10 08:30:00', 2, NOW(), NOW()),
('京A12345', 3, 'exit', 'normal', 0.97, 15.00, '2026-04-10 10:15:00', 2, NOW(), NOW()),
('沪B67890', 1, 'entry', 'normal', 0.99, 0, '2026-04-10 09:00:00', 2, NOW(), NOW()),
('沪B67890', 3, 'exit', 'normal', 0.98, 10.00, '2026-04-10 11:30:00', 2, NOW(), NOW()),
('粤C11111', 1, 'entry', 'normal', 0.96, 0, '2026-04-10 09:15:00', 2, NOW(), NOW()),
('粤C11111', 3, 'exit', 'normal', 0.95, 20.00, '2026-04-10 14:00:00', 2, NOW(), NOW()),
('京D22222', 5, 'entry', 'normal', 0.97, 0, '2026-04-10 08:45:00', 3, NOW(), NOW()),
('京D22222', 6, 'exit', 'normal', 0.96, 5.00, '2026-04-10 09:30:00', 3, NOW(), NOW()),
('沪E33333', 5, 'entry', 'normal', 0.94, 0, '2026-04-10 10:00:00', 3, NOW(), NOW()),
('粤F44444', 5, 'entry', 'normal', 0.93, 0, '2026-04-10 11:00:00', 3, NOW(), NOW());

-- ============================================
-- 15. 支付渠道 (payment_channel)
-- ============================================
INSERT INTO payment_channel (channel_code, channel_name, fee_rate, config, status, sort_order, tenant_id, created_at, updated_at) VALUES
('WECHAT', '微信支付', 0.006, '{"appId":"wx1234567890","mchId":"1234567890"}', 'enabled', 1, 2, NOW(), NOW()),
('ALIPAY', '支付宝', 0.005, '{"appId":"2021001123456789"}', 'enabled', 2, 2, NOW(), NOW()),
('WECHAT', '微信支付', 0.006, '{"appId":"wx0987654321","mchId":"0987654321"}', 'enabled', 1, 3, NOW(), NOW()),
('ALIPAY', '支付宝', 0.005, '{"appId":"2021098765432100"}', 'enabled', 2, 3, NOW(), NOW());

-- ============================================
-- 16. 支付记录 (payment)
-- ============================================
INSERT INTO payment (order_no, pass_record_id, plate_number, amount_due, amount_paid, payment_channel_id, payment_status, payment_time, tenant_id, created_at, updated_at) VALUES
('PAY202604100001', 1, '京A12345', 15.00, 15.00, 1, 'paid', '2026-04-10 10:15:30', 2, NOW(), NOW()),
('PAY202604100002', 2, '沪B67890', 10.00, 10.00, 1, 'paid', '2026-04-10 11:30:15', 2, NOW(), NOW()),
('PAY202604100003', 3, '粤C11111', 20.00, 20.00, 2, 'paid', '2026-04-10 14:00:45', 2, NOW(), NOW()),
('PAY202604100004', 4, '京D22222', 5.00, 5.00, 1, 'paid', '2026-04-10 09:30:20', 3, NOW(), NOW());

-- ============================================
-- 17. 月卡会员 (member)
-- ============================================
INSERT INTO member (owner_id, vehicle_id, member_type, start_date, end_date, balance, status, tenant_id, created_at, updated_at) VALUES
(1, 1, 'monthly', '2026-04-01', '2026-05-01', 0, 'active', 2, NOW(), NOW()),
(2, 2, 'vip', '2026-01-01', '2026-12-31', 0, 'active', 2, NOW(), NOW()),
(3, 3, 'monthly', '2026-04-01', '2026-05-01', 0, 'active', 2, NOW(), NOW()),
(4, 4, 'monthly', '2026-03-15', '2026-04-15', 0, 'expired', 2, NOW(), NOW());

-- ============================================
-- 18. 黑名单 (blacklist)
-- ============================================
INSERT INTO blacklist (vehicle_id, reason, created_by, tenant_id, created_at, updated_at) VALUES
(4, '多次逃费', 1, 2, NOW(), NOW());

-- ============================================
-- 19. 岗亭端 (kiosk_client)
-- ============================================
CREATE TABLE IF NOT EXISTS kiosk_client (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    kiosk_code VARCHAR(64) NOT NULL COMMENT '岗亭编码',
    kiosk_name VARCHAR(128) NOT NULL COMMENT '岗亭名称',
    barrier_id BIGINT DEFAULT NULL COMMENT '绑定的道闸ID',
    lane_id BIGINT DEFAULT NULL COMMENT '绑定的车道ID',
    status VARCHAR(16) NOT NULL DEFAULT 'offline' COMMENT '状态: online/offline/disabled',
    last_heartbeat DATETIME DEFAULT NULL COMMENT '最后心跳时间',
    ip_address VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    mac_address VARCHAR(64) DEFAULT NULL COMMENT 'MAC地址',
    remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
    tenant_id BIGINT DEFAULT NULL COMMENT '租户ID',
    created_at DATETIME DEFAULT NULL COMMENT '创建时间',
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_kiosk_code (kiosk_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗亭端表';

INSERT INTO kiosk_client (kiosk_code, kiosk_name, barrier_id, lane_id, status, ip_address, remark, created_at, updated_at) VALUES
('KIOSK001', '入口岗亭A', 1, 1, 'offline', '192.168.1.101', '星河购物中心入口A岗亭', NOW(), NOW()),
('KIOSK002', '出口岗亭A', 2, 3, 'offline', '192.168.1.102', '星河购物中心出口A岗亭', NOW(), NOW()),
('KIOSK003', '市民公园岗亭', 3, 5, 'offline', '192.168.1.103', '市民公园停车场岗亭', NOW(), NOW());

-- ============================================
-- 初始化完成
-- ============================================
