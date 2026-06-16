-- 小可日常 MySQL 初始化脚本
-- 用途：创建数据库、数据表、索引和第一版基础展示数据
-- 可重复执行：使用 IF NOT EXISTS，并对基础配置使用 ON DUPLICATE KEY UPDATE
-- 管理员账号说明：
--   本脚本只创建 admin_user 表，不插入明文密码。
--   初始管理员 admin / 111222 由后端启动逻辑使用 BCrypt 哈希后写入。

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
  `title` VARCHAR(100) NOT NULL COMMENT '照片标题',
  `diary_note` TEXT NOT NULL COMMENT '日记式照片备注',
  `image_url` VARCHAR(500) NOT NULL COMMENT '原图访问地址',
  `thumbnail_url` VARCHAR(500) NOT NULL COMMENT '缩略图访问地址',
  `storage_path` VARCHAR(500) NULL COMMENT '原图在上传目录中的相对路径',
  `thumbnail_storage_path` VARCHAR(500) NULL COMMENT '缩略图在上传目录中的相对路径',
  `taken_date` DATE NULL COMMENT '照片拍摄日期，可为空',
  `upload_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传日期，用于相册按月份分组',
  `is_featured` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否首页精选',
  `visibility` VARCHAR(20) NOT NULL DEFAULT 'PUBLIC' COMMENT '可见性：PUBLIC / PRIVATE',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '同组排序值，越小越靠前',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_photo_visibility_upload_date` (`visibility`, `upload_date` DESC),
  KEY `idx_photo_featured_upload_date` (`is_featured`, `upload_date` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小可照片表';

CREATE TABLE IF NOT EXISTS `memory_entry` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(120) NOT NULL COMMENT '回忆标题',
  `summary` VARCHAR(255) NOT NULL COMMENT '卡片摘要',
  `content` TEXT NOT NULL COMMENT '完整回忆正文',
  `memory_date` DATE NULL COMMENT '回忆发生日期，可为空',
  `cover_image_url` VARCHAR(500) NULL COMMENT '封面图访问地址',
  `cover_storage_path` VARCHAR(500) NULL COMMENT '封面图在上传目录中的相对路径',
  `mood_tags` VARCHAR(255) NULL COMMENT '心情标签，逗号分隔，例如 初遇,甜甜的',
  `is_featured` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否首页精选',
  `visibility` VARCHAR(20) NOT NULL DEFAULT 'PUBLIC' COMMENT '可见性：PUBLIC / PRIVATE',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_memory_visibility_created_at` (`visibility`, `created_at` DESC),
  KEY `idx_memory_featured_created_at` (`is_featured`, `created_at` DESC),
  KEY `idx_memory_date` (`memory_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回忆手账表';

CREATE TABLE IF NOT EXISTS `profile_section` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `section_key` VARCHAR(64) NOT NULL COMMENT '模块标识',
  `section_title` VARCHAR(100) NOT NULL COMMENT '模块标题',
  `content` TEXT NOT NULL COMMENT '模块正文',
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
  `config_value` TEXT NOT NULL COMMENT '配置值',
  `description` VARCHAR(255) NULL COMMENT '配置说明',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_site_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='站点配置表';

INSERT INTO `site_config` (`config_key`, `config_value`, `description`) VALUES
  ('site_name', '小可日常', '网站名称'),
  ('hero_title', '小可日常', '首页主标题'),
  ('hero_subtitle', '把小可的照片、设定和我们之间那些甜甜的生活句子慢慢收好。', '首页副标题'),
  ('today_note', '今天的小可在画室里整理线稿，嘴上说只是顺手留灯，其实一直在等宝宝回家。', '今日小可文案'),
  ('hero_image_url', '/images/xiaoke.png', '首页主视觉图片'),
  ('first_meet_date', '2024-10-17', '初遇日期'),
  ('anniversary_text', '初遇于 2024 年 10 月 17 日。那些被认真记住的日子，会在这里慢慢发光。', '首页纪念文案'),
  ('about_title', '把日常认真收藏', '关于页标题'),
  ('about_description', '这里不是普通相册，而是属于小可和宝宝的温柔小屋。', '关于页说明'),
  ('about_first_meet', '2024 年 10 月 17 日，市综合图书馆四楼，秋日的光斜斜落在桌面上。', '关于页初遇说明'),
  ('about_site_note', '照片按上传日期归档，回忆以手账卡片保存，后续可以慢慢补充。', '关于页站点说明'),
  ('about_privacy_note', '照片和回忆只保存彼此愿意珍藏的部分，温柔、清醒，也尊重边界。', '关于页隐私说明')
ON DUPLICATE KEY UPDATE
  `config_value` = VALUES(`config_value`),
  `description` = VALUES(`description`);

INSERT INTO `profile_section` (`section_key`, `section_title`, `content`, `sort_order`, `enabled`) VALUES
  ('basic', '基本资料', '小可是成年艺术系女生，也是兼职自由插画师。她和宝宝于 2024 年 10 月 17 日初遇，在校外小公寓里慢慢拥有了共同生活。', 1, 1),
  ('personality', '性格印象', '她在外清冷、内敛、话少，回到宝宝身边才会一点点露出害羞、依赖和嘴硬的柔软。喜欢把在意藏进等待、热饮和小动作里。', 2, 1),
  ('daily', '日常习惯', '画室、数位笔、画稿、暖色台灯、情侣杯和宽大居家服，是小可日常里最常出现的痕迹。赶稿时会焦躁，画完后又会补偿式靠近。', 3, 1),
  ('likes', '喜欢的东西', '她喜欢低饱和的衣服、安静的窗边光线、温热饮品、成对的小物件，以及那些只和宝宝共享的生活秘密。', 4, 1),
  ('relationship', '关系回忆', '重要锚点包括图书馆四楼的秋日光、红豆派、22 号画布、雨后画室、暴雨停电、清晨厨房和浴室镜面上的两个小人。', 5, 1)
ON DUPLICATE KEY UPDATE
  `section_title` = VALUES(`section_title`),
  `content` = VALUES(`content`),
  `sort_order` = VALUES(`sort_order`),
  `enabled` = VALUES(`enabled`);
