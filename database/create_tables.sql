-- =============================================
-- Drools员工标签系统数据库建表语句
-- 数据库版本: MySQL 5.7
-- 创建时间: 2024-01-15
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `employee_tagging` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `employee_tagging`;

-- =============================================
-- 1. 员工信息表
-- =============================================
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_no` varchar(50) NOT NULL COMMENT '员工编号',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `hire_date` date DEFAULT NULL COMMENT '入职日期',
  
  -- 能力素质字段
  `technical_skills` text COMMENT '技术技能(JSON格式)',
  `soft_skills` text COMMENT '软技能(JSON格式)',
  `leadership_score` int(11) DEFAULT 0 COMMENT '领导力评分(1-100)',
  `communication_score` int(11) DEFAULT 0 COMMENT '沟通能力评分(1-100)',
  `innovation_score` int(11) DEFAULT 0 COMMENT '创新能力评分(1-100)',
  `learning_ability` int(11) DEFAULT 0 COMMENT '学习能力评分(1-100)',
  `certifications` text COMMENT '认证证书(JSON格式)',
  
  -- 履历信息字段
  `total_work_years` int(11) DEFAULT 0 COMMENT '总工作年限',
  `education` varchar(50) DEFAULT NULL COMMENT '最高学历',
  `graduate_school` varchar(200) DEFAULT NULL COMMENT '毕业院校',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `industries` text COMMENT '行业经验(JSON格式)',
  `previous_positions` text COMMENT '历任职位(JSON格式)',
  `management_years` int(11) DEFAULT 0 COMMENT '管理年限',
  `team_size` int(11) DEFAULT 0 COMMENT '管理团队规模',
  
  -- 考评信息字段
  `current_performance_score` int(11) DEFAULT 0 COMMENT '当前绩效分数',
  `performance_level` varchar(10) DEFAULT NULL COMMENT '绩效等级(A/B/C/D)',
  `performance_history` text COMMENT '历史绩效(JSON格式)',
  `potential_rating` int(11) DEFAULT 0 COMMENT '潜力评级(1-5)',
  `competency_scores` text COMMENT '胜任力评分(JSON格式)',
  `team_collaboration_score` int(11) DEFAULT 0 COMMENT '团队协作评分',
  `growth_trend` varchar(20) DEFAULT NULL COMMENT '成长趋势(上升/稳定/下降)',
  `last_evaluation_date` date DEFAULT NULL COMMENT '最近考评日期',
  `strength_areas` text COMMENT '优势领域(JSON格式)',
  `improvement_areas` text COMMENT '改进领域(JSON格式)',
  
  -- 系统字段
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除(0-否,1-是)',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_no` (`employee_no`),
  KEY `idx_department` (`department`),
  KEY `idx_position` (`position`),
  KEY `idx_performance_level` (`performance_level`),
  KEY `idx_education` (`education`),
  KEY `idx_hire_date` (`hire_date`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

-- =============================================
-- 2. 标签库表
-- =============================================
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(100) NOT NULL COMMENT '标签编码',
  `name` varchar(200) NOT NULL COMMENT '标签名称',
  `description` text COMMENT '标签描述',
  `category` varchar(100) NOT NULL COMMENT '标签分类',
  `dimension` varchar(50) NOT NULL COMMENT '所属维度(ABILITY/EXPERIENCE/EVALUATION)',
  `priority` int(11) DEFAULT 5 COMMENT '优先级(1-10)',
  `weight` decimal(3,2) DEFAULT 1.00 COMMENT '权重(0.1-1.0)',
  `color` varchar(20) DEFAULT NULL COMMENT '标签颜色',
  `is_active` tinyint(1) DEFAULT 1 COMMENT '是否启用(0-否,1-是)',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_code` (`code`),
  KEY `idx_dimension` (`dimension`),
  KEY `idx_category` (`category`),
  KEY `idx_priority` (`priority`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签库表';

-- =============================================
-- 3. 员工标签关联表
-- =============================================
DROP TABLE IF EXISTS `employee_tag`;
CREATE TABLE `employee_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工ID',
  `tag_code` varchar(100) NOT NULL COMMENT '标签编码',
  `tag_name` varchar(200) NOT NULL COMMENT '标签名称',
  `category` varchar(100) NOT NULL COMMENT '标签分类',
  `dimension` varchar(50) NOT NULL COMMENT '所属维度',
  `confidence` decimal(3,2) DEFAULT 0.80 COMMENT '置信度(0.0-1.0)',
  `source` varchar(50) NOT NULL COMMENT '标签来源(RULE_ENGINE/MANUAL/SYSTEM)',
  `reason` text COMMENT '打标签原因',
  `assigned_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `is_active` tinyint(1) DEFAULT 1 COMMENT '是否有效(0-否,1-是)',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_tag_code` (`tag_code`),
  KEY `idx_dimension` (`dimension`),),
  KEY `idx_tag_code` (`tag_code`),
  KEY `idx_dimension` (`dimension`),
  KEY `idx_source` (`source`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_assigned_time` (`assigned_time`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工标签关联表';

-- =============================================
-- 4. 标签规则表
-- =============================================
DROP TABLE IF EXISTS `tag_rule`;
CREATE TABLE `tag_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_code` varchar(100) NOT NULL COMMENT '规则编码',
  `rule_name` varchar(200) NOT NULL COMMENT '规则名称',
  `tag_code` varchar(100) NOT NULL COMMENT '关联的标签编码',
  `rule_content` longtext COMMENT '规则内容(DRL格式)',
  `rule_template` varchar(100) NOT NULL COMMENT '规则模板类型',
  `conditions` text COMMENT '条件配置(JSON格式)',
  `description` text COMMENT '规则描述',
  `priority` int(11) DEFAULT 5 COMMENT '规则优先级(1-10)',
  `is_active` tinyint(1) DEFAULT 1 COMMENT '是否启用(0-否,1-是)',
  `version` varchar(20) DEFAULT '1.0' COMMENT '规则版本',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_executed_time` datetime DEFAULT NULL COMMENT '最后执行时间',
  `execution_count` bigint(20) DEFAULT 0 COMMENT '执行次数',
  `test_cases` text COMMENT '测试用例(JSON格式)',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`),
  KEY `idx_tag_code` (`tag_code`),
  KEY `idx_rule_template` (`rule_template`),
  KEY `idx_priority` (`priority`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签规则表';

-- =============================================
-- 5. 项目经验表
-- =============================================
DROP TABLE IF EXISTS `project_experience`;
CREATE TABLE `project_experience` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工ID',
  `project_name` varchar(200) NOT NULL COMMENT '项目名称',
  `role` varchar(100) DEFAULT NULL COMMENT '项目角色',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `duration_months` int(11) DEFAULT NULL COMMENT '项目时长(月)',
  `technology` text COMMENT '使用技术',
  `achievement` text COMMENT '项目成果',
  `description` text COMMENT '项目描述',
  `project_scale` varchar(50) DEFAULT NULL COMMENT '项目规模',
  `team_size` int(11) DEFAULT NULL COMMENT '团队规模',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_project_name` (`project_name`),
  KEY `idx_role` (`role`),
  KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目经验表';

-- =============================================
-- 6. 标签变更历史表
-- =============================================
DROP TABLE IF EXISTS `tag_change_history`;
CREATE TABLE `tag_change_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) NOT NULL COMMENT '员工ID',
  `tag_code` varchar(100) NOT NULL COMMENT '标签编码',
  `operation_type` varchar(20) NOT NULL COMMENT '操作类型(ADD/REMOVE/UPDATE)',
  `old_value` text COMMENT '旧值(JSON格式)',
  `new_value` text COMMENT '新值(JSON格式)',
  `reason` text COMMENT '变更原因',
  `operator` varchar(100) DEFAULT NULL COMMENT '操作人',
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_tag_code` (`tag_code`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签变更历史表';

-- =============================================
-- 7. 系统配置表
-- =============================================
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(50) DEFAULT 'STRING' COMMENT '配置类型',
  `description` varchar(500) DEFAULT NULL COMMENT '配置描述',
  `is_active` tinyint(1) DEFAULT 1 COMMENT '是否启用',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- =============================================
-- 初始化基础数据
-- =============================================

-- 插入标签库基础数据
INSERT INTO `tag` (`code`, `name`, `description`, `category`, `dimension`, `priority`, `weight`, `color`) VALUES
-- 能力素质类标签
('TECH_EXPERT', '技术专家', '在特定技术领域有深入专业知识', 'TECHNICAL_ABILITY', 'ABILITY', 9, 0.90, '#FF6B6B'),
('TECH_SENIOR', '高级技术人员', '技术能力强，经验丰富', 'TECHNICAL_ABILITY', 'ABILITY', 8, 0.80, '#4ECDC4'),
('TECH_INTERMEDIATE', '中级技术人员', '技术能力中等，有一定经验', 'TECHNICAL_ABILITY', 'ABILITY', 6, 0.60, '#45B7D1'),
('TECH_JUNIOR', '初级技术人员', '技术基础扎实，需要指导', 'TECHNICAL_ABILITY', 'ABILITY', 4, 0.40, '#96CEB4'),
('FULLSTACK_DEV', '全栈开发者', '前后端技术都很熟练', 'TECHNICAL_ABILITY', 'ABILITY', 8, 0.80, '#FFEAA7'),
('FRONTEND_EXPERT', '前端专家', '前端技术专家', 'TECHNICAL_ABILITY', 'ABILITY', 8, 0.80, '#DDA0DD'),
('BACKEND_EXPERT', '后端专家', '后端技术专家', 'TECHNICAL_ABILITY', 'ABILITY', 8, 0.80, '#98D8C8'),

('STRONG_LEADER', '强领导力', '具备优秀的领导和管理能力', 'LEADERSHIP', 'ABILITY', 9, 0.90, '#FF7675'),
('POTENTIAL_LEADER', '潜在领导者', '具备领导潜质', 'LEADERSHIP', 'ABILITY', 7, 0.70, '#FD79A8'),
('TEAM_PLAYER', '团队协作者', '善于团队合作', 'LEADERSHIP', 'ABILITY', 6, 0.60, '#FDCB6E'),

('EXCELLENT_COMM', '沟通专家', '沟通能力出色', 'COMMUNICATION', 'ABILITY', 8, 0.80, '#6C5CE7'),
('GOOD_COMM', '沟通良好', '沟通能力较好', 'COMMUNICATION', 'ABILITY', 6, 0.60, '#A29BFE'),

('INNOVATOR', '创新者', '具备创新思维和能力', 'INNOVATION', 'ABILITY', 9, 0.90, '#00B894'),
('CREATIVE_THINKER', '创意思考者', '思维活跃，有创意', 'INNOVATION', 'ABILITY', 7, 0.70, '#55A3FF'),

-- 履历类标签
('VETERAN_10PLUS', '资深专家(10年+)', '工作经验10年以上', 'EXPERIENCE_LEVEL', 'EXPERIENCE', 9, 0.90, '#E17055'),
('SENIOR_5TO10', '高级人员(5-10年)', '工作经验5-10年', 'EXPERIENCE_LEVEL', 'EXPERIENCE', 8, 0.80, '#F39C12'),
('MID_3TO5', '中级人员(3-5年)', '工作经验3-5年', 'EXPERIENCE_LEVEL', 'EXPERIENCE', 6, 0.60, '#F1C40F'),
('JUNIOR_1TO3', '初级人员(1-3年)', '工作经验1-3年', 'EXPERIENCE_LEVEL', 'EXPERIENCE', 4, 0.40, '#2ECC71'),
('FRESH_GRAD', '应届毕业生', '工作经验不足1年', 'EXPERIENCE_LEVEL', 'EXPERIENCE', 2, 0.20, '#3498DB'),

('PHD_DEGREE', '博士学历', '具有博士学位', 'EDUCATION_BACKGROUND', 'EXPERIENCE', 9, 0.90, '#9B59B6'),
('MASTER_DEGREE', '硕士学历', '具有硕士学位', 'EDUCATION_BACKGROUND', 'EXPERIENCE', 7, 0.70, '#8E44AD'),
('BACHELOR_DEGREE', '本科学历', '具有本科学位', 'EDUCATION_BACKGROUND', 'EXPERIENCE', 5, 0.50, '#3F51B5'),
('TOP_UNIVERSITY', '名校毕业', '985/211等知名院校毕业', 'EDUCATION_BACKGROUND', 'EXPERIENCE', 8, 0.80, '#E91E63'),

('MULTI_INDUSTRY', '跨行业经验', '有多个行业工作经验', 'INDUSTRY_EXPERIENCE', 'EXPERIENCE', 8, 0.80, '#FF9800'),
('FINTECH_EXP', '金融科技经验', '有金融科技行业经验', 'INDUSTRY_EXPERIENCE', 'EXPERIENCE', 7, 0.70, '#795548'),
('ECOMMERCE_EXP', '电商经验', '有电商行业经验', 'INDUSTRY_EXPERIENCE', 'EXPERIENCE', 7, 0.70, '#607D8B'),

('LARGE_PROJECT', '大型项目经验', '参与过大型项目', 'PROJECT_EXPERIENCE', 'EXPERIENCE', 8, 0.80, '#FF5722'),
('STARTUP_EXP', '创业经验', '有创业或初创公司经验', 'PROJECT_EXPERIENCE', 'EXPERIENCE', 8, 0.80, '#009688'),

-- 考评类标签
('TOP_PERFORMER', '顶尖绩效', '绩效评级A+', 'PERFORMANCE_LEVEL', 'EVALUATION', 10, 1.00, '#C0392B'),
('HIGH_PERFORMER', '高绩效', '绩效评级A', 'PERFORMANCE_LEVEL', 'EVALUATION', 9, 0.90, '#E74C3C'),
('GOOD_PERFORMER', '良好绩效', '绩效评级B', 'PERFORMANCE_LEVEL', 'EVALUATION', 7, 0.70, '#F39C12'),
('AVERAGE_PERFORMER', '一般绩效', '绩效评级C', 'PERFORMANCE_LEVEL', 'EVALUATION', 5, 0.50, '#F1C40F'),
('NEEDS_IMPROVEMENT', '需要改进', '绩效评级D', 'PERFORMANCE_LEVEL', 'EVALUATION', 3, 0.30, '#95A5A6'),

('HIGH_POTENTIAL', '高潜力', '潜力评估为高', 'POTENTIAL_ASSESSMENT', 'EVALUATION', 9, 0.90, '#8E44AD'),
('MEDIUM_POTENTIAL', '中等潜力', '潜力评估为中等', 'POTENTIAL_ASSESSMENT', 'EVALUATION', 6, 0.60, '#3498DB'),
('RISING_STAR', '新星', '快速成长的员工', 'POTENTIAL_ASSESSMENT', 'EVALUATION', 8, 0.80, '#E67E22'),

('COMPETENT_ALL', '全面胜任', '各项胜任力都达标', 'COMPETENCY_RATING', 'EVALUATION', 8, 0.80, '#27AE60'),
('COMPETENT_PARTIAL', '部分胜任', '部分胜任力达标', 'COMPETENCY_RATING', 'EVALUATION', 6, 0.60, '#F39C12'),

('EXCELLENT_COLLAB', '协作优秀', '团队协作能力优秀', 'TEAM_COLLABORATION', 'EVALUATION', 9, 0.90, '#16A085'),
('GOOD_COLLAB', '协作良好', '团队协作能力良好', 'TEAM_COLLABORATION', 'EVALUATION', 7, 0.70, '#1ABC9C'),

('RAPID_GROWTH', '快速成长', '成长趋势向上', 'GROWTH_TREND', 'EVALUATION', 8, 0.80, '#2ECC71'),
('STABLE_GROWTH', '稳定成长', '成长趋势稳定', 'GROWTH_TREND', 'EVALUATION', 6, 0.60, '#3498DB'),
('SLOW_GROWTH', '成长缓慢', '成长趋势较慢', 'GROWTH_TREND', 'EVALUATION', 4, 0.40, '#95A5A6');

-- 插入系统配置数据
INSERT INTO `system_config` (`config_key`, `config_value`, `config_type`, `description`) VALUES
('tag.auto.expire.enabled', 'true', 'BOOLEAN', '是否启用标签自动过期'),
('tag.expire.check.interval', '3600000', 'INTEGER', '标签过期检查间隔(毫秒)'),
('rule.max.execution.time', '30000', 'INTEGER', '规则最大执行时间(毫秒)'),
('system.batch.size', '100', 'INTEGER', '批处理大小'),
('tag.confidence.threshold', '0.6', 'DECIMAL', '标签置信度阈值'),
('rule.auto.compile', 'true', 'BOOLEAN', '是否自动编译规则');

-- =============================================
-- 创建视图
-- =============================================

-- 员工标签统计视图
CREATE VIEW `v_employee_tag_stats` AS
SELECT 
    e.id as employee_id,
    e.employee_no,
    e.name,
    e.department,
    e.position,
    COUNT(et.id) as total_tags,
    COUNT(CASE WHEN et.dimension = 'ABILITY' THEN 1 END) as ability_tags,
    COUNT(CASE WHEN et.dimension = 'EXPERIENCE' THEN 1 END) as experience_tags,
    COUNT(CASE WHEN et.dimension = 'EVALUATION' THEN 1 END) as evaluation_tags,
    AVG(et.confidence) as avg_confidence
FROM employee e
LEFT JOIN employee_tag et ON e.id = et.employee_id AND et.is_active = 1
WHERE e.is_deleted = 0
GROUP BY e.id, e.employee_no, e.name, e.department, e.position;

-- 标签使用统计视图
CREATE VIEW `v_tag_usage_stats` AS
SELECT 
    t.code,
    t.name,
    t.dimension,
    t.category,
    COUNT(et.id) as usage_count,
    AVG(et.confidence) as avg_confidence,
    MAX(et.assigned_time) as last_used_time
FROM tag t
LEFT JOIN employee_tag et ON t.code = et.tag_code AND et.is_active = 1
WHERE t.is_active = 1
GROUP BY t.code, t.name, t.dimension, t.category;

-- =============================================
-- 创建存储过程
-- =============================================

-- 清理过期标签存储过程
DELIMITER $$
CREATE PROCEDURE `sp_clean_expired_tags`()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE tag_id BIGINT;
    DECLARE cur CURSOR FOR 
        SELECT id FROM employee_tag 
        WHERE is_active = 1 AND expire_time IS NOT NULL AND expire_time < NOW();
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    START TRANSACTION;
    
    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO tag_id;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 标记标签为失效
        UPDATE employee_tag SET is_active = 0, updated_time = NOW() WHERE id = tag_id;
        
        -- 记录变更历史
        INSERT INTO tag_change_history (employee_id, tag_code, operation_type, reason, operation_time)
        SELECT employee_id, tag_code, 'REMOVE', '标签过期自动失效', NOW()
        FROM employee_tag WHERE id = tag_id;
        
    END LOOP;
    CLOSE cur;
    
    COMMIT;
    
    SELECT ROW_COUNT() as affected_rows;
END$$
DELIMITER ;

-- 员工标签统计存储过程
DELIMITER $$
CREATE PROCEDURE `sp_employee_tag_statistics`(IN emp_id BIGINT)
BEGIN
    SELECT 
        dimension,
        COUNT(*) as tag_count,
        AVG(confidence) as avg_confidence,
        GROUP_CONCAT(tag_name ORDER BY confidence DESC) as tag_names
    FROM employee_tag 
    WHERE employee_id = emp_id AND is_active = 1
    GROUP BY dimension
    ORDER BY tag_count DESC;
END$$
DELIMITER ;

-- =============================================
-- 创建触发器
-- =============================================

-- 员工标签变更触发器
DELIMITER $$
CREATE TRIGGER `tr_employee_tag_change` 
AFTER UPDATE ON `employee_tag`
FOR EACH ROW
BEGIN
    IF OLD.is_active != NEW.is_active THEN
        INSERT INTO tag_change_history (
            employee_id, tag_code, operation_type, 
            old_value, new_value, reason, operation_time
        ) VALUES (
            NEW.employee_id, NEW.tag_code, 
            CASE WHEN NEW.is_active = 1 THEN 'ADD' ELSE 'REMOVE' END,
            CONCAT('{"is_active":', OLD.is_active, ',"confidence":', OLD.confidence, '}'),
            CONCAT('{"is_active":', NEW.is_active, ',"confidence":', NEW.confidence, '}'),
            '系统自动记录', NOW()
        );
    END IF;
END$$
DELIMITER ;

-- =============================================
-- 创建定时任务(需要开启事件调度器)
-- =============================================

-- 开启事件调度器
SET GLOBAL event_scheduler = ON;

-- 创建清理过期标签的定时任务
CREATE EVENT IF NOT EXISTS `evt_clean_expired_tags`
ON SCHEDULE EVERY 1 HOUR
STARTS CURRENT_TIMESTAMP
DO
  CALL sp_clean_expired_tags();

-- =============================================
-- 插入测试数据
-- =============================================

-- 插入测试员工数据
INSERT INTO `employee` (
    `employee_no`, `name`, `age`, `gender`, `department`, `position`, `hire_date`,
    `technical_skills`, `leadership_score`, `communication_score`, `innovation_score`, `learning_ability`,
    `total_work_years`, `education`, `graduate_school`, `major`, `management_years`, `team_size`,
    `current_performance_score`, `performance_level`, `potential_rating`, `team_collaboration_score`, `growth_trend`
) VALUES 
('EMP001', '张技术专家', 32, '男', '技术部', '高级架构师', '2015-03-15',
 '["Java", "Python", "微服务", "分布式", "数据库", "Redis"]', 88, 85, 90, 92,
 9, '硕士', '清华大学', '计算机科学', 3, 8,
 92, 'A', 5, 90, '上升'),

('EMP002', '李前端专家', 28, '女', '技术部', '前端架构师', '2018-06-01',
 '["JavaScript", "React", "Vue", "CSS", "TypeScript", "Webpack"]', 75, 88, 85, 90,
 6, '本科', '北京大学', '软件工程', 1, 4,
 88, 'A', 4, 92, '上升'),

('EMP003', '王初级开发', 24, '男', '技术部', 'Java开发工程师', '2023-07-01',
 '["Java", "Spring", "MySQL"]', 60, 70, 75, 88,
 1, '本科', '华中科技大学', '计算机科学', 0, 0,
 78, 'B', 4, 85, '上升');

-- 插入项目经验数据
INSERT INTO `project_experience` (
    `employee_id`, `project_name`, `role`, `duration_months`, `technology`, `achievement`
) VALUES 
(1, '分布式支付系统', '技术负责人', 18, 'Java+微服务', '系统性能提升300%'),
(1, '大数据平台', '架构师', 12, 'Python+Spark', '处理能力提升10倍'),
(2, '电商前端重构', '前端负责人', 8, 'React+TypeScript', '页面加载速度提升50%'),
(3, '用户管理系统', '开发工程师', 6, 'Java+Spring Boot', '完成核心功能开发');

-- 查看表结构和数据
SHOW TABLES;
SELECT COUNT(*) as tag_count FROM tag;
SELECT COUNT(*) as employee_count FROM employee;

-- =============================================
-- 建表语句执行完成
-- =============================================
