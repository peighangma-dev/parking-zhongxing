# 停车场管理系统 安装部署说明

## 目录

1. [环境要求](#环境要求)
2. [项目结构](#项目结构)
3. [数据库配置](#数据库配置)
4. [后端服务部署](#后端服务部署)
5. [前端部署](#前端部署)
6. [Nginx配置](#nginx配置)
7. [服务启动脚本](#服务启动脚本)
8. [验证部署](#验证部署)
9. [常见问题](#常见问题)

---

## 环境要求

### 硬件要求
- CPU: 2核及以上
- 内存: 4GB及以上
- 磁盘: 20GB及以上

### 软件要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Spring Boot 2.7.x 需要 JDK 17 |
| Maven | 3.8+ | 后端项目构建工具 |
| MySQL | 8.0+ | 或 MariaDB 10.5+ |
| Redis | 6.0+ | 缓存服务 |
| Nginx | 1.18+ | 反向代理服务器 |
| Node.js | 16+ | 前端构建工具 |
| npm | 8+ | Node.js 包管理器 |

### 操作系统
- Ubuntu 20.04+ (推荐)
- CentOS 7+
- Debian 10+

---

## 项目结构

```
parking-management-system/
├── parking-gateway/          # API网关服务 (端口: 8081)
├── parking-uc/              # 用户认证服务 (端口: 8085)
├── parking-barrier/         # 道闸设备服务 (端口: 8082)
├── parking-vehicle/         # 车辆管理服务 (端口: 8083)
├── parking-payment/         # 支付服务 (端口: 8084)
├── parking-tenant/          # 租户管理服务 (端口: 8090)
├── parking-common/          # 公共模块
├── parking-web/             # 前端Vue3项目
├── deploy/                  # 部署配置文件
│   ├── docker-compose.yml   # Docker Compose配置
│   └── mysql/
│       └── init/            # 数据库初始化脚本
├── pom.xml                  # Maven父项目配置
└── README.txt              # 项目说明文件
```

---

## 数据库配置

### 1. 安装MySQL

```bash
# Ubuntu/Debian
apt update
apt install -y mysql-server mysql-client

# CentOS
yum install -y mysql-server mysql-client
```

### 2. 启动MySQL服务

```bash
service mysql start   # Ubuntu/Debian
systemctl start mysqld  # CentOS
```

### 3. 创建数据库和用户

```sql
-- 登录MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE IF NOT EXISTS parking_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权
CREATE USER 'parking'@'localhost' IDENTIFIED BY 'parking123';
GRANT ALL PRIVILEGES ON parking_db.* TO 'parking'@'localhost';
FLUSH PRIVILEGES;
```

### 4. 初始化数据库表

项目使用MyBatis-Plus自动建表，首次启动服务时会自动创建表结构。但如需手动初始化，可执行：

```bash
mysql -u parking -p parking123 < deploy/mysql/init/01-init.sql
mysql -u parking -p parking123 < deploy/mysql/init/02-uc-ext.sql
```

---

## 后端服务部署

### 1. 安装JDK 17

```bash
# Ubuntu/Debian
apt update
apt install -y openjdk-17-jdk

# 验证安装
java -version
```

### 2. 安装Maven

```bash
apt install -y maven

# 验证安装
mvn -version
```

### 3. 安装Redis

```bash
# Ubuntu/Debian
apt install -y redis-server

# 启动Redis
service redis-server start

# 验证Redis
redis-cli ping
```

### 4. 编译项目

```bash
# 进入项目目录
cd /path/to/parking-management-system

# 清理并编译所有模块
mvn clean package -DskipTests

# 编译指定模块（可选）
mvn clean package -pl parking-gateway -am -DskipTests
```

编译后的JAR文件位于各模块的 `target/` 目录下：

| 服务 | JAR文件 | 默认端口 |
|------|---------|---------|
| Gateway | parking-gateway-1.0.0.jar | 8081 |
| UC | parking-uc-1.0.0.jar | 8085 |
| Barrier | parking-barrier-1.0.0.jar | 8082 |
| Vehicle | parking-vehicle-1.0.0.jar | 8083 |
| Payment | parking-payment-1.0.0.jar | 8084 |
| Tenant | parking-tenant-1.0.0.jar | 8090 |

### 5. 启动服务

```bash
# 启动Gateway
java -jar parking-gateway/target/parking-gateway-1.0.0.jar &

# 启动其他服务（每个服务在不同终端）
java -jar parking-uc/target/parking-uc-1.0.0.jar --server.port=8085 --spring.datasource.password=parking123 &
java -jar parking-barrier/target/parking-barrier-1.0.0.jar --server.port=8082 --spring.datasource.password=parking123 &
java -jar parking-vehicle/target/parking-vehicle-1.0.0.jar --server.port=8083 --spring.datasource.password=parking123 &
java -jar parking-payment/target/parking-payment-1.0.0.jar --server.port=8084 --spring.datasource.password=parking123 &
java -jar parking-tenant/target/parking-tenant-1.0.0.jar --server.port=8090 &
```

### 6. 使用启动脚本

项目提供了一键启动脚本 `deploy/start-services.sh`：

```bash
chmod +x deploy/start-services.sh
./deploy/start-services.sh
```

---

## 前端部署

### 1. 安装Node.js

```bash
# 使用nvm安装Node.js（推荐）
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
source ~/.bashrc
nvm install 18
nvm use 18

# 或直接安装
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
apt install -y nodejs
```

### 2. 安装依赖

```bash
cd parking-web
npm install
```

### 3. 配置API代理

编辑 `vite.config.ts` 文件，配置API代理指向后端Gateway：

```typescript
export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',  // Gateway地址
        changeOrigin: true,
        timeout: 30000
      }
    }
  }
})
```

### 4. 构建生产版本

```bash
npm run build
```

构建完成后，静态文件位于 `dist/` 目录。

---

## Nginx配置

### 1. 安装Nginx

```bash
apt install -y nginx
```

### 2. 配置Nginx

创建配置文件 `/etc/nginx/sites-available/parking.conf`：

```nginx
server {
    listen 80;
    server_name _;

    # 前端静态文件
    location / {
        root /path/to/parking-management-system/parking-web/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # API反向代理
    location /api/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_http_version 1.1;
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 3. 启用配置

```bash
# 删除默认站点
rm /etc/nginx/sites-enabled/default

# 创建符号链接
ln -s /etc/nginx/sites-available/parking.conf /etc/nginx/sites-enabled/

# 测试配置
nginx -t

# 重载Nginx
nginx -s reload
```

### 4. 配置HTTPS（可选）

使用Let's Encrypt免费SSL证书：

```bash
apt install -y certbot python3-certbot-nginx

# 获取证书
certbot --nginx -d yourdomain.com

# 自动续期
certbot renew --dry-run
```

---

## 服务启动脚本

创建文件 `deploy/start-all.sh`：

```bash
#!/bin/bash

# 停车场管理系统启动脚本

PARENT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PARENT_DIR"

echo "=== 启动停车场管理系统 ==="

# 配置
DB_PASSWORD="parking123"
GATEWAY_PORT="8081"
UC_PORT="8085"
BARRIER_PORT="8082"
VEHICLE_PORT="8083"
PAYMENT_PORT="8084"
TENANT_PORT="8090"

# 启动函数
start_service() {
    local name=$1
    local jar=$2
    local port=$3
    local extra=$4
    
    if lsof -i:$port > /dev/null 2>&1; then
        echo "$name 端口 $port 已被占用，跳过启动"
    else
        echo "启动 $name..."
        nohup java -jar $jar --server.port=$port $extra > /tmp/$name.log 2>&1 &
        echo "$name 已启动 (PID: $!)"
    fi
}

# 启动MySQL和Redis
echo "检查数据库服务..."
service mysql start || true
service redis-server start || true

# 等待数据库就绪
sleep 5

# 启动后端服务
start_service "Gateway" "parking-gateway/target/parking-gateway-1.0.0.jar" "$GATEWAY_PORT"
sleep 3
start_service "UC" "parking-uc/target/parking-uc-1.0.0.jar" "$UC_PORT" "--spring.datasource.password=$DB_PASSWORD"
start_service "Barrier" "parking-barrier/target/parking-barrier-1.0.0.jar" "$BARRIER_PORT" "--spring.datasource.password=$DB_PASSWORD"
start_service "Vehicle" "parking-vehicle/target/parking-vehicle-1.0.0.jar" "$VEHICLE_PORT" "--spring.datasource.password=$DB_PASSWORD"
start_service "Payment" "parking-payment/target/parking-payment-1.0.0.jar" "$PAYMENT_PORT" "--spring.datasource.password=$DB_PASSWORD"
start_service "Tenant" "parking-tenant/target/parking-tenant-1.0.0.jar" "$TENANT_PORT" "--spring.datasource.password=$DB_PASSWORD"

# 启动Nginx
echo "启动 Nginx..."
nginx -s reload || nginx

echo ""
echo "=== 服务启动完成 ==="
echo "Gateway: http://localhost:$GATEWAY_PORT"
echo "前端页面: http://localhost/"
echo ""
echo "查看日志: tail -f /tmp/[服务名].log"
```

赋予执行权限并运行：

```bash
chmod +x deploy/start-all.sh
./deploy/start-all.sh
```

---

## 验证部署

### 1. 检查服务状态

```bash
# 检查Java进程
ps aux | grep java | grep parking

# 检查端口监听
ss -tlnp | grep -E "8081|8082|8083|8084|8085|8090|80"
```

### 2. 测试API

```bash
# 测试登录
curl -X POST http://localhost/api/uc/v1/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 预期响应
# {"code":0,"message":"success","data":{"token":"eyJ...","userId":1,...}}
```

### 3. 登录凭证

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 超级管理员 |

---

## 常见问题

### 1. 服务启动失败，端口被占用

```bash
# 查找占用端口的进程
lsof -i:8081
# 或
ss -tlnp | grep 8081

# 终止进程
kill -9 <PID>
```

### 2. 数据库连接失败

```bash
# 检查MySQL服务
service mysql status

# 检查MySQL用户权限
mysql -u root -e "SHOW GRANTS FOR 'parking'@'localhost';"

# 重置密码（如需要）
mysql -u root -e "ALTER USER 'parking'@'localhost' IDENTIFIED BY 'parking123';"
```

### 3. 前端无法访问后端API

1. 检查Nginx日志：`tail /var/log/nginx/error.log`
2. 检查Gateway是否正常启动：`curl http://localhost:8081/actuator/health`
3. 检查防火墙设置：`ufw allow 80` 或 `iptables -L`

### 4. Redis连接失败

```bash
# 检查Redis服务
service redis-server status

# 测试Redis连接
redis-cli ping
# 应返回 PONG
```

### 5. 编译失败

```bash
# 清理Maven缓存
mvn clean

# 重新编译
mvn clean package -DskipTests -U
```

### 6. 403 Forbidden错误

如果Nginx代理返回403，检查：

1. 静态文件路径是否正确
2. 文件权限是否正确（nginx用户需要有读取权限）
3. SELinux是否阻止（CentOS）：`setsebool -P httpd_read_user_content 1`

---

## Docker部署（可选）

项目提供了Docker Compose配置，可一键启动所有服务：

```bash
# 安装Docker和Docker Compose
apt install -y docker.io docker-compose

# 启动所有服务
cd deploy
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

注意：Docker部署会启动MySQL、Redis、Sentinel等中间件服务。

---

## 联系支持

如有问题，请提交Issue或联系技术支持。
