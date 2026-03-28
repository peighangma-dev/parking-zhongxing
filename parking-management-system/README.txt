===============================================================================
                    停车场管理系统 - 项目详细文档
===============================================================================

一、项目概述
===============================================================================
本系统是一套功能完善的停车场管理系统，采用前后端分离架构，支持多租户管理。
系统基于 Spring Cloud 微服务架构，前端使用 Vue 3 + TypeScript + Element Plus 构建。


二、技术栈
===============================================================================

【后端技术栈】
------------------------------------------------------------------------------
1. 基础框架
   - Spring Boot 2.7.18
   - Spring Cloud 2021.0.8
   - Spring Cloud Gateway (网关)
   - MyBatis-Plus 3.5.3 (ORM框架)

2. 安全认证
   - JWT (jjwt 0.11.5) - Token认证
   - Spring Security - 权限管理
   - BCrypt - 密码加密

3. 数据存储
   - MySQL 8.0 / MariaDB (主数据库)
   - Redis (缓存和会话)

4. 外部集成
   - 微信支付 SDK
   - 支付宝 SDK

【前端技术栈】
------------------------------------------------------------------------------
   - Vue 3.4.x (组合式API)
   - TypeScript 5.x
   - Vite 5.x (构建工具)
   - Element Plus 2.5.x (UI组件库)
   - Pinia (状态管理)
   - Vue Router 4.x


三、项目模块结构
===============================================================================

【微服务模块】
------------------------------------------------------------------------------
parking-gateway/          - API网关服务 (端口: 8080)
parking-uc/               - 用户认证服务 (端口: 8085)
parking-barrier/          - 道闸设备服务 (端口: 8081)
parking-vehicle/          - 车辆管理服务 (端口: 8083)
parking-space/            - 车位管理服务 (端口: 8086)
parking-payment/          - 支付服务 (端口: 8082)
parking-tenant/          - 租户管理服务 (端口: 8090)
parking-report/           - 报表服务 (端口: 8084)
parking-oss/              - 文件存储服务 (端口: 8087)
parking-invoice/          - 发票服务 (端口: 8088)
parking-monitor/          - 监控服务 (端口: 8089)

【公共模块】
------------------------------------------------------------------------------
parking-common/
   parking-common-core/   - 核心公共代码
   parking-common-database/ - 数据库公共代码

【前端模块】
------------------------------------------------------------------------------
parking-web/             - Vue 3 前端应用


四、数据库表结构
===============================================================================

【核心业务表】
------------------------------------------------------------------------------
1. tenant                - 租户表
   - id                 - 主键ID
   - tenant_code        - 租户编码 (唯一)
   - tenant_name        - 租户名称
   - contact_name       - 联系人
   - contact_phone      - 联系电话
   - contact_email      - 联系邮箱
   - package_id         - 套餐ID
   - status             - 状态 (active/disabled)
   - expire_time        - 到期时间
   - max_users          - 最大用户数
   - max_spaces         - 最大车位数
   - created_at         - 创建时间

2. package_info          - 套餐表
   - id                 - 主键ID
   - package_code       - 套餐编码
   - package_name       - 套餐名称
   - package_type       - 套餐类型
   - price              - 价格
   - duration_days      - 时长(天)
   - features           - 功能特性 (JSON格式)
   - status             - 状态

3. barrier              - 道闸设备表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - code               - 设备编码 (唯一)
   - name               - 设备名称
   - location           - 安装位置
   - total_lanes        - 车道数量
   - status             - 状态 (online/offline/fault)
   - raise_timeout      - 抬杆超时秒数

4. lane                - 车道表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - barrier_id         - 所属道闸ID
   - lane_name          - 车道名称
   - lane_type          - 类型 (entry/exit)
   - camera_id          - 相机设备ID
   - barrier_controller_id - 道闸控制器ID
   - status             - 状态

