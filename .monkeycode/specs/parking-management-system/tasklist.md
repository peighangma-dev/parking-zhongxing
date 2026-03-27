# 停车场管理系统实施计划

## 阶段一：项目初始化与公共模块

- [x] 1. 初始化 Maven 多模块项目结构
   - 创建 parking-management-system 父 pom.xml
   - 定义聚合模块：parking-gateway, parking-barrier, parking-payment, parking-vehicle, parking-report, parking-common, parking-web
   - 配置统一的 Spring Boot 版本 (3.2)、Java 版本 (17)、编码格式 (UTF-8)

- [x] 2. 创建 parking-common 公共模块
   - [x] 2.1 创建 parking-common-core 核心工具子模块
     - [x] 定义统一响应结果类 Result<T>
     - [x] 定义分页请求和分页结果类
     - [x] 定义系统错误码枚举 ErrorCode
     - [x] 编写异常类：BusinessException、ValidateException
     - [x] 编写日期工具类 DateUtils、字符串工具类 StringUtils
   
   - [x] 2.2 创建 parking-common-database 数据库子模块
     - [x] 集成 MyBatis-Plus 3.5 依赖
     - [x] 编写分页插件配置
     - [x] 编写逻辑删除插件配置
     - [x] 定义 BaseEntity 基础实体类（包含 id, createdAt, updatedAt, deleted 字段）
   
   - [x] 2.3 创建 parking-common-redis 缓存子模块
     - [x] 集成 Redis 和 Redisson
     - [x] 编写 Redis 序列化配置
     - [x] 封装 RedisTemplate 工具类

- [x] 3. 配置 Docker Compose 开发环境
   - [x] 编写 docker-compose.yml 配置 MySQL 8.0、Redis 7.0、Kafka
   - [x] 编写初始化 SQL 脚本创建 parking_db 数据库

## 阶段二：数据库表结构实现

- [x] 4. 创建数据库表结构
   - [x] 4.1 编写 barrier 道闸设备表 SQL
   - [x] 4.2 编写 lane 车道表 SQL
   - [x] 4.3 编写 owner 车主信息表 SQL
   - [x] 4.4 编写 vehicle 车辆表 SQL
   - [x] 4.5 编写 member 月卡表 SQL
   - [x] 4.6 编写 pass_record 通行记录表 SQL
   - [x] 4.7 编写 payment 支付记录表 SQL
   - [x] 4.8 编写 payment_channel 支付渠道表 SQL
   - [x] 4.9 编写 rate_rule 费率规则表 SQL
   - [x] 4.10 编写 blacklist 黑名单表 SQL

## 阶段三：车辆服务 (parking-vehicle) 开发

- [x] 5. 搭建 parking-vehicle 微服务框架
   - [x] 创建 Spring Boot 启动类和配置文件
   - [x] 配置 MyBatis-Plus、Redis、Kafka 连接

- [x] 6. 实现车辆管理功能
   - [x] 6.1 编写 Vehicle 实体类和 VehicleMapper
   - [x] 6.2 编写 VehicleService 业务类
     - [x] 实现车辆 CRUD 功能
     - [x] 实现按车牌号、手机号查询
     - [x] 实现车辆状态管理（正常/禁用）
   
   - [x] 6.3 编写 VehicleController REST API

- [x] 7. 实现车主管理功能
   - [x] 7.1 编写 Owner 实体类和 OwnerMapper
   - [x] 7.2 编写 OwnerService 业务类
   - [x] 7.3 编写 OwnerController REST API

- [x] 8. 实现月卡管理功能
   - [x] 8.1 编写 Member 实体类和 MemberMapper
   - [x] 8.2 编写 MemberService 业务类
     - [x] 实现月卡开通功能
     - [x] 实现月卡续期功能（按月/季/年）
     - [x] 实现月卡有效期验证
   
   - [x] 8.3 编写 MemberController REST API

