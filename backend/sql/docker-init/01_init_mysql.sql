-- 小可日常 MySQL 初始化脚本
-- 用途：创建数据库与表结构，不负责导入业务展示内容。
-- 展示内容由 content_seed.sql 或手动导入脚本补充。

CREATE DATABASE IF NOT EXISTS `xiaoke_daily`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE `xiaoke_daily`;

CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(64) NOT NULL COMMENT '管理员用户名',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '管理员密码哈希，后端使用 BCrypt 写入',
  `nickname` VARCHAR(64) NOT NULL DEFAULT '管理员' COMMENT '后台显示昵称',
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：1 启用，0 禁用',
  `last_login_at` DATETIME NULL COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员账号表';

CREATE TABLE IF NOT EXISTS `photo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '照片标题',
  `diary_note` VARCHAR(1000) DEFAULT NULL COMMENT '日记式照片备注',
  `image_url` VARCHAR(255) DEFAULT NULL COMMENT '原图访问地址',
  `thumbnail_url` VARCHAR(255) DEFAULT NULL COMMENT '缩略图访问地址',
  `storage_path` VARCHAR(255) DEFAULT NULL COMMENT '原图存储相对路径',
  `thumbnail_storage_path` VARCHAR(255) DEFAULT NULL COMMENT '缩略图存储相对路径',
  `taken_date` DATE DEFAULT NULL COMMENT '照片拍摄日期，可为空',
  `upload_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传日期',
  `is_featured` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否首页精选',
  `visibility` VARCHAR(255) DEFAULT NULL COMMENT '可见性',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_photo_visibility_upload_date` (`visibility`, `upload_date` DESC),
  KEY `idx_photo_featured_upload_date` (`is_featured`, `upload_date` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小可照片表';

CREATE TABLE IF NOT EXISTS `memory_entry` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '回忆标题',
  `summary` VARCHAR(255) NOT NULL COMMENT '卡片摘要',
  `content` VARCHAR(6000) DEFAULT NULL COMMENT '完整回忆正文',
  `memory_date` DATE DEFAULT NULL COMMENT '回忆发生日期，可为空',
  `cover_image_url` VARCHAR(255) DEFAULT NULL COMMENT '封面图访问地址',
  `cover_storage_path` VARCHAR(255) DEFAULT NULL COMMENT '封面图存储相对路径',
  `mood_tags` VARCHAR(255) DEFAULT NULL COMMENT '心情标签，逗号分隔',
  `is_featured` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否首页精选',
  `visibility` VARCHAR(255) DEFAULT NULL COMMENT '可见性',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `favorite_count` INT DEFAULT NULL COMMENT '收藏数',
  PRIMARY KEY (`id`),
  KEY `idx_memory_visibility_created_at` (`visibility`, `created_at` DESC),
  KEY `idx_memory_featured_created_at` (`is_featured`, `created_at` DESC),
  KEY `idx_memory_date` (`memory_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回忆手账表';

CREATE TABLE IF NOT EXISTS `profile_section` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `section_key` VARCHAR(255) DEFAULT NULL COMMENT '模块标识',
  `section_title` VARCHAR(255) DEFAULT NULL COMMENT '模块标题',
  `content` VARCHAR(4000) DEFAULT NULL COMMENT '模块正文',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示顺序，越小越靠前',
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否展示',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_profile_section_key` (`section_key`),
  KEY `idx_profile_enabled_sort` (`enabled`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小可档案模块表';

CREATE TABLE IF NOT EXISTS `site_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` VARCHAR(4000) NOT NULL COMMENT '配置值',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '配置说明',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_site_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='站点配置表';
