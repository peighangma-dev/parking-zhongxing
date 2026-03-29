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


 四、数据库表结构 (parking_db)
================================================================================ 

【系统基础表】
------------------------------------------------------------------------------
1. sys_tenant            - 租户表
   - id                 - 主键ID
   - tenant_code        - 租户编码 (唯一)
   - tenant_name        - 租户名称
   - contact_name       - 联系人姓名
   - contact_phone      - 联系电话
   - contact_email      - 联系邮箱
   - domain            - 域名
   - package_id        - 套餐ID
   - expire_time       - 到期时间
   - max_users         - 最大用户数
   - max_spaces        - 最大车位数
   - status            - 状态 (active/inactive/suspended)
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

2. sys_user             - 系统用户表
   - id                 - 主键ID
   - username           - 用户名 (唯一)
   - password           - 密码 (BCrypt加密)
   - nickname           - 昵称
   - phone             - 手机号
   - email             - 邮箱
   - avatar            - 头像URL
   - status            - 状态 (normal/disabled)
   - last_login_time   - 最后登录时间
   - tenant_id         - 租户ID
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

3. sys_role            - 角色表
   - id                 - 主键ID
   - role_code         - 角色编码 (唯一)
   - role_name         - 角色名称
   - description       - 描述
   - tenant_id         - 租户ID
   - status            - 状态 (normal/disabled)
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

4. sys_user_role       - 用户角色关联表
   - id                 - 主键ID
   - user_id           - 用户ID
   - role_id           - 角色ID
   - tenant_id         - 租户ID
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

5. sys_menu            - 菜单权限表
   - id                 - 主键ID
   - parent_id         - 父菜单ID
   - menu_name         - 菜单名称
   - menu_type         - 类型 (directory/menu/button)
   - path              - 路由路径
   - component         - 组件路径
   - icon             - 图标
   - permission       - 权限标识
   - sort_order       - 排序
   - tenant_id         - 租户ID
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

6. sys_role_menu       - 角色菜单关联表
   - id                 - 主键ID
   - role_id           - 角色ID
   - menu_id           - 菜单ID
   - created_at        - 创建时间
   - deleted           - 逻辑删除

7. sys_package         - 套餐表
   - id                 - 主键ID
   - package_code      - 套餐编码 (唯一)
   - package_name     - 套餐名称
   - package_type     - 套餐类型
   - description      - 描述
   - max_users        - 最大用户数
   - max_spaces       - 最大车位数
   - max_devices      - 最大设备数
   - price            - 价格
   - sort_order       - 排序
   - status           - 状态 (enabled/disabled)
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

8. sys_package_feature - 套餐功能表
   - id                 - 主键ID
   - package_id        - 套餐ID
   - feature_code      - 功能代码
   - feature_name      - 功能名称
   - enabled           - 是否启用 (0/1)
   - created_at        - 创建时间

【停车场业务表】
------------------------------------------------------------------------------
9. parking_lot         - 停车场表
   - id                 - 主键ID
   - lot_name         - 停车场名称
   - lot_code         - 停车场编码 (唯一)
   - address           - 地址
   - total_spaces     - 总车位数
   - occupied_spaces   - 已占用车位数
   - contact_person   - 联系人
   - contact_phone   - 联系电话
   - status           - 状态 (normal/disabled)
   - description      - 描述
   - tenant_id        - 租户ID
   - created_at        - 创建时间
   - updated_at        - 更新时间
   - deleted           - 逻辑删除

10. parking_area       - 停车场区域表
    - id                 - 主键ID
    - area_name        - 区域名称
    - total_spaces     - 总车位数量
    - occupied_spaces  - 已占用数量
    - floor            - 楼层
    - status           - 状态 (normal/maintenance/closed)
    - tenant_id        - 租户ID
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

11. parking_space      - 车位表
    - id                 - 主键ID
    - space_number     - 车位编号 (唯一)
    - area_id           - 所属区域ID
    - space_type       - 类型 (standard/large/disabled/electric)
    - status           - 状态 (empty/occupied/reserved)
    - tenant_id        - 租户ID
    - vehicle_plate    - 当前车牌号
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

