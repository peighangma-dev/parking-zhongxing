# 宝塔面板部署停车场管理系统

## 目录

1. [环境准备](#环境准备)
2. [宝塔面板安装](#宝塔面板安装)
3. [软件安装](#软件安装)
4. [数据库配置](#数据库配置)
5. [后端服务部署](#后端服务部署)
6. [前端部署](#前端部署)
7. [网站配置](#网站配置)
8. [SSL证书配置](#ssl证书配置)
9. [服务维护](#服务维护)
10. [常见问题](#常见问题)

---

## 环境准备

### 服务器要求
- 系统：Ubuntu 20.04+ / CentOS 7+
- 配置：2核CPU / 4GB内存 / 50GB磁盘
- 网络：独立IP

---

## 宝塔面板安装

### Ubuntu/Debian

```bash
wget -O install.sh https://download.bt.cn/install/install-ubuntu_6.0.sh && sudo bash install.sh
```

### CentOS

```bash
yum install -y wget && wget -O install.sh https://download.bt.cn/install/install_6.0.sh && bash install.sh
```

### 安装完成后

安装完成后会显示面板登录信息：
- 面板地址：http://IP:8888
- 用户名：admin
- 密码：xxxxxx

请保存好这些信息，首次登录需要强制修改密码。

---

## 软件安装

### 登录宝塔面板

1. 打开浏览器访问：`http://您的服务器IP:8888`
2. 输入用户名和密码登录

### 安装推荐软件

在「软件商店」中安装以下软件：

| 软件名称 | 版本 | 用途 |
|---------|------|------|
| Nginx | 1.22 | Web服务器/反向代理 |
| MySQL | 8.0 | 数据库 |
| Redis | 6.2 | 缓存服务 |
| JDK | 17 | Java运行环境 |
| Node.js | 16 | 前端构建 |

### 安装步骤

1. 点击「软件商店」
2. 搜索并安装以下软件（点击右侧「安装」）：
   - Nginx（点击设置 -> 选择版本1.22 -> 确认安装）
   - MySQL（点击设置 -> 选择版本8.0 -> 确认安装）
   - Redis（点击设置 -> 选择版本6.2 -> 确认安装）
   - JDK（搜索java，选择Tomcat或直接选择JDK17）
   - Node.js（搜索node，选择16版本）

### 等待安装完成

软件安装可能需要5-15分钟，请耐心等待。

---

## 数据库配置

### 1. 创建数据库

1. 打开宝塔面板 -> 点击「数据库」
2. 点击「添加数据库」

填写信息：
- 数据库名：`parking_db`
- 用户名：`parking`
- 密码：`parking123`（建议使用更强密码）
- 编码：`utf8mb4`

点击「提交」

### 2. 导入初始化数据（可选）

如果项目有初始化SQL文件：

1. 点击数据库列表右侧「管理」按钮
2. 点击「导入」
3. 上传SQL文件或直接粘贴SQL内容
4. 点击「执行」

### 3. 创建用户并授权

在phpMyAdmin中执行（或者在宝塔数据库管理中）：

```sql
-- 创建用户（如已通过宝塔创建，可跳过）
CREATE USER 'parking'@'localhost' IDENTIFIED BY 'parking123';

-- 授权
GRANT ALL PRIVILEGES ON parking_db.* TO 'parking'@'localhost';
FLUSH PRIVILEGES;
```

---

## 后端服务部署

### 1. 创建项目目录

```bash
# 在服务器上创建目录
mkdir -p /www/parking
cd /www/parking
```

### 2. 上传后端代码

方式一：使用Git克隆
```bash
cd /www/parking
git clone https://github.com/您的仓库/parking-management-system.git
```

方式二：使用宝塔文件管理上传
1. 打开宝塔 -> 点击「文件」
2. 进入 `/www/parking` 目录
3. 点击「上传」按钮
4. 选择项目压缩包上传
5. 上传完成后解压

### 3. 编译后端项目

```bash
# 进入项目目录
cd /www/parking/parking-management-system

# 清理并编译
mvn clean package -DskipTests
```

如果服务器没有安装Maven，需要先安装：

```bash
# 在宝塔软件商店安装Maven，或手动安装
apt install -y maven

# 或者下载Maven
cd /tmp
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
tar -xzf apache-maven-3.9.6-bin.tar.gz -C /www/server/
ln -s /www/server/apache-maven-3.9.6/bin/mvn /usr/bin/mvn
```

### 4. 修改数据库连接配置（如果需要）

在各服务的 `application.yml` 中修改数据库连接信息：

```yaml
spring:
  datasource:
    username: parking
    password: parking123
    url: jdbc:mysql://localhost:3306/parking_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
```

### 5. 启动后端服务

```bash
cd /www/parking/parking-management-system

# 创建日志目录
mkdir -p /www/parking/logs

# 启动Gateway（后台运行）
nohup java -jar parking-gateway/target/parking-gateway-1.0.0.jar --server.port=8081 > /www/parking/logs/gateway.log 2>&1 &

# 等待3秒
sleep 3

# 启动其他服务
nohup java -jar parking-uc/target/parking-uc-1.0.0.jar --server.port=8085 --spring.datasource.password=parking123 > /www/parking/logs/uc.log 2>&1 &

nohup java -jar parking-barrier/target/parking-barrier-1.0.0.jar --server.port=8082 --spring.datasource.password=parking123 > /www/parking/logs/barrier.log 2>&1 &

nohup java -jar parking-vehicle/target/parking-vehicle-1.0.0.jar --server.port=8083 --spring.datasource.password=parking123 > /www/parking/logs/vehicle.log 2>&1 &

nohup java -jar parking-payment/target/parking-payment-1.0.0.jar --server.port=8084 --spring.datasource.password=parking123 > /www/parking/logs/payment.log 2>&1 &

nohup java -jar parking-tenant/target/parking-tenant-1.0.0.jar --server.port=8090 --spring.datasource.password=parking123 > /www/parking/logs/tenant.log 2>&1 &

# 检查服务状态
sleep 10
ps aux | grep java | grep parking
```

### 6. 验证后端服务

```bash
# 测试Gateway健康检查
curl http://localhost:8081/actuator/health

# 测试登录API
curl -X POST http://localhost:8081/api/uc/v1/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

## 前端部署

### 1. 安装Node.js依赖

```bash
cd /www/parking/parking-management-system/parking-web

# 安装依赖
npm install
```

### 2. 修改API配置（如需要）

编辑 `vite.config.ts`：

```typescript
export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8081',  // 指向后端Gateway
        changeOrigin: true
      }
    }
  }
})
```

### 3. 构建前端

```bash
cd /www/parking/parking-management-system/parking-web

# 构建生产版本
npm run build
```

构建完成后，静态文件在 `dist/` 目录。

---

## 网站配置

### 1. 创建网站

1. 宝塔面板 -> 点击「网站」
2. 点击「添加站点」

填写信息：
- 域名：填写您的域名，如 `parking.yourdomain.com`（或填IP用于测试）
- 根目录：`/www/parking/parking-management-system/parking-web/dist`
- FTP：不创建
- 数据库：选择已有 `parking_db`
- PHP版本：纯静态（选择「纯静态」）

点击「提交」

### 2. 配置反向代理

1. 网站列表中找到刚创建的网站
2. 点击「设置」
3. 点击「反向代理」
4. 点击「添加反向代理」

填写信息：
- 代理名称：`api`
- 目标URL：`http://127.0.0.1:8081`
- 发送域名：`$host`
- 内容替换：留空

点击「提交」

### 3. 配置URL伪静态

为了让前端路由正常工作，需要配置伪静态规则。

1. 网站设置 -> 点击「伪静态」
2. 选择 `vue-history` 或 `history`
3. 点击「保存」

如果没有vue-history选项，手动添加规则：

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

### 4. 配置文件

点击「配置文件」，在server段添加：

```nginx
# API反向代理
location /api/ {
    proxy_pass http://127.0.0.1:8081;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}

# 跨域设置
add_header 'Access-Control-Allow-Origin' '*' always;
add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS' always;
add_header 'Access-Control-Allow-Headers' 'Content-Type, Authorization' always;

# OPTIONS预检请求处理
if ($request_method = 'OPTIONS') {
    add_header 'Access-Control-Allow-Origin' '*';
    add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS';
    add_header 'Access-Control-Allow-Headers' 'Content-Type, Authorization';
    add_header 'Access-Control-Max-Age' 1728000;
    add_header 'Content-Type' 'text/plain charset=UTF-8';
    add_header 'Content-Length' 0;
    return 204;
}
```

点击「保存」

### 5. 重载Nginx配置

点击「重载配置」或执行：

```bash
nginx -s reload
```

---

## SSL证书配置

### 方式一：使用Let's Encrypt免费证书（推荐）

1. 网站设置 -> 点击「SSL」
2. 点击「Let's Encrypt」
3. 填写邮箱
4. 勾选域名
5. 点击「申请"

申请成功后会自动配置SSL。

### 方式二：使用宝塔SSL

1. 网站设置 -> 点击「SSL」
2. 点击「其他证书」
3. 粘贴证书和私钥内容
4. 点击「保存」

证书文件位置：
- 证书：`/etc/ssl/certs/您的域名.crt`
- 私钥：`/etc/ssl/private/您的域名.key`

### 强制HTTPS

申请SSL成功后，勾选「强制HTTPS」。

---

## 服务维护

### 启动/停止服务

```bash
# 停止所有服务
pkill -f "parking-"

# 启动所有服务
cd /www/parking/parking-management-system

nohup java -jar parking-gateway/target/parking-gateway-1.0.0.jar --server.port=8081 > /www/parking/logs/gateway.log 2>&1 &

sleep 2

nohup java -jar parking-uc/target/parking-uc-1.0.0.jar --server.port=8085 --spring.datasource.password=parking123 > /www/parking/logs/uc.log 2>&1 &
nohup java -jar parking-barrier/target/parking-barrier-1.0.0.jar --server.port=8082 --spring.datasource.password=parking123 > /www/parking/logs/barrier.log 2>&1 &
nohup java -jar parking-vehicle/target/parking-vehicle-1.0.0.jar --server.port=8083 --spring.datasource.password=parking123 > /www/parking/logs/vehicle.log 2>&1 &
nohup java -jar parking-payment/target/parking-payment-1.0.0.jar --server.port=8084 --spring.datasource.password=parking123 > /www/parking/logs/payment.log 2>&1 &
nohup java -jar parking-tenant/target/parking-tenant-1.0.0.jar --server.port=8090 --spring.datasource.password=parking123 > /www/parking/logs/tenant.log 2>&1 &
```

### 查看日志

```bash
# 实时查看Gateway日志
tail -f /www/parking/logs/gateway.log

# 查看所有日志
ls -la /www/parking/logs/
```

### 使用Supervisor管理服务（推荐）

1. 宝塔软件商店安装「Supervisor」
2. 点击「添加守护进程」

为每个服务添加守护进程：

**Gateway:**
```
名称：parking-gateway
启动用户：root
启动目录：/www/parking/parking-management-system
启动命令：java -jar parking-gateway/target/parking-gateway-1.0.0.jar --server.port=8081
```

**UC:**
```
名称：parking-uc
启动用户：root
启动目录：/www/parking/parking-management-system
启动命令：java -jar parking-uc/target/parking-uc-1.0.0.jar --server.port=8085 --spring.datasource.password=parking123
```

（其他服务类似配置）

添加完成后，点击「运行」启动所有服务。

### 防火墙配置

确保以下端口已开放：

| 端口 | 用途 |
|------|------|
| 80 | HTTP |
| 443 | HTTPS |
| 8888 | 宝塔面板 |

在服务器安全组和宝塔防火墙中都需开放。

---

## 常见问题

### 1. 访问网站显示403 Forbidden

检查：
- 网站根目录是否正确
- 目录权限：`chmod -R 755 /www/parking/parking-management-system/parking-web/dist`
- Nginx配置是否正确

### 2. API返回404

检查：
- 反向代理是否配置
- 后端服务是否启动：`ps aux | grep java | grep parking`
- 端口是否正确：`curl http://localhost:8081/api/uc/v1/login`

### 3. 数据库连接失败

检查：
- MySQL服务是否运行
- 数据库用户名密码是否正确
- 宝塔防火墙是否开放3306端口

### 4. Redis连接失败

```bash
# 检查Redis
redis-cli ping
# 应返回 PONG

# 如需修改Redis密码
redis-cli config set requirepass "您的密码"
```

### 5. 页面空白

可能原因：
- 静态文件路径错误
- 伪静态规则未配置
- 浏览器缓存，强制刷新 Ctrl+F5

### 6. 内存不足

如果服务器内存较小，可能需要增加swap：

```bash
# 创建2GB swap
fallocate -l 2G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile
echo '/swapfile none swap sw 0 0' >> /etc/fstab
```

---

## 默认登录信息

| 项目 | 值 |
|------|-----|
| 前台地址 | http://您的域名或IP |
| 数据库名 | parking_db |
| 数据库用户 | parking |
| 数据库密码 | parking123 |
| 管理后台账号 | admin |
| 管理后台密码 | admin123 |

---

## 目录结构参考

```
/www/
├── parking/                      # 项目根目录
│   ├── parking-management-system/ # 后端项目
│   │   ├── parking-gateway/      # 网关服务
│   │   ├── parking-uc/          # 用户服务
│   │   ├── parking-barrier/    # 道闸服务
│   │   ├── parking-vehicle/     # 车辆服务
│   │   ├── parking-payment/     # 支付服务
│   │   ├── parking-tenant/      # 租户服务
│   │   └── parking-web/         # 前端项目
│   │       └── dist/           # 构建产物（网站根目录）
│   └── logs/                    # 日志目录
│       ├── gateway.log
│       ├── uc.log
│       └── ...
└── server/
    ├── panel/                   # 宝塔面板
    ├── nginx/                   # Nginx
    ├── mysql/                   # MySQL
    └── java/                    # JDK
```

---

## 联系支持

如有问题，请提交Issue或联系技术支持。