- [x] 9. 实现黑名单管理功能
   - [x] 9.1 编写 Blacklist 实体类和 BlacklistMapper
   - [x] 9.2 编写 BlacklistService 业务类
   - [x] 9.3 编写 BlacklistController REST API

## 阶段四：支付服务 (parking-payment) 开发

- [x] 10. 搭建 parking-payment 微服务框架
   - [x] 创建 Spring Boot 启动类和配置文件
   - [x] 配置 MyBatis-Plus、Redis 连接

- [x] 11. 实现支付渠道管理
   - [x] 11.1 编写 PaymentChannel 实体类和 PaymentChannelMapper
   - [x] 11.2 编写 PaymentChannelService 业务类
   - [x] 11.3 编写 PaymentChannelController REST API

- [x] 12. 实现费率规则管理
   - [x] 12.1 编写 RateRule 实体类和 RateRuleMapper
   - [x] 12.2 编写 RateRuleService 业务类
   - [x] 12.3 编写 RateRuleController REST API

- [x] 14. 实现支付交易功能
   - [x] 14.1 编写 Payment 实体类和 PaymentMapper
   - [x] 14.2 编写 PaymentService 业务类
     - [x] 实现订单创建（生成唯一订单号）
     - [x] 实现订单状态查询
     - [x] 实现订单取消功能
     - [x] 实现订单冲正功能
   
   - [x] 14.3 编写 PaymentController REST API

- [x] 15. 实现支付渠道适配器
   - [x] 15.1 定义 PaymentChannelAdapter 接口
   - [x] 15.2 编写抽象 AbstractPaymentChannelAdapter 基类
   - [x] 15.3 实现模拟支付适配器 MockPaymentAdapter

## 阶段五：道闸服务 (parking-barrier) 开发

- [x] 16. 搭建 parking-barrier 微服务框架
   - [x] 创建 Spring Boot 启动类和配置文件
   - [x] 配置 MyBatis-Plus、Redis、Kafka 连接

- [x] 17. 实现道闸设备管理
   - [x] 17.1 编写 Barrier 实体类和 BarrierMapper
   - [x] 17.2 编写 BarrierService 业务类
   - [x] 17.3 编写 BarrierController REST API

- [x] 19. 实现道闸控制器组件
   - [x] 19.1 定义 BarrierController 接口
   - [x] 19.2 编写 SimulatedBarrierControl 模拟实现
   - [x] 19.3 编写 BarrierControlController REST API

## 阶段六：报表服务 (parking-report) 开发

- [ ] 25. 搭建 parking-report 微服务框架

- [ ] 26. 实现通行记录查询
   - [ ] 26.1 编写 PassRecordService 查询类
     - 实现按时间段查询
     - 实现按车牌号查询
     - 实现按通道查询
     - 支持分页和排序
   
   - [ ] 26.2 编写 PassRecordController REST API
     - GET /api/report/v1/pass-records 查询记录
     - GET /api/report/v1/pass-records/export 导出 Excel

- [ ] 27. 实现收费报表统计
   - [ ] 27.1 编写 DailySummaryService 日报业务类
     - 统计当日应收/实收金额
     - 统计各支付渠道交易笔数
     - 统计现金收入金额
   
   - [ ] 27.2 编写 MonthlySummaryService 月报业务类
   - [ ] 27.3 编写 ReportController REST API
     - GET /api/report/v1/daily-summary 收费日报
     - GET /api/report/v1/monthly-summary 收费月报

- [ ] 28. 实现日对账功能
   - [ ] 28.1 编写 ReconcileService 对账业务类
     - 核对当日所有交易流水
     - 生成对账差异报告
   
   - [ ] 28.2 实现 POST /api/report/v1/reconcile 日对账接口

## 阶段七：API 网关 (parking-gateway) 开发

- [x] 29. 搭建 parking-gateway 网关服务
   - [x] 集成 Spring Cloud Gateway 2023
   - [x] 配置路由规则（/api/barrier -> barrier-service, /api/payment -> payment-service, /api/vehicle -> vehicle-service, /api/report -> report-service）

