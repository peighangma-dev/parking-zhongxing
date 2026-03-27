-- 停车场管理系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS parking_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parking_db;

-- ----------------------------
-- 1. 道闸设备表
-- ----------------------------
CREATE TABLE IF NOT EXISTS barrier (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    code VARCHAR(32) NOT NULL COMMENT '设备编码',
    name VARCHAR(64) NOT NULL COMMENT '设备名称',
    location VARCHAR(128) DEFAULT NULL COMMENT '安装位置',
    total_lanes INT DEFAULT 1 COMMENT '车道数量',
    status VARCHAR(16) NOT NULL DEFAULT 'offline' COMMENT '状态: online/offline/fault',
    raise_timeout INT DEFAULT 10 COMMENT '抬杆超时秒数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='道闸设备表';

-- ----------------------------
-- 2. 车道表
-- ----------------------------
CREATE TABLE IF NOT EXISTS lane (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    barrier_id BIGINT NOT NULL COMMENT '所属道闸ID',
    lane_name VARCHAR(32) NOT NULL COMMENT '车道名称',
    lane_type VARCHAR(16) NOT NULL COMMENT '类型: entry/exit',
    camera_id VARCHAR(64) DEFAULT NULL COMMENT '相机设备ID',
    barrier_controller_id VARCHAR(64) DEFAULT NULL COMMENT '道闸控制器ID',
    status VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/maintenance/fault',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_barrier_id (barrier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车道表';

-- ----------------------------
-- 3. 车主信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS owner (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(64) NOT NULL COMMENT '车主姓名',
    phone VARCHAR(16) NOT NULL COMMENT '联系电话',
    id_card VARCHAR(32) DEFAULT NULL COMMENT '身份证号',
    address VARCHAR(256) DEFAULT NULL COMMENT '联系地址',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车主信息表';

-- ----------------------------
-- 4. 车辆表
-- ----------------------------
CREATE TABLE IF NOT EXISTS vehicle (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    plate_number VARCHAR(16) NOT NULL COMMENT '车牌号',
    plate_color VARCHAR(16) DEFAULT NULL COMMENT '车牌颜色',
    vehicle_brand VARCHAR(64) DEFAULT NULL COMMENT '车辆品牌',
    vehicle_type VARCHAR(16) DEFAULT 'sedan' COMMENT '车型: sedan/suv/truck',
    owner_id BIGINT DEFAULT NULL COMMENT '车主ID',
    vehicle_type_cat VARCHAR(16) NOT NULL DEFAULT 'temp' COMMENT '分类: monthly/temp/vip/blacklist',
    status VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_plate_number (plate_number),
    KEY idx_owner_id (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆表';

-- ----------------------------
-- 5. 月卡会员表
-- ----------------------------
CREATE TABLE IF NOT EXISTS member (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    owner_id BIGINT NOT NULL COMMENT '车主ID',
    vehicle_id BIGINT NOT NULL COMMENT '车辆ID',
    member_type VARCHAR(16) NOT NULL DEFAULT 'monthly' COMMENT '类型: monthly/seasonal/annual/vip',
    start_date DATE NOT NULL COMMENT '生效日期',
    end_date DATE NOT NULL COMMENT '到期日期',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    status VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '状态: active/expired/cancelled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_owner_id (owner_id),
    KEY idx_vehicle_id (vehicle_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月卡会员表';

-- ----------------------------
-- 6. 通行记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS pass_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    plate_number VARCHAR(16) NOT NULL COMMENT '车牌号',
    lane_id BIGINT NOT NULL COMMENT '车道ID',
    pass_type VARCHAR(16) NOT NULL COMMENT '类型: entry/exit',
    pass_status VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/abnormal',
    recognition_confidence DECIMAL(5,4) DEFAULT NULL COMMENT '识别置信度',
    image_url VARCHAR(256) DEFAULT NULL COMMENT '抓拍图片URL',
    fee_calculated DECIMAL(10,2) DEFAULT NULL COMMENT '计算费用',
    pass_time DATETIME NOT NULL COMMENT '通行时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_plate_number (plate_number),
    KEY idx_lane_id (lane_id),
    KEY idx_pass_time (pass_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通行记录表';

-- ----------------------------
-- 7. 支付渠道表
-- ----------------------------
CREATE TABLE IF NOT EXISTS payment_channel (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    channel_code VARCHAR(32) NOT NULL COMMENT '渠道编码',
    channel_name VARCHAR(64) NOT NULL COMMENT '渠道名称',
    fee_rate DECIMAL(5,4) DEFAULT 0 COMMENT '手续费率',
    status VARCHAR(16) NOT NULL DEFAULT 'enabled' COMMENT '状态: enabled/disabled',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_channel_code (channel_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付渠道表';

-- ----------------------------
-- 8. 支付记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS payment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    pass_record_id BIGINT DEFAULT NULL COMMENT '关联通行记录ID',
    plate_number VARCHAR(16) NOT NULL COMMENT '车牌号',
    amount_due DECIMAL(10,2) NOT NULL COMMENT '应付金额',
    amount_paid DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    payment_channel_id BIGINT DEFAULT NULL COMMENT '支付渠道ID',
    payment_status VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/paid/refunded/reversed',
    payment_time DATETIME DEFAULT NULL COMMENT '支付时间',
    transaction_id VARCHAR(64) DEFAULT NULL COMMENT '第三方交易号',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_plate_number (plate_number),
    KEY idx_payment_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- ----------------------------
-- 9. 费率规则表
-- ----------------------------
CREATE TABLE IF NOT EXISTS rate_rule (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    rule_name VARCHAR(64) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(16) NOT NULL DEFAULT 'default' COMMENT '类型: default/weekend/holiday/night',
    first_hour_fee DECIMAL(10,2) NOT NULL COMMENT '首小时费用',
    subsequent_fee DECIMAL(10,2) NOT NULL COMMENT '续费费用',
    daily_max_fee DECIMAL(10,2) DEFAULT NULL COMMENT '24小时封顶',
    vehicle_category VARCHAR(16) DEFAULT 'all' COMMENT '适用车型: all/small/large',
    priority INT DEFAULT 0 COMMENT '优先级',
    status VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '状态: active/inactive',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='费率规则表';

-- ----------------------------
-- 10. 黑名单表
-- ----------------------------
CREATE TABLE IF NOT EXISTS blacklist (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    vehicle_id BIGINT NOT NULL COMMENT '车辆ID',
    reason VARCHAR(256) DEFAULT NULL COMMENT '加入原因',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_vehicle_id (vehicle_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑名单表';

-- ----------------------------
-- 11. 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(32) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码',
    nickname VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    phone VARCHAR(16) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    avatar VARCHAR(256) DEFAULT NULL COMMENT '头像URL',
    status VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ----------------------------
-- 12. 角色表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_code VARCHAR(32) NOT NULL COMMENT '角色编码',
    role_name VARCHAR(64) NOT NULL COMMENT '角色名称',
    description VARCHAR(256) DEFAULT NULL COMMENT '描述',
    status VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/disabled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- 13. 菜单权限表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(64) NOT NULL COMMENT '菜单名称',
    menu_type VARCHAR(16) NOT NULL COMMENT '类型: directory/menu/button',
    path VARCHAR(128) DEFAULT NULL COMMENT '路由路径',
    component VARCHAR(256) DEFAULT NULL COMMENT '组件路径',
    icon VARCHAR(64) DEFAULT NULL COMMENT '图标',
    permission VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- ----------------------------
-- 14. 用户角色关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ----------------------------
-- 15. 角色菜单关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_menu (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- ----------------------------
-- 16. 停车场区域表
-- ----------------------------
CREATE TABLE IF NOT EXISTS parking_area (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    area_name VARCHAR(64) NOT NULL COMMENT '区域名称',
    total_spaces INT NOT NULL COMMENT '总车位数量',
    occupied_spaces INT DEFAULT 0 COMMENT '已占用数量',
    floor VARCHAR(32) DEFAULT NULL COMMENT '楼层',
    status VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '状态: normal/maintenance/closed',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='停车场区域表';

-- ----------------------------
-- 17. 车位表
-- ----------------------------
CREATE TABLE IF NOT EXISTS parking_space (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    space_number VARCHAR(32) NOT NULL COMMENT '车位编号',
    area_id BIGINT NOT NULL COMMENT '所属区域ID',
    space_type VARCHAR(16) NOT NULL DEFAULT 'standard' COMMENT '类型: standard/large/disabled/electric',
    status VARCHAR(16) NOT NULL DEFAULT 'empty' COMMENT '状态: empty/occupied/reserved',
    vehicle_plate VARCHAR(16) DEFAULT NULL COMMENT '当前车牌号',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_space_number (space_number),
    KEY idx_area_id (area_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车位表';

-- ----------------------------
-- 18. 文件信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS file_info (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    file_name VARCHAR(256) NOT NULL COMMENT '文件名',
    file_path VARCHAR(512) NOT NULL COMMENT '文件路径',
    file_url VARCHAR(512) DEFAULT NULL COMMENT '访问URL',
    file_size BIGINT DEFAULT NULL COMMENT '文件大小(字节)',
    file_type VARCHAR(64) DEFAULT NULL COMMENT '文件类型/MIME',
    storage_type VARCHAR(16) NOT NULL COMMENT '存储类型: local/aliyun/qiniu/tencent',
    bucket_name VARCHAR(128) DEFAULT NULL COMMENT '存储桶名称',
    created_by BIGINT DEFAULT NULL COMMENT '上传人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

-- ----------------------------
-- 19. 存储配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS storage_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    config_key VARCHAR(64) NOT NULL COMMENT '配置键',
    provider VARCHAR(16) NOT NULL COMMENT '提供商: aliyun/qiniu/tencent',
    access_key VARCHAR(256) DEFAULT NULL COMMENT '访问密钥',
    secret_key VARCHAR(256) DEFAULT NULL COMMENT '私有密钥',
    bucket_name VARCHAR(128) DEFAULT NULL COMMENT '存储桶名称',
    endpoint VARCHAR(256) DEFAULT NULL COMMENT '接入点',
    domain VARCHAR(256) DEFAULT NULL COMMENT '自定义域名',
    status VARCHAR(16) NOT NULL DEFAULT 'disabled' COMMENT '状态: enabled/disabled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='存储配置表';

-- ----------------------------
-- 20. 发票抬头表
-- ----------------------------
CREATE TABLE IF NOT EXISTS invoice_title (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title_type VARCHAR(16) NOT NULL DEFAULT 'personal' COMMENT '类型: personal/enterprise',
    company_name VARCHAR(128) DEFAULT NULL COMMENT '公司名称',
    tax_number VARCHAR(64) DEFAULT NULL COMMENT '税号',
    bank_name VARCHAR(128) DEFAULT NULL COMMENT '开户银行',
    bank_account VARCHAR(64) DEFAULT NULL COMMENT '银行账号',
    address VARCHAR(256) DEFAULT NULL COMMENT '地址',
    phone VARCHAR(32) DEFAULT NULL COMMENT '电话',
    email VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认: 0否/1是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发票抬头表';

-- ----------------------------
-- 21. 发票表
-- ----------------------------
CREATE TABLE IF NOT EXISTS invoice (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    invoice_no VARCHAR(32) NOT NULL COMMENT '发票号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title_id BIGINT NOT NULL COMMENT '发票抬头ID',
    plate_number VARCHAR(16) DEFAULT NULL COMMENT '车牌号',
    amount DECIMAL(10,2) NOT NULL COMMENT '发票金额',
    tax_amount DECIMAL(10,2) DEFAULT NULL COMMENT '税额',
    status VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/approved/issued/rejected',
    invoice_type VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '类型: normal/special',
    billing_time DATETIME DEFAULT NULL COMMENT '开票时间',
    sending_type VARCHAR(16) DEFAULT 'email' COMMENT '发送方式: email/快递',
    invoice_url VARCHAR(512) DEFAULT NULL COMMENT '发票PDF URL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_invoice_no (invoice_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发票表';

-- ----------------------------
-- 22. 摄像头表
-- ----------------------------
CREATE TABLE IF NOT EXISTS camera (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    camera_code VARCHAR(64) NOT NULL COMMENT '摄像头编码',
    camera_name VARCHAR(128) NOT NULL COMMENT '摄像头名称',
    lane_id BIGINT DEFAULT NULL COMMENT '关联车道ID',
    ip_address VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    port INT DEFAULT NULL COMMENT '端口',
    username VARCHAR(64) DEFAULT NULL COMMENT '用户名',
    password VARCHAR(128) DEFAULT NULL COMMENT '密码',
    channel INT DEFAULT 1 COMMENT '通道号',
    stream_url VARCHAR(512) DEFAULT NULL COMMENT '流地址',
    status VARCHAR(16) NOT NULL DEFAULT 'offline' COMMENT '状态: online/offline/fault',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_camera_code (camera_code),
    KEY idx_lane_id (lane_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄像头表';

-- ----------------------------
-- 23. 视频录像表
-- ----------------------------
CREATE TABLE IF NOT EXISTS video_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    camera_id BIGINT NOT NULL COMMENT '摄像头ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME DEFAULT NULL COMMENT '结束时间',
    file_path VARCHAR(512) DEFAULT NULL COMMENT '文件路径',
    file_size BIGINT DEFAULT NULL COMMENT '文件大小',
    record_type VARCHAR(16) NOT NULL DEFAULT 'continuous' COMMENT '类型: continuous/motion/alarm',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_camera_id (camera_id),
    KEY idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频录像表';

-- ----------------------------
-- 24. 报警记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS alarm (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    alarm_type VARCHAR(32) NOT NULL COMMENT '报警类型',
    camera_id BIGINT DEFAULT NULL COMMENT '摄像头ID',
    lane_id BIGINT DEFAULT NULL COMMENT '车道ID',
    alarm_time DATETIME NOT NULL COMMENT '报警时间',
    alarm_level VARCHAR(16) NOT NULL DEFAULT 'medium' COMMENT '级别: low/medium/high',
    description VARCHAR(512) DEFAULT NULL COMMENT '描述',
    image_url VARCHAR(512) DEFAULT NULL COMMENT '抓拍图片',
    video_url VARCHAR(512) DEFAULT NULL COMMENT '关联视频',
    status VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/confirmed/resolved',
    confirmed_by BIGINT DEFAULT NULL COMMENT '确认人',
    confirmed_time DATETIME DEFAULT NULL COMMENT '确认时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_camera_id (camera_id),
    KEY idx_lane_id (lane_id),
    KEY idx_alarm_time (alarm_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报警记录表';

-- ----------------------------
-- 25. 操作日志表
-- ----------------------------
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT DEFAULT NULL COMMENT '操作人ID',
    username VARCHAR(64) DEFAULT NULL COMMENT '操作人用户名',
    operation VARCHAR(64) DEFAULT NULL COMMENT '操作类型',
    module VARCHAR(64) DEFAULT NULL COMMENT '操作模块',
    method VARCHAR(128) DEFAULT NULL COMMENT '方法名',
    request_url VARCHAR(256) DEFAULT NULL COMMENT '请求URL',
    request_method VARCHAR(16) DEFAULT NULL COMMENT '请求方法',
    request_params TEXT DEFAULT NULL COMMENT '请求参数',
    response_result TEXT DEFAULT NULL COMMENT '响应结果',
    ip_address VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    user_agent TEXT DEFAULT NULL COMMENT '用户代理',
    execution_time BIGINT DEFAULT NULL COMMENT '执行时长(毫秒)',
    status VARCHAR(16) DEFAULT NULL COMMENT '状态: success/failed',
    error_message TEXT DEFAULT NULL COMMENT '错误信息',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------
-- 初始数据
-- ----------------------------
-- 插入默认支付渠道
INSERT INTO payment_channel (channel_code, channel_name, fee_rate, status, sort_order) VALUES
('WECHAT', '微信支付', 0.0025, 'enabled', 1),
('ALIPAY', '支付宝', 0.0025, 'enabled', 2),
('CASH', '现金支付', 0.0000, 'enabled', 3),
('ETC', 'ETC电子支付', 0.0010, 'enabled', 4),
('MEMBER', '会员卡支付', 0.0000, 'enabled', 5);

-- 插入默认费率规则
INSERT INTO rate_rule (rule_name, rule_type, first_hour_fee, subsequent_fee, daily_max_fee, vehicle_category, priority, status) VALUES
('默认白天费率', 'default', 5.00, 3.00, 50.00, 'all', 0, 'active'),
('周末费率', 'weekend', 6.00, 4.00, 60.00, 'all', 1, 'active'),
('夜间费率', 'night', 3.00, 2.00, 20.00, 'all', 2, 'active');

-- 插入超级管理员用户 (密码: admin123)
INSERT INTO sys_user (username, password, nickname, phone, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt7uC8u', '超级管理员', '13800138000', 'normal');

-- 插入默认角色
INSERT INTO sys_role (role_code, role_name, description, status) VALUES
('SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 'normal'),
('ADMIN', '管理员', '系统管理员，拥有管理权限', 'normal'),
('OPERATOR', '操作员', '普通操作员', 'normal');

-- 插入默认菜单
INSERT INTO sys_menu (parent_id, menu_name, menu_type, path, component, icon, sort_order) VALUES
(0, '系统管理', 'directory', '/system', NULL, 'Setting', 1),
(1, '用户管理', 'menu', '/system/user', 'system/user/index', 'User', 1),
(1, '角色管理', 'menu', '/system/role', 'system/role/index', 'Role', 2),
(1, '菜单管理', 'menu', '/system/menu', 'system/menu/index', 'Menu', 3),
(0, '道闸管理', 'directory', '/barrier', NULL, 'Monitor', 2),
(5, '设备管理', 'menu', '/barrier/device', 'barrier/device/index', 'Connection', 1),
(5, '车道管理', 'menu', '/barrier/lane', 'barrier/lane/index', 'Guide', 2),
(5, '实时监控', 'menu', '/barrier/monitor', 'barrier/monitor/index', 'VideoCamera', 3),
(0, '支付管理', 'directory', '/payment', NULL, 'Money', 3),
(9, '支付渠道', 'menu', '/payment/channel', 'payment/channel/index', 'Coin', 1),
(9, '费率规则', 'menu', '/payment/rate', 'payment/rate/index', 'PriceTag', 2),
(9, '交易记录', 'menu', '/payment/record', 'payment/record/index', 'List', 3),
(0, '车辆管理', 'directory', '/vehicle', NULL, 'Car', 4),
(13, '车辆列表', 'menu', '/vehicle/list', 'vehicle/list/index', 'Van', 1),
(13, '月卡管理', 'menu', '/vehicle/member', 'vehicle/member/index', 'Wallet', 2),
(13, '黑名单', 'menu', '/vehicle/blacklist', 'vehicle/blacklist/index', 'Lock', 3),
(0, '车位管理', 'directory', '/space', NULL, 'House', 5),
(17, '区域管理', 'menu', '/space/area', 'space/area/index', 'Grid', 1),
(17, '车位管理', 'menu', '/space/space', 'space/space/index', 'Box', 2),
(17, '监控大屏', 'menu', '/space/monitor', 'space/monitor/index', 'Monitor', 3),
(0, '文件管理', 'directory', '/oss', NULL, 'Folder', 6),
(21, '存储配置', 'menu', '/oss/config', 'oss/config/index', 'Cloud', 1),
(21, '文件列表', 'menu', '/oss/file', 'oss/file/index', 'Document', 2),
(0, '发票管理', 'directory', '/invoice', NULL, 'DocumentChecked', 7),
(25, '发票抬头', 'menu', '/invoice/title', 'invoice/title/index', 'Tickets', 1),
(25, '发票申请', 'menu', '/invoice/apply', 'invoice/apply/index', 'Form', 2),
(25, '发票管理', 'menu', '/invoice/list', 'invoice/list/index', 'List', 3),
(0, '视频监控', 'directory', '/monitor', NULL, 'Video', 8),
(29, '摄像头管理', 'menu', '/monitor/camera', 'monitor/camera/index', 'VideoCamera', 1),
(29, '实时视频', 'menu', '/monitor/live', 'monitor/live/index', 'VideoPlay', 2),
(29, '录像回放', 'menu', '/monitor/playback', 'monitor/playback/index', 'Refresh', 3),
(29, '报警记录', 'menu', '/monitor/alarm', 'monitor/alarm/index', 'Bell', 4),
(0, '报表统计', 'directory', '/report', NULL, 'DataAnalysis', 9),
(33, '通行记录', 'menu', '/report/pass', 'report/pass/index', 'Keys', 1),
(33, '收费日报', 'menu', '/report/daily', 'report/daily/index', 'Calendar', 2),
(33, '收费月报', 'menu', '/report/monthly', 'report/monthly/index', 'Histogram', 3),
(33, '财务报表', 'menu', '/report/finance', 'report/finance/index', 'Money', 4),
(33, '经营分析', 'menu', '/report/business', 'report/business/index', 'DataLine', 5);