5. vehicle             - 车辆表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - plate_number       - 车牌号 (唯一)
   - plate_color        - 车牌颜色
   - vehicle_brand      - 车辆品牌
   - vehicle_type       - 车型 (sedan/suv/truck)
   - owner_id           - 车主ID
   - vehicle_type_cat   - 分类 (monthly/temp/vip/blacklist)
   - status             - 状态 (normal/disabled)

6. member              - 月卡会员表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - owner_id           - 车主ID
   - vehicle_id         - 车辆ID
   - member_type        - 类型 (monthly/seasonal/annual/vip)
   - start_date         - 生效日期
   - end_date           - 到期日期
   - balance            - 账户余额
   - status             - 状态 (active/expired/cancelled)

7. pass_record         - 通行记录表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - plate_number       - 车牌号
   - lane_id            - 车道ID
   - pass_type          - 类型 (entry/exit)
   - pass_status        - 状态 (normal/abnormal)
   - recognition_confidence - 识别置信度
   - image_url          - 抓拍图片URL
   - fee_calculated     - 计算费用
   - pass_time          - 通行时间

8. payment_channel     - 支付渠道表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - channel_code       - 渠道编码
   - channel_name       - 渠道名称
   - fee_rate           - 手续费率
   - status             - 状态 (enabled/disabled)

9. payment             - 支付记录表
   - id                 - 主键ID
   - tenant_id          - 租户ID
   - order_no           - 订单号 (唯一)
   - pass_record_id     - 关联通行记录ID
   - plate_number       - 车牌号
   - amount_due         - 应付金额
   - amount_paid        - 实付金额
   - payment_channel_id - 支付渠道ID
   - payment_status     - 状态 (pending/paid/refunded/reversed)
   - payment_time       - 支付时间
   - transaction_id     - 第三方交易号

10. rate_rule          - 费率规则表
    - id                 - 主键ID
    - tenant_id          - 租户ID
    - rule_name          - 规则名称
    - rule_type          - 类型 (default/weekend/holiday/night)
    - first_hour_fee     - 首小时费用
    - subsequent_fee     - 续费费用
    - daily_max_fee      - 24小时封顶
    - vehicle_category   - 适用车型
    - priority           - 优先级
    - status             - 状态

11. blacklist          - 黑名单表
    - id                 - 主键ID
    - tenant_id          - 租户ID
    - vehicle_id         - 车辆ID
    - reason             - 加入原因
    - operator_id        - 操作人ID
    - created_at         - 创建时间
    - updated_at         - 更新时间

【系统表】
------------------------------------------------------------------------------
12. sys_user           - 系统用户表
13. sys_role           - 角色表
14. sys_menu           - 菜单表
15. sys_user_role      - 用户角色关联表
16. sys_role_menu      - 角色菜单关联表


五、功能模块
===============================================================================

【租户管理】
------------------------------------------------------------------------------
- 租户增删改查
- 租户状态管理 (启用/禁用)
- 套餐分配
- 资源配额管理 (用户数/车位数限制)
- 租户注册申请

【设备管理】
------------------------------------------------------------------------------
- 道闸设备管理
  - 设备添加/编辑/删除
  - 设备状态监控 (在线/离线/故障)
  - 设备参数配置 (抬杆超时等)
- 车道管理
  - 车道配置
  - 相机关联
  - 状态管理

【车辆管理】
------------------------------------------------------------------------------
- 车辆列表
  - 车牌号管理
  - 车辆颜色/品牌/车型
  - 车辆分类 (月卡/临时/VIP/黑名单)
  - 状态管理
- 月卡管理
  - 会员开通/续费
  - 账户余额管理
  - 会员类型 (月卡/季卡/年卡/VIP)
- 黑名单管理
  - 黑名单添加/移除
  - 原因记录

【支付管理】
------------------------------------------------------------------------------
- 支付渠道配置
  - 微信支付
  - 支付宝支付
- 费率规则配置
  - 首小时费用
  - 续费费用
  - 24小时封顶
  - 分类费率 (工作日/周末/节假日/夜间)
- 交易记录
  - 订单查询
  - 支付状态 (待支付/已支付/已取消/已退款)
  - 退款/冲正操作