- [x] 30. 实现认证过滤器
   - [x] 30.1 编写 AuthFilter 认证过滤器
     - 验证 JWT Token
     - 提取用户信息到请求头
     - 放行公开接口

## 阶段八：前端管理后台 (parking-web) 开发

- [x] 32. 初始化 Vue 3 项目
   - [x] 使用 Vite 5 创建项目
   - [x] 集成 Element Plus 2.5
   - [x] 集成 Vue Router 4
   - [x] 集成 Pinia 状态管理
   - [x] 集成 Axios HTTP 客户端

- [x] 33. 实现道闸管理页面
   - [x] 33.1 道闸设备列表页

- [x] 34. 实现支付管理页面
   - [x] 34.1 支付渠道列表页

- [x] 35. 实现车辆管理页面
   - [x] 35.1 车辆列表页（支持分页、搜索）
   - [x] 35.3 月卡管理页

- [x] 37. 实现系统公共组件
   - [x] 37.1 登录页面
   - [x] 37.2 首页仪表盘

## 阶段九：联调与测试

- [ ] 38. 配置前端反向代理
   - 在 Vite 中配置代理解决跨域问题
   - 代理 /api/barrier -> http://localhost:8081
   - 代理 /api/payment -> http://localhost:8082
   - 代理 /api/vehicle -> http://localhost:8083
   - 代理 /api/report -> http://localhost:8084

- [ ] 39. 编写集成测试
   - [ ] 39.1 编写道闸服务集成测试
   - [ ] 39.2 编写支付服务集成测试
   - [ ] 39.3 编写车辆服务集成测试

- [ ] 40. 验证核心业务流程
   - 验证月卡车辆入场自动抬杆
   - 验证临时车辆缴费后抬杆
   - 验证黑名单车辆拒绝入场
   - 验证各类支付方式正常交易

- [ ] 41. 性能测试与优化
   - 验证车牌识别到抬杆响应时间 ≤ 500ms
   - 验证系统支持 100 并发用户

## 阶段十：用户权限服务 (parking-uc) 开发

- [ ] 42. 搭建 parking-uc 用户权限微服务框架
   - 创建 Spring Boot 启动类和配置文件
   - 配置 MyBatis-Plus、Redis 连接

- [ ] 43. 实现用户管理
   - [ ] 43.1 编写 User 实体类和 UserMapper
     - 字段：id, username, password, nickname, phone, email, avatar, status, last_login_time
   - [ ] 43.2 编写 UserService 业务类
     - 实现用户注册功能（密码加密存储）
     - 实现用户登录功能（JWT Token 生成）
     - 实现用户信息查询和修改
     - 实现密码修改和找回
   
   - [ ] 43.3 编写 UserController REST API
     - POST /api/uc/v1/users/register 用户注册
     - POST /api/uc/v1/users/login 用户登录
     - GET /api/uc/v1/users/{id} 用户详情
     - PUT /api/uc/v1/users/{id} 更新用户
     - POST /api/uc/v1/users/{id}/password 修改密码

- [ ] 44. 实现角色管理
   - [ ] 44.1 编写 Role 实体类和 RoleMapper
     - 字段：id, role_code, role_name, description, status
   - [ ] 44.2 编写 RoleService 业务类
     - 实现角色 CRUD
     - 实现角色绑定菜单权限
   
   - [ ] 44.3 编写 RoleController REST API
     - POST /api/uc/v1/roles 角色创建
     - GET /api/uc/v1/roles 角色列表
     - PUT /api/uc/v1/roles/{id} 角色更新
     - DELETE /api/uc/v1/roles/{id} 角色删除

