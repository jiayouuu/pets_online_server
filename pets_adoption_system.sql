-- 宠物领养系统数据库设计
-- 作者: AI Generated
-- 版本: 1.0

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 创建数据库
-- ----------------------------
CREATE DATABASE IF NOT EXISTS pets_adoption_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE pets_adoption_system;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色：USER, DOCTOR, ADMIN',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0-未验证，1-正常, 2-封禁',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 邮箱验证表
-- ----------------------------
DROP TABLE IF EXISTS `email_verification`;
CREATE TABLE `email_verification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `token` varchar(100) NOT NULL COMMENT '验证token',
  `type` varchar(20) NOT NULL COMMENT '类型：REGISTER, RESET_PASSWORD',
  `is_used` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已使用',
  `expired_at` datetime NOT NULL COMMENT '过期时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮箱验证表';

-- ----------------------------
-- 宠物表
-- ----------------------------
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '宠物ID',
  `name` varchar(50) NOT NULL COMMENT '宠物名称',
  `species` varchar(50) NOT NULL COMMENT '物种（猫/狗等）',
  `breed` varchar(50) NOT NULL COMMENT '品种',
  `age` int(11) NOT NULL COMMENT '年龄（月）',
  `gender` varchar(10) NOT NULL COMMENT '性别',
  `weight` decimal(5,2) DEFAULT NULL COMMENT '体重（kg）',
  `color` varchar(50) DEFAULT NULL COMMENT '毛色',
  `location` varchar(100) NOT NULL COMMENT '所在地',
  `health_status` varchar(50) NOT NULL COMMENT '健康状态',
  `character_description` text DEFAULT NULL COMMENT '性格描述',
  `is_sterilized` tinyint(1) DEFAULT NULL COMMENT '是否绝育',
  `is_vaccinated` tinyint(1) DEFAULT NULL COMMENT '是否接种疫苗',
  `status` varchar(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '状态：AVAILABLE, ADOPTED, PENDING',
  `created_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_location` (`location`),
  KEY `idx_breed` (`breed`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物表';

-- ----------------------------
-- 宠物图片表
-- ----------------------------
DROP TABLE IF EXISTS `pet_image`;
CREATE TABLE `pet_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pet_id` bigint(20) NOT NULL COMMENT '宠物ID',
  `url` varchar(255) NOT NULL COMMENT '图片URL',
  `is_cover` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否封面',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物图片表';

-- ----------------------------
-- 宠物点赞收藏表
-- ----------------------------
DROP TABLE IF EXISTS `pet_user_relation`;
CREATE TABLE `pet_user_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pet_id` bigint(20) NOT NULL COMMENT '宠物ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `type` varchar(20) NOT NULL COMMENT '类型：LIKE, FAVORITE',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pet_user_type` (`pet_id`,`user_id`,`type`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物点赞收藏表';

-- ----------------------------
-- 领养申请表
-- ----------------------------
DROP TABLE IF EXISTS `adoption_application`;
CREATE TABLE `adoption_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `pet_id` bigint(20) NOT NULL COMMENT '宠物ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `environment` text NOT NULL COMMENT '家庭环境',
  `experience` text NOT NULL COMMENT '养宠经验',
  `signature` text NOT NULL COMMENT '电子签名数据',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING, APPROVED, REJECTED, CANCELED, COMPLETED',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `admin_comment` text DEFAULT NULL COMMENT '管理员备注',
  `reject_reason` text DEFAULT NULL COMMENT '拒绝原因',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_pet_id` (`pet_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养申请表';

-- ----------------------------
-- 物流信息表
-- ----------------------------
DROP TABLE IF EXISTS `logistics`;
CREATE TABLE `logistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `application_id` bigint(20) NOT NULL COMMENT '申请ID',
  `tracking_number` varchar(100) DEFAULT NULL COMMENT '物流单号',
  `logistics_company` varchar(50) DEFAULT NULL COMMENT '物流公司',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING, SHIPPING, DELIVERED, COMPLETED',
  `current_location` varchar(100) DEFAULT NULL COMMENT '当前位置',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application_id` (`application_id`),
  KEY `idx_tracking_number` (`tracking_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

-- ----------------------------
-- 物流轨迹表
-- ----------------------------
DROP TABLE IF EXISTS `logistics_track`;
CREATE TABLE `logistics_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `logistics_id` bigint(20) NOT NULL COMMENT '物流ID',
  `location` varchar(100) NOT NULL COMMENT '位置',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `track_time` datetime NOT NULL COMMENT '轨迹时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_logistics_id` (`logistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';

-- ----------------------------
-- 帖子表
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `category` varchar(50) NOT NULL COMMENT '分类：ADOPTION, EXPERIENCE, HELP',
  `pet_id` bigint(20) DEFAULT NULL COMMENT '关联宠物ID（可选）',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING, PUBLISHED, REJECTED',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- ----------------------------
-- 帖子图片表
-- ----------------------------
DROP TABLE IF EXISTS `post_image`;
CREATE TABLE `post_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `url` varchar(255) NOT NULL COMMENT '图片URL',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子图片表';

-- ----------------------------
-- 帖子点赞收藏表
-- ----------------------------
DROP TABLE IF EXISTS `post_user_relation`;
CREATE TABLE `post_user_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `type` varchar(20) NOT NULL COMMENT '类型：LIKE, FAVORITE',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user_type` (`post_id`,`user_id`,`type`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞收藏表';

-- ----------------------------
-- 评论表
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '内容',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父评论ID',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- 评论点赞表
-- ----------------------------
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `comment_id` bigint(20) NOT NULL COMMENT '评论ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';

-- ----------------------------
-- 举报表
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '举报人ID',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID',
  `target_type` varchar(20) NOT NULL COMMENT '目标类型：PET, POST, COMMENT',
  `reason` varchar(100) NOT NULL COMMENT '原因',
  `description` text DEFAULT NULL COMMENT '描述',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING, PROCESSED, REJECTED',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(255) DEFAULT NULL COMMENT '处理结果',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_target_id_type` (`target_id`,`target_type`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';

-- ----------------------------
-- 敏感词表
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `word` varchar(50) NOT NULL COMMENT '敏感词',
  `category` varchar(20) NOT NULL COMMENT '分类',
  `level` tinyint(1) NOT NULL DEFAULT 1 COMMENT '级别：1-一般, 2-中度, 3-严重',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词表';

-- ----------------------------
-- 在线问诊表
-- ----------------------------
DROP TABLE IF EXISTS `consultation`;
CREATE TABLE `consultation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '咨询ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` text NOT NULL COMMENT '描述',
  `doctor_id` bigint(20) DEFAULT NULL COMMENT '医生ID',
  `status` varchar(20) NOT NULL DEFAULT 'WAITING' COMMENT '状态：WAITING, PROCESSING, COMPLETED, CANCELED',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doctor_id` (`doctor_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线问诊表';

-- ----------------------------
-- 问诊图片表
-- ----------------------------
DROP TABLE IF EXISTS `consultation_image`;
CREATE TABLE `consultation_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `consultation_id` bigint(20) NOT NULL COMMENT '咨询ID',
  `url` varchar(255) NOT NULL COMMENT '图片URL',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_consultation_id` (`consultation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问诊图片表';

-- ----------------------------
-- 问诊消息表
-- ----------------------------
DROP TABLE IF EXISTS `consultation_message`;
CREATE TABLE `consultation_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `consultation_id` bigint(20) NOT NULL COMMENT '咨询ID',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者ID',
  `content` text NOT NULL COMMENT '内容',
  `type` varchar(20) NOT NULL DEFAULT 'TEXT' COMMENT '类型：TEXT, IMAGE, PRESCRIPTION',
  `attachment` varchar(255) DEFAULT NULL COMMENT '附件URL',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_consultation_id` (`consultation_id`),
  KEY `idx_sender_id` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问诊消息表';

-- ----------------------------
-- 系统通知表
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `type` varchar(50) NOT NULL COMMENT '类型',
  `target_id` bigint(20) DEFAULT NULL COMMENT '目标ID',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

-- ----------------------------
-- 用户统计表
-- ----------------------------
DROP TABLE IF EXISTS `user_stats`;
CREATE TABLE `user_stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_count` int(11) NOT NULL DEFAULT 0 COMMENT '登录次数',
  `post_count` int(11) NOT NULL DEFAULT 0 COMMENT '发帖数',
  `comment_count` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `application_count` int(11) NOT NULL DEFAULT 0 COMMENT '申请数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户统计表';

-- ----------------------------
-- 系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` text NOT NULL COMMENT '配置值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- 初始化系统配置
-- ----------------------------
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('SITE_NAME', '宠物领养系统', '站点名称'),
('SITE_DESC', '为流浪动物提供一个温暖的家', '站点描述'),
('ADMIN_EMAIL', 'admin@petadoption.com', '管理员邮箱'),
('EMAIL_VERIFICATION_EXPIRE', '24', '邮箱验证有效期(小时)'),
('ADOPTION_AUTO_CANCEL', '72', '领养申请自动取消时间(小时)'),
('SENSITIVE_WORD_ENABLE', 'true', '是否启用敏感词过滤'),
('POST_AUDIT_AUTO', 'true', '是否开启帖子自动审核');

-- ----------------------------
-- 初始化管理员账号
-- ----------------------------
INSERT INTO `user` (`email`, `password`, `nickname`, `role`, `status`) VALUES
('admin@petadoption.com', '$2a$10$dYRcVBgQcWzKCT3ZhZ.UB.XGCTj.bQJJJ/QQMeNOiVmJv8tkJGJxG', '系统管理员', 'ADMIN', 1);

SET FOREIGN_KEY_CHECKS = 1; 