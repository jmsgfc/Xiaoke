# 小可日常

`xiaoke-daily` 是一个记录小可照片、回忆手账和人物档案的全栈网站。

## 技术栈

- 前端：Vue 3 + Vite + TypeScript
- 后端：Spring Boot 3 + Spring Data JPA
- 数据库：MySQL 8
- 部署方式：Docker Compose

## 项目结构

```text
xiaoke-daily/
├─ frontend/          # 前端项目
├─ backend/           # 后端项目
├─ uploads/           # 图片上传目录
├─ logs/              # 本地运行日志
├─ docker-compose.yml # Docker 编排
└─ .env.example       # 环境变量模板
```

## 推荐启动方式：Docker

### 1. 准备环境变量

先复制一份环境文件：

```powershell
Copy-Item .env.example .env
```

然后按你的实际情况修改 `.env`，至少确认这些值：

```env
MYSQL_DATABASE=xiaoke_daily
MYSQL_USERNAME=root
MYSQL_PASSWORD=请改成你自己的数据库密码
MYSQL_ROOT_PASSWORD=请改成你自己的数据库 root 密码
ADMIN_INITIAL_USERNAME=admin
ADMIN_INITIAL_PASSWORD=请改成你自己的后台密码
```

### 2. 启动整站

```powershell
docker compose up -d --build
```

### 3. 访问地址

- 前台首页：[http://127.0.0.1:5173](http://127.0.0.1:5173)
- 后端接口根地址：[http://127.0.0.1:8080/api](http://127.0.0.1:8080/api)
- 管理后台入口：[http://127.0.0.1:5173/admin/login](http://127.0.0.1:5173/admin/login)

### 4. 停止服务

```powershell
docker compose down
```

如果你还想把数据库也清空：

```powershell
docker compose down -v
```

注意：`-v` 会删除 MySQL 数据卷，数据库会回到第一次初始化状态。

## Docker 说明

- `mysql` 服务会自动执行 `backend/sql/` 里的初始化脚本
- 图片目录会挂载到容器内 `/data/uploads`
- 前端由 `nginx` 提供静态页面，并反向代理 `/api` 和 `/uploads`
- 第一次启动会自动导入当前整理好的照片和回忆数据

## 本地开发方式

如果你临时想分开跑前后端，也可以这样：

### 前端

```powershell
cd frontend
npm install
npm run dev
```

### 后端

先设置环境变量：

```powershell
$env:MYSQL_HOST="127.0.0.1"
$env:MYSQL_PORT="3306"
$env:MYSQL_DATABASE="xiaoke_daily"
$env:MYSQL_USERNAME="root"
$env:MYSQL_PASSWORD="你的数据库密码"
$env:ADMIN_INITIAL_USERNAME="admin"
$env:ADMIN_INITIAL_PASSWORD="你的后台密码"
```

然后启动：

```powershell
cd backend
mvn spring-boot:run
```

## 数据与图片

- 上传图片目录：`uploads/photos`
- SQL 初始化目录：`backend/sql`
- `101-130` 这批照片与手账文案已经补进 SQL，并可随 Docker 首次初始化导入

## Git 说明

仓库默认不会上传下面这些内容：

- `frontend/node_modules`
- `frontend/dist`
- `backend/target`
- `logs`
- `uploads/photos`
- `.env`

也就是说，公开仓库保留代码和部署方式，真实照片与密码请只保存在你自己的机器或服务器上。