- [ ] 45. 实现菜单权限管理
   - [ ] 45.1 编写 Menu 实体类和 MenuMapper
     - 字段：id, parent_id, menu_name, menu_type, path, component, icon, permission, sort_order
     - menu_type: directory/menu/button
   - [ ] 45.2 编写 MenuService 业务类
     - 实现菜单树形结构查询
     - 实现根据角色获取菜单权限
   
   - [ ] 45.3 编写 MenuController REST API
     - POST /api/uc/v1/menus 菜单创建
     - GET /api/uc/v1/menus/tree 菜单树形列表
     - PUT /api/uc/v1/menus/{id} 菜单更新
     - DELETE /api/uc/v1/menus/{id} 菜单删除

- [ ] 46. 实现用户角色绑定
   - [ ] 46.1 编写 UserRole 实体类和 UserRoleMapper
   - [ ] 46.2 编写 UserRoleService 业务类
     - 实现用户绑定角色
     - 实现角色分配用户列表查询
   - [ ] 46.3 编写 UserRoleController REST API
     - POST /api/uc/v1/users/{userId}/roles 分配角色
     - GET /api/uc/v1/users/{userId}/roles 获取用户角色

- [ ] 47. 实现登录认证与权限校验
   - [ ] 47.1 集成 JWT Token 认证
     - 实现 Token 生成和验证
     - 实现 Token 刷新机制
     - 实现 Token 黑名单（退出登录）
   
   - [ ] 47.2 编写权限校验注解和拦截器
     - @RequiredPermissions 注解
     - 校验用户菜单权限

## 阶段十一：车位管理服务 (parking-space) 开发

- [ ] 48. 搭建 parking-space 车位管理微服务框架

- [ ] 49. 实现停车场区域管理
   - [ ] 49.1 编写 ParkingArea 实体类和 ParkingAreaMapper
     - 字段：id, area_name, total_spaces, occupied_spaces, floor, status
   - [ ] 49.2 编写 ParkingAreaService 业务类
     - 实现区域 CRUD
     - 实现车位统计更新
   
   - [ ] 49.3 编写 ParkingAreaController REST API
     - POST /api/space/v1/areas 区域创建
     - GET /api/space/v1/areas 区域列表
     - PUT /api/space/v1/areas/{id} 区域更新
     - DELETE /api/space/v1/areas/{id} 区域删除

- [ ] 50. 实现车位管理
   - [ ] 50.1 编写 ParkingSpace 实体类和 ParkingSpaceMapper
     - 字段：id, space_number, area_id, space_type, status, vehicle_plate
     - status: empty/occupied/reserved
     - space_type: standard/large/disabled/electric
   - [ ] 50.2 编写 ParkingSpaceService 业务类
     - 实现车位 CRUD
     - 实现车位状态查询
     - 实现车位占用/释放
   
   - [ ] 50.3 编写 ParkingSpaceController REST API
     - POST /api/space/v1/spaces 车位创建
     - GET /api/space/v1/spaces 车位列表
     - PUT /api/space/v1/spaces/{id} 车位更新
     - DELETE /api/space/v1/spaces/{id} 车位删除
     - GET /api/space/v1/spaces/status 车位状态统计

- [ ] 51. 实现车位实时监控
   - [ ] 51.1 编写 SpaceMonitorService 监控业务类
     - 实时更新车位占用状态
     - 推送车位变化事件到 Kafka
   - [ ] 51.2 集成 Redis 实现车位状态缓存
     - 使用 Redis Hash 存储实时车位状态
     - 实现车位状态变更广播

- [ ] 52. 实现车位引导功能
   - [ ] 52.1 编写 GuideService 引导业务类
     - 实现最短路径引导算法
     - 生成车位引导指令
   - [ ] 52.2 集成车位引导屏设备
     - 实现引导信息下发
     - 支持 LED/LCD 引导屏协议

## 阶段十二：文件存储服务 (parking-oss) 开发

- [ ] 53. 搭建 parking-oss 文件存储微服务框架