【报表统计】
------------------------------------------------------------------------------
- 通行记录查询
- 收费日报表
- 月度报表

【系统管理】
------------------------------------------------------------------------------
- 用户管理
  - 用户增删改查
  - 密码修改
  - 角色分配
- 角色管理
  - 角色权限配置
  - 菜单权限分配
- 菜单管理
  - 树形菜单结构
  - 支持目录/菜单/按钮三种类型
  - 菜单CRUD操作
- 操作日志
  - 用户操作记录追踪
  - 按模块/操作类型/时间筛选
  - 批量删除/导出功能
- 登录日志
  - 登录历史记录
  - 显示IP/浏览器/操作系统/登录地点
  - 批量删除/清空30天前日志
- 系统配置
  - 基本设置 (系统名称/Logo/描述)
  - 安全设置 (密码策略/登录锁定/Token配置)
  - 业务设置 (费率/免费停车时长/月卡规则)
  - 通知设置 (邮件/SMS通知配置)
  - 文件存储 (本地/OSS/COS/MinIO配置)

【超级管理员功能】
------------------------------------------------------------------------------
- 可查看所有租户的数据
- 顶部导航栏租户选择器
- 支持按租户筛选数据


六、API接口列表
===============================================================================

【UC服务 - 用户认证】  /api/uc/
------------------------------------------------------------------------------
POST   /api/uc/v1/login                  - 用户登录
GET    /api/uc/v1/current                - 获取当前用户信息
GET    /api/uc/v1/users/page             - 用户分页列表
POST   /api/uc/v1/users                  - 创建用户
PUT    /api/uc/v1/users/{id}             - 更新用户
DELETE /api/uc/v1/users/{id}              - 删除用户
PUT    /api/uc/v1/users/{id}/password   - 修改密码
GET    /api/uc/v1/users/{id}/roles      - 获取用户角色
PUT    /api/uc/v1/users/{id}/roles       - 分配角色

【UC服务 - 角色管理】  /api/uc/
------------------------------------------------------------------------------
GET    /api/uc/v1/roles/page             - 角色分页列表
GET    /api/uc/v1/roles/all              - 所有角色列表
POST   /api/uc/v1/roles                   - 创建角色
PUT    /api/uc/v1/roles/{id}              - 更新角色
DELETE /api/uc/v1/roles/{id}              - 删除角色

【UC服务 - 菜单管理】  /api/uc/
------------------------------------------------------------------------------
GET    /api/uc/v1/menus/tree             - 菜单树形结构
GET    /api/uc/v1/menus/by-role/{roleId} - 获取角色菜单权限
POST   /api/uc/v1/menus                  - 创建菜单
PUT    /api/uc/v1/menus/{id}              - 更新菜单
DELETE /api/uc/v1/menus/{id}              - 删除菜单
PUT    /api/uc/v1/menus/role/{roleId}/menus - 分配角色菜单

【UC服务 - 日志管理】  /api/uc/
------------------------------------------------------------------------------
GET    /api/uc/v1/logs/operation/page    - 操作日志分页
DELETE /api/uc/v1/logs/operation/{id}    - 删除操作日志
POST   /api/uc/v1/logs/operation/batch-delete - 批量删除操作日志
GET    /api/uc/v1/logs/login/page       - 登录日志分页
DELETE /api/uc/v1/logs/login/{id}        - 删除登录日志
POST   /api/uc/v1/logs/login/batch-delete - 批量删除登录日志
DELETE /api/uc/v1/logs/login/clear      - 清空30天前登录日志

【UC服务 - 系统配置】  /api/uc/
------------------------------------------------------------------------------
GET    /api/uc/v1/config                 - 获取系统配置
PUT    /api/uc/v1/config                 - 保存系统配置

