# Xiaoke Daily

`Xiaoke Daily` 是一个记录小可日常照片、回忆手账和人物档案的全栈网站。

## 技术栈

- 前端：Vue 3 + Vite + TypeScript
- 后端：Spring Boot
- 数据库：MySQL

## 项目结构

```text
xiaoke-daily/
├─ frontend/   # 前端项目
├─ backend/    # 后端项目
├─ uploads/    # 本地图片上传目录（默认不提交 Git）
└─ logs/       # 本地运行日志（默认不提交 Git）
```

## 运行前准备

公开仓库中不会保存真实密码。启动后端前，请先在运行环境里设置这些变量：

```powershell
$env:MYSQL_HOST="127.0.0.1"
$env:MYSQL_PORT="3306"
$env:MYSQL_DATABASE="xiaoke_daily"
$env:MYSQL_USERNAME="root"
$env:MYSQL_PASSWORD="你的数据库密码"
$env:ADMIN_INITIAL_USERNAME="admin"
$env:ADMIN_INITIAL_PASSWORD="你的管理员密码"
```

可选变量：

```powershell
$env:APP_UPLOAD_DIR="../uploads"
$env:APP_PUBLIC_UPLOAD_BASE_URL="/uploads"
```

## 启动项目

### 前端

```powershell
cd frontend
npm install
npm run dev
```

### 后端

```powershell
cd backend
mvn spring-boot:run
```

## Git 说明

这个仓库默认不会上传下面这些内容：

- `frontend/node_modules`
- `frontend/dist`
- `backend/target`
- `backend/data`
- `logs`
- `uploads/photos`

也就是说，公开 GitHub 仓库里只保留代码和运行说明，照片与本地数据请单独保存。