12. barrier           - 道闸设备表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - code             - 设备编码 (唯一)
    - name              - 设备名称
    - location         - 安装位置
    - total_lanes      - 车道数量
    - status           - 状态 (online/offline/fault)
    - raise_timeout    - 抬杆超时秒数
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

13. lane              - 车道表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - barrier_id       - 所属道闸ID
    - lane_name        - 车道名称
    - lane_type        - 类型 (entry/exit)
    - camera_id        - 相机设备ID
    - barrier_controller_id - 道闸控制器ID
    - status           - 状态 (normal/maintenance/fault)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

14. camera            - 摄像头表
    - id                 - 主键ID
    - camera_code      - 摄像头编码 (唯一)
    - camera_name      - 摄像头名称
    - lane_id          - 关联车道ID
    - ip_address      - IP地址
    - port             - 端口
    - username         - 用户名
    - password         - 密码
    - channel         - 通道号
    - stream_url      - 流地址
    - status           - 状态 (online/offline/fault)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

【车辆管理表】
------------------------------------------------------------------------------
15. owner              - 车主信息表
    - id                 - 主键ID
    - name             - 车主姓名
    - phone            - 联系电话 (唯一)
    - id_card          - 身份证号
    - address          - 联系地址
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

16. vehicle            - 车辆表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - plate_number     - 车牌号 (唯一)
    - plate_color      - 车牌颜色
    - vehicle_brand   - 车辆品牌
    - vehicle_type    - 车型 (sedan/suv/truck)
    - owner_id        - 车主ID
    - vehicle_type_cat - 分类 (monthly/temp/vip/blacklist)
    - status           - 状态 (normal/disabled)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

17. blacklist         - 黑名单表
    - id                 - 主键ID
    - vehicle_id       - 车辆ID
    - reason           - 加入原因
    - created_at        - 创建时间
    - created_by       - 创建人ID
    - tenant_id        - 租户ID
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

18. member            - 月卡会员表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - owner_id         - 车主ID
    - vehicle_id       - 车辆ID
    - member_type     - 类型 (monthly/seasonal/annual/vip)
    - start_date       - 生效日期
    - end_date         - 到期日期
    - balance         - 账户余额
    - status           - 状态 (active/expired/cancelled)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

【通行支付表】
------------------------------------------------------------------------------
19. pass_record        - 通行记录表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - plate_number     - 车牌号
    - lane_id          - 车道ID
    - pass_type        - 类型 (entry/exit)
    - pass_status      - 状态 (normal/abnormal)
    - recognition_confidence - 识别置信度
    - image_url        - 抓拍图片URL
    - fee_calculated   - 计算费用
    - pass_time        - 通行时间
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

20. rate_rule          - 费率规则表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - rule_name        - 规则名称
    - rule_type       - 类型 (default/weekend/holiday/night)
    - first_hour_fee  - 首小时费用
    - subsequent_fee  - 续费费用
    - daily_max_fee   - 24小时封顶
    - vehicle_category - 适用车型 (all/small/large)
    - priority        - 优先级
    - status           - 状态 (active/inactive)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

21. payment_channel    - 支付渠道表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - channel_code     - 渠道编码 (唯一)
    - channel_name     - 渠道名称
    - fee_rate        - 手续费率
    - config          - 渠道配置JSON
    - description     - 描述
    - status           - 状态 (enabled/disabled)
    - sort_order      - 排序
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

22. payment           - 支付记录表
    - id                 - 主键ID
    - tenant_id        - 租户ID
    - order_no         - 订单号 (唯一)
    - pass_record_id   - 关联通行记录ID
    - plate_number     - 车牌号
    - amount_due      - 应付金额
    - amount_paid     - 实付金额
    - payment_channel_id - 支付渠道ID
    - payment_status  - 状态 (pending/paid/refunded/reversed)
    - payment_time   - 支付时间
    - transaction_id  - 第三方交易号
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