【Tenant服务 - 租户管理】  /api/tenant/
------------------------------------------------------------------------------
GET    /api/tenant/v1/page               - 租户分页列表
GET    /api/tenant/v1/list               - 租户列表
GET    /api/tenant/v1/{id}               - 获取租户详情
POST   /api/tenant/v1                    - 创建租户
PUT    /api/tenant/v1/{id}               - 更新租户
DELETE /api/tenant/v1/{id}              - 删除租户
PUT    /api/tenant/v1/status/{id}        - 更新租户状态

【Package服务 - 套餐管理】  /api/package/
------------------------------------------------------------------------------
GET    /api/package/v1/page              - 套餐分页列表
GET    /api/package/v1/list              - 套餐列表
GET    /api/package/v1/{id}              - 获取套餐详情
POST   /api/package/v1                   - 创建套餐
PUT    /api/package/v1/{id}              - 更新套餐
DELETE /api/package/v1/{id}              - 删除套餐

【Barrier服务 - 道闸设备】  /api/barrier/
------------------------------------------------------------------------------
GET    /api/barrier/v1/devices/page     - 设备分页列表
GET    /api/barrier/v1/devices/{id}     - 获取设备详情
GET    /api/barrier/v1/devices/code/{code} - 根据编码获取设备
POST   /api/barrier/v1/devices          - 创建设备
PUT    /api/barrier/v1/devices/{id}     - 更新设备
DELETE /api/barrier/v1/devices/{id}     - 删除设备
POST   /api/barrier/v1/devices/{id}/status - 更新设备状态

【Lane服务 - 车道管理】  /api/barrier/
------------------------------------------------------------------------------
GET    /api/barrier/v1/lanes/page       - 车道分页列表
GET    /api/barrier/v1/lanes/{id}       - 获取车道详情
POST   /api/barrier/v1/lanes            - 创建车道
PUT    /api/barrier/v1/lanes/{id}       - 更新车道
DELETE /api/barrier/v1/lanes/{id}       - 删除车道

【Vehicle服务 - 车辆管理】  /api/vehicle/
------------------------------------------------------------------------------
GET    /api/vehicle/v1/vehicles/page    - 车辆分页列表
GET    /api/vehicle/v1/vehicles/{id}    - 获取车辆详情
GET    /api/vehicle/v1/vehicles/plate/{plateNumber} - 根据车牌获取
POST   /api/vehicle/v1/vehicles         - 添加车辆
PUT    /api/vehicle/v1/vehicles/{id}    - 更新车辆
DELETE /api/vehicle/v1/vehicles/{id}    - 删除车辆
PUT    /api/vehicle/v1/vehicles/{id}/enable  - 启用车辆
PUT    /api/vehicle/v1/vehicles/{id}/disable - 禁用车辆

【Blacklist服务 - 黑名单】  /api/vehicle/
------------------------------------------------------------------------------
GET    /api/vehicle/v1/blacklist/page   - 黑名单分页列表
POST   /api/vehicle/v1/blacklist         - 添加到黑名单
DELETE /api/vehicle/v1/blacklist/{id}   - 移除黑名单

【Payment服务 - 支付管理】  /api/payment/
------------------------------------------------------------------------------
GET    /api/payment/v1/page             - 交易记录分页
GET    /api/payment/v1/order/{orderNo}  - 根据订单号查询
POST   /api/payment/v1/create           - 创建支付订单
POST   /api/payment/v1/callback/{channel} - 支付回调
POST   /api/payment/v1/cancel/{orderNo} - 取消订单
POST   /api/payment/v1/reverse/{orderNo} - 冲正订单
POST   /api/payment/v1/refund/{orderNo} - 退款

【RateRule服务 - 费率规则】  /api/payment/
------------------------------------------------------------------------------
GET    /api/payment/v1/rates/page       - 费率规则分页
POST   /api/payment/v1/rates            - 创建规则
PUT    /api/payment/v1/rates/{id}       - 更新规则
DELETE /api/payment/v1/rates/{id}       - 删除规则

【PaymentChannel服务 - 支付渠道】  /api/payment/
------------------------------------------------------------------------------
GET    /api/payment/v1/channels         - 渠道列表
POST   /api/payment/v1/channels         - 创建渠道
PUT    /api/payment/v1/channels/{id}    - 更新渠道
DELETE /api/payment/v1/channels/{id}    - 删除渠道