- [ ] 54. 实现云存储适配器
   - [ ] 54.1 定义 StorageAdapter 接口
     - 定义上传、下载、删除、查询文件方法
   - [ ] 54.2 编写抽象 AbstractStorageAdapter 基类
     - 实现通用文件处理逻辑
   - [ ] 54.3 实现七牛云存储适配器 QiniuStorageAdapter
     - 集成七牛 SDK
     - 实现文件上传到七牛云
     - 实现文件访问 URL 生成
     - 支持私有空间签名 URL
   - [ ] 54.4 实现阿里云OSS适配器 AliyunStorageAdapter
     - 集成阿里云 OSS SDK
     - 实现文件上传到阿里云 OSS
   - [ ] 54.5 实现腾讯云COS适配器 TencentStorageAdapter
     - 集成腾讯云 COS SDK
     - 实现文件上传到腾讯云 COS

- [ ] 55. 实现文件管理功能
   - [ ] 55.1 编写 FileInfo 实体类和 FileInfoMapper
     - 字段：id, file_name, file_path, file_url, file_size, file_type, storage_type, bucket_name, created_by, created_at
   - [ ] 55.2 编写 FileService 业务类
     - 实现文件上传（自动选择云存储）
     - 实现文件删除（同时删除云端文件）
     - 实现文件查询（按用户、类型查询）
   
   - [ ] 55.3 编写 FileController REST API
     - POST /api/oss/v1/files/upload 文件上传
     - GET /api/oss/v1/files/{id} 文件信息
     - DELETE /api/oss/v1/files/{id} 文件删除
     - GET /api/oss/v1/files/list 文件列表

- [ ] 56. 实现文件存储配置管理
   - [ ] 56.1 编写 StorageConfig 实体类和 StorageConfigMapper
     - 字段：id, config_key, provider, access_key, secret_key, bucket_name, endpoint, domain, status
   - [ ] 56.2 编写 StorageConfigService 业务类
     - 实现多云存储配置管理
     - 实现配置启用/禁用
   
   - [ ] 56.3 编写 StorageConfigController REST API
     - POST /api/oss/v1/configs 配置创建
     - GET /api/oss/v1/configs 配置列表
     - PUT /api/oss/v1/configs/{id} 配置更新
     - DELETE /api/oss/v1/configs/{id} 配置删除

## 阶段十三：发票服务 (parking-invoice) 开发

- [ ] 57. 搭建 parking-invoice 发票微服务框架

- [ ] 58. 实现发票抬头管理
   - [ ] 58.1 编写 InvoiceTitle 实体类和 InvoiceTitleMapper
     - 字段：id, user_id, title_type, company_name, tax_number, bank_name, bank_account, address, phone, email, is_default
     - title_type: personal/enterprise
   - [ ] 58.2 编写 InvoiceTitleService 业务类
     - 实现发票抬头 CRUD
     - 实现默认抬头设置
   
   - [ ] 58.3 编写 InvoiceTitleController REST API
     - POST /api/invoice/v1/titles 抬头创建
     - GET /api/invoice/v1/titles 抬头列表
     - PUT /api/invoice/v1/titles/{id} 抬头更新
     - DELETE /api/invoice/v1/titles/{id} 抬头删除
     - PUT /api/invoice/v1/titles/{id}/default 设置默认

- [ ] 59. 实现发票申请管理
   - [ ] 59.1 编写 Invoice 实体类和 InvoiceMapper
     - 字段：id, invoice_no, user_id, title_id, plate_number, amount, tax_amount, status, invoice_type, billing_time, sending_type
     - status: pending/approved/issued/rejected
     - invoice_type: normal/special
     - sending_type: email/快递
   - [ ] 59.2 编写 InvoiceService 业务类
     - 实现发票申请创建
     - 实现发票申请审批
     - 实现发票开具（调用电子发票平台）
     - 实现发票作废
   
   - [ ] 59.3 编写 InvoiceController REST API
     - POST /api/invoice/v1/invoices 发票申请
     - GET /api/invoice/v1/invoices 发票列表
     - GET /api/invoice/v1/invoices/{id} 发票详情
     - PUT /api/invoice/v1/invoices/{id}/approve 审批通过
     - PUT /api/invoice/v1/invoices/{id}/reject 审批拒绝
     - PUT /api/invoice/v1/invoices/{id}/issue 开具发票
     - PUT /api/invoice/v1/invoices/{id}/cancel 作废发票