【发票相关表】
------------------------------------------------------------------------------
23. invoice_title     - 发票抬头表
    - id                 - 主键ID
    - user_id          - 用户ID
    - title_type       - 类型 (personal/enterprise)
    - company_name     - 公司名称
    - tax_number      - 税号
    - bank_name       - 开户银行
    - bank_account    - 银行账号
    - address          - 地址
    - phone           - 电话
    - email           - 邮箱
    - is_default     - 是否默认 (0/1)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

24. invoice           - 发票表
    - id                 - 主键ID
    - invoice_no       - 发票号 (唯一)
    - user_id          - 用户ID
    - title_id         - 发票抬头ID
    - plate_number     - 车牌号
    - amount          - 发票金额
    - tax_amount      - 税额
    - status           - 状态 (pending/approved/issued/rejected)
    - invoice_type    - 类型 (normal/special)
    - billing_time   - 开票时间
    - sending_type   - 发送方式 (email/快递)
    - invoice_url    - 发票PDF URL
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

【其他业务表】
------------------------------------------------------------------------------
25. alarm             - 报警记录表
    - id                 - 主键ID
    - alarm_type      - 报警类型
    - camera_id       - 摄像头ID
    - lane_id         - 车道ID
    - alarm_time      - 报警时间
    - alarm_level     - 级别 (low/medium/high)
    - description    - 描述
    - image_url       - 抓拍图片
    - video_url       - 关联视频
    - status          - 状态 (pending/confirmed/resolved)
    - confirmed_by    - 确认人
    - confirmed_time - 确认时间
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

26. file_info         - 文件信息表
    - id                 - 主键ID
    - file_name       - 文件名
    - file_path       - 文件路径
    - file_url        - 访问URL
    - file_size       - 文件大小(字节)
    - file_type       - 文件类型/MIME
    - storage_type    - 存储类型 (local/aliyun/qiniu/tencent)
    - bucket_name     - 存储桶名称
    - created_by      - 上传人ID
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

27. storage_config    - 存储配置表
    - id                 - 主键ID
    - config_key      - 配置键 (唯一)
    - provider        - 提供商 (aliyun/qiniu/tencent)
    - access_key     - 访问密钥
    - secret_key     - 私有密钥
    - bucket_name    - 存储桶名称
    - endpoint       - 接入点
    - domain         - 自定义域名
    - status          - 状态 (enabled/disabled)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

28. video_record      - 视频录像表
    - id                 - 主键ID
    - camera_id       - 摄像头ID
    - start_time     - 开始时间
    - end_time       - 结束时间
    - file_path      - 文件路径
    - file_size     - 文件大小
    - record_type   - 类型 (continuous/motion/alarm)
    - created_at        - 创建时间
    - updated_at        - 更新时间
    - deleted           - 逻辑删除

29. operation_log     - 操作日志表
    - id                 - 主键ID
    - user_id         - 操作人ID
    - username        - 操作人用户名
    - operation      - 操作类型
    - module         - 操作模块
    - method         - 方法名
    - request_url    - 请求URL
    - request_method - 请求方法
    - request_params - 请求参数
    - response_result - 响应结果
    - ip_address    - IP地址
    - user_agent    - 用户代理
    - execution_time - 执行时长(毫秒)
    - status         - 状态 (success/failed)
    - error_message - 错误信息
    - created_at        - 创建时间
    - deleted           - 逻辑删除


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

【Tenant服务 - 租户功能】  /api/tenant/
------------------------------------------------------------------------------
GET    /api/tenant/v1/features/{tenantId} - 获取租户功能列表
GET    /api/tenant/v1/features/check     - 检查租户功能权限

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

【Report服务 - 报表统计】  /api/report/
------------------------------------------------------------------------------
GET    /api/report/v1/pass-records/page - 通行记录分页


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
当前版本: v2.0.27
构建时间: 2026-03-28
作者: Parking Development Team


十二、Git 分支说明
===============================================================================
当前分支: 260328-fix-security-and-complete-features

主要提交记录:
- 安全修复: JWT/BCrypt/支付回调签名
- 功能增强: 超级管理员多租户查看
- 系统管理: 菜单管理/操作日志/登录日志/系统配置
- 文档更新: README.txt 详细说明

===============================================================================
                              文档结束
===============================================================================