七、安全特性
===============================================================================

【认证安全】
------------------------------------------------------------------------------
- JWT Token 认证 (使用 jjwt 库实现)
- Token 包含用户ID和用户名
- Token 有效期可配置
- 支持 Token 刷新

【密码安全】
------------------------------------------------------------------------------
- BCrypt 密码加密
- 密码强度校验 (至少8位，包含字母和数字)
- 登录失败次数限制 (可配置)

【接口安全】
------------------------------------------------------------------------------
- 请求头验证 (X-Tenant-Id, X-Super-Admin)
- 超级管理员权限校验
- 租户数据隔离

【支付安全】
------------------------------------------------------------------------------
- 支付回调签名校验
- 订单状态机控制 (pending -> paid -> refunded/reversed)
- 退款金额校验

【跨域安全】
------------------------------------------------------------------------------
- CORS 配置限制来源
- 支持配置允许的域名列表


八、部署指南
===============================================================================

【环境要求】
------------------------------------------------------------------------------
- JDK 17+
- Node.js 18+
- MySQL 8.0+ / MariaDB 10.5+
- Redis 6.0+
- Nginx 1.20+

【数据库初始化】
------------------------------------------------------------------------------
1. 创建数据库:
   CREATE DATABASE parking_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

2. 执行初始化脚本:
   mysql -u root -p parking_db < deploy/mysql/init/01-init.sql

【后端服务启动】
------------------------------------------------------------------------------
1. 编译项目:
   cd parking-management-system
   mvn clean package -DskipTests

2. 启动各个微服务:
   java -jar parking-gateway/target/parking-gateway-1.0.0.jar &
   java -jar parking-uc/target/parking-uc-1.0.0.jar --server.port=8085 &
   java -jar parking-barrier/target/parking-barrier-1.0.0.jar --server.port=8081 &
   java -jar parking-vehicle/target/parking-vehicle-1.0.0.jar --server.port=8083 &
   java -jar parking-space/target/parking-space-1.0.0.jar --server.port=8086 &
   java -jar parking-payment/target/parking-payment-1.0.0.jar --server.port=8082 &
   java -jar parking-tenant/target/parking-tenant-1.0.0.jar --server.port=8090 &

【前端部署】
------------------------------------------------------------------------------
1. 安装依赖:
   cd parking-web
   npm install

2. 开发模式:
   npm run dev

3. 生产构建:
   npm run build

4. 部署dist目录到nginx

【Nginx配置】
------------------------------------------------------------------------------
配置文件位于: deploy/nginx.conf

主要配置:
- 前端静态文件服务
- API反向代理到各个微服务
- 静态资源缓存策略

启动nginx:
   nginx -c /workspace/parking-management-system/deploy/nginx.conf


九、默认账号
===============================================================================

【系统管理员】
------------------------------------------------------------------------------
用户名: admin
密码: admin123
角色: 超级管理员 (SUPER_ADMIN)

【测试租户】
------------------------------------------------------------------------------
租户编码: tenant001
租户名称: 测试租户


十、多租户设计
===============================================================================

【租户隔离机制】
------------------------------------------------------------------------------
1. 数据隔离
   - 每个租户数据通过 tenant_id 字段区分
   - 所有业务表都包含 tenant_id 字段
   - 查询时自动带上租户过滤条件

2. 租户上下文
   - TenantContext 通过 ThreadLocal 存储当前租户信息
   - Gateway 层解析请求头设置租户上下文
   - 服务层自动使用租户上下文进行数据过滤

3. 超级管理员
   - 拥有特殊标记 X-Super-Admin: true
   - 可以查看所有租户的数据
   - 前端提供租户选择器进行筛选


十一、版本信息
===============================================================================
当前版本: v2.0.26
构建时间: 2026-03-28
作者: Parking Development Team


===============================================================================
                              文档结束
===============================================================================