- [ ] 60. 实现电子发票开具
   - [ ] 60.1 定义 InvoiceIssuer 接口
   - [ ] 60.2 实现电子发票平台对接
     - 生成发票 PDF
     - 推送到用户邮箱
     - 提供发票查验接口

## 阶段十四：视频监控服务 (parking-monitor) 开发

- [ ] 61. 搭建 parking-monitor 视频监控微服务框架

- [ ] 62. 实现摄像头设备管理
   - [ ] 62.1 编写 Camera 实体类和 CameraMapper
     - 字段：id, camera_code, camera_name, lane_id, ip_address, port, username, password, channel, stream_url, status
   - [ ] 62.2 编写 CameraService 业务类
     - 实现摄像头 CRUD
     - 实现摄像头状态监控
   
   - [ ] 62.3 编写 CameraController REST API
     - POST /api/monitor/v1/cameras 摄像头注册
     - GET /api/monitor/v1/cameras 摄像头列表
     - PUT /api/monitor/v1/cameras/{id} 摄像头更新
     - DELETE /api/monitor/v1/cameras/{id} 摄像头删除

- [ ] 63. 实现实时视频流
   - [ ] 63.1 定义 VideoStreamProvider 接口
   - [ ] 63.2 实现 RTSP 流媒体服务
     - 集成 FFmpeg 流媒体处理
     - 实现实时视频流拉取
     - 支持 HLS/RTMP/WebRTC 输出
   - [ ] 63.3 编写 VideoService 业务类
     - 实现视频流地址获取
     - 实现视频截图

- [ ] 64. 实现视频录像管理
   - [ ] 64.1 编写 VideoRecord 实体类和 VideoRecordMapper
     - 字段：id, camera_id, start_time, end_time, file_path, file_size, record_type
     - record_type: continuous/motion/alarm
   - [ ] 64.2 编写 VideoRecordService 业务类
     - 实现定时录像
     - 实现录像回放
     - 实现录像下载
   
   - [ ] 64.3 编写 VideoRecordController REST API
     - GET /api/monitor/v1/records 录像列表
     - GET /api/monitor/v1/records/{id}/playback 获取回放地址
     - GET /api/monitor/v1/records/{id}/download 下载录像

- [ ] 65. 实现报警管理
   - [ ] 65.1 编写 Alarm 实体类和 AlarmMapper
     - 字段：id, alarm_type, camera_id, lane_id, alarm_time, alarm_level, description, image_url, video_url, status
   - [ ] 65.2 编写 AlarmService 业务类
     - 实现报警记录
     - 实现报警确认
     - 实现报警统计
   
   - [ ] 65.3 编写 AlarmController REST API
     - POST /api/monitor/v1/alarms 报警记录
     - GET /api/monitor/v1/alarms 报警列表
     - PUT /api/monitor/v1/alarms/{id}/confirm 确认报警

## 阶段十五：扩展报表功能

- [ ] 66. 实现账单管理
   - [ ] 66.1 编写 Bill 实体类和 BillMapper
     - 字段：id, bill_no, user_id, member_id, bill_type, amount, status, billing_date, due_date, paid_time
   - [ ] 66.2 编写 BillService 业务类
     - 实现月卡账单生成
     - 实现账单查询
     - 实现账单支付
   
   - [ ] 66.3 编写 BillController REST API
     - GET /api/report/v1/bills 账单列表
     - GET /api/report/v1/bills/{id} 账单详情
     - POST /api/report/v1/bills/{id}/pay 账单支付

- [ ] 67. 实现财务报表
   - [ ] 67.1 编写 FinanceReportService 财务报业务类
     - 收入统计日报、月报、年报
     - 各支付渠道收入占比分析
     - 现金收款与电子收款对比
     - 退款和冲正统计
   
   - [ ] 67.2 实现财务对账单
     - 日终对账文件生成
     - 与支付渠道对账文件核对

- [ ] 68. 实现经营分析报表
   - [ ] 68.1 编写 BusinessAnalysisService 经营分析业务类
     - 车流量分析（分时段、分日期）
     - 车位利用率分析
     - 月卡续费率分析
     - 收费效率分析
   
   - [ ] 68.2 实现数据可视化
     - 集成 ECharts 图表
     - 支持导出 PDF 报表

## 阶段十六：前端补充页面开发

- [ ] 69. 实现用户权限页面
   - [ ] 69.1 用户管理页面
     - 用户列表、用户注册、用户编辑
   - [ ] 69.2 角色管理页面
     - 角色列表、角色创建、权限分配
   - [ ] 69.3 菜单管理页面
     - 菜单树形列表、菜单编辑

- [ ] 70. 实现车位管理页面
   - [ ] 70.1 停车场区域管理页面
   - [ ] 70.2 车位管理页面（支持批量操作）
   - [ ] 70.3 车位监控大屏页面
     - 实时车位状态显示
     - 车位引导信息发布

- [ ] 71. 实现文件管理页面
   - [ ] 71.1 云存储配置页面
   - [ ] 71.2 文件管理页面（上传、预览、删除）

- [ ] 72. 实现发票管理页面
   - [ ] 72.1 发票抬头管理页面
   - 72.2 发票申请页面
   - [ ] 72.3 发票管理页面（审批、开具）
   - [ ] 72.4 发票统计页面

- [ ] 73. 实现视频监控页面
   - [ ] 73.1 摄像头列表页面
   - [ ] 73.2 实时视频预览页面
   - [ ] 73.3 录像回放页面
   - [ ] 73.4 报警列表页面

- [ ] 74. 实现扩展报表页面
   - [ ] 74.1 账单管理页面
   - [ ] 74.2 财务报表页面
   - [ ] 74.3 经营分析页面（含图表）
   - [ ] 74.4 数据导出功能

## 阶段十七：联调与测试

- [ ] 75. 完善前端反向代理配置
   - 代理 /api/uc -> http://localhost:8085
   - 代理 /api/space -> http://localhost:8086
   - 代理 /api/oss -> http://localhost:8087
   - 代理 /api/invoice -> http://localhost:8088
   - 代理 /api/monitor -> http://localhost:8089

- [ ] 76. 编写服务间集成测试
   - [ ] 76.1 测试车辆服务与道闸服务集成
   - [ ] 76.2 测试支付服务与道闸服务集成
   - [ ] 76.3 测试文件上传和云存储集成
   - [ ] 76.4 测试发票开具流程

- [ ] 77. 验证完整业务流程
   - 验证月卡车辆入场自动抬杆
   - 验证临时车辆缴费后抬杆
   - 验证黑名单车辆拒绝入场
   - 验证各类支付方式正常交易
   - 验证电子发票开具和发送
   - 验证实时视频监控

## 阶段十八：部署配置

- [ ] 78. 完善 Docker Compose 部署文件
   - 添加所有新增微服务的 Dockerfile
   - 配置 MySQL 库表初始化
   - 配置 Redis 集群
   - 配置 Kafka 集群
   - 配置各个微服务环境变量

- [ ] 79. 编写完整部署文档
   - 编写环境要求说明
   - 编写各云服务商配置指南
   - 编写服务编排说明
   - 编写运维手册（含日志查看、故障排查）
   - 编写备份恢复方案

---

## 检查点

- [ ] 检查点 1：确保所有服务启动成功，数据库连接正常
- [ ] 检查点 2：确保 MyBatis-Plus 自动生成代码正常
- [ ] 检查点 3：确保各微服务 API 接口可正常调用
- [ ] 检查点 4：确保前端项目构建成功
- [ ] 检查点 5：确保新增模块核心业务流程端到端测试通过
