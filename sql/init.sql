-- 员工团队建设活动管理系统 - 数据库脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS activity_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE activity_system;

-- 1. 用户表（员工和管理员）
CREATE TABLE IF NOT EXISTS sys_user (
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    dept VARCHAR(100) NOT NULL COMMENT '部门',
    position VARCHAR(50) COMMENT '职位',
    role VARCHAR(10) NOT NULL DEFAULT 'user' COMMENT '角色: admin管理员, user普通员工',
    status VARCHAR(10) DEFAULT '1' COMMENT '状态: 1正常, 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    dept_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (dept_id),
    UNIQUE KEY uk_dept_name (dept_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 3. 活动表
CREATE TABLE IF NOT EXISTS activity (
    activity_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    activity_name VARCHAR(200) NOT NULL COMMENT '活动名称',
    activity_type VARCHAR(50) COMMENT '活动类型',
    location VARCHAR(200) COMMENT '活动地点',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    description TEXT COMMENT '活动描述',
    max_participants INT(11) DEFAULT 50 COMMENT '最大参与人数',
    current_participants INT(11) DEFAULT 0 COMMENT '当前报名人数',
    registration_deadline DATETIME COMMENT '报名截止时间',
    status VARCHAR(10) DEFAULT '0' COMMENT '状态: 0报名中, 1进行中, 2已结束, 3已取消',
    create_by BIGINT(20) COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    dept_ids VARCHAR(500) COMMENT '可参加部门ID,多个用逗号分隔',
    PRIMARY KEY (activity_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 4. 报名表
CREATE TABLE IF NOT EXISTS registration (
    registration_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '报名ID',
    activity_id BIGINT(20) NOT NULL COMMENT '活动ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    status VARCHAR(10) DEFAULT '0' COMMENT '状态: 0待审核, 1已确认, 2已拒绝, 3已取消',
    registration_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (registration_id),
    KEY idx_activity_id (activity_id),
    KEY idx_user_id (user_id),
    UNIQUE KEY uk_activity_user (activity_id, user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='报名表';

-- 5. 反馈表（已弃用，保留结构）
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    activity_id BIGINT(20) NOT NULL COMMENT '活动ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    rating INT(1) COMMENT '评分1-5星',
    content TEXT COMMENT '反馈内容',
    feedback_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '反馈时间',
    PRIMARY KEY (feedback_id),
    KEY idx_activity_id (activity_id),
    KEY idx_user_id (user_id),
    UNIQUE KEY uk_activity_user_feedback (activity_id, user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';

-- 6. 评论表
CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    activity_id BIGINT(20) NOT NULL COMMENT '活动ID',
    user_id BIGINT(20) NOT NULL COMMENT '评论人ID',
    content TEXT NOT NULL COMMENT '评论内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (comment_id),
    KEY idx_activity_id (activity_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 7. 消息表
CREATE TABLE IF NOT EXISTS message (
    message_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    title VARCHAR(200) NOT NULL COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    dept_id BIGINT(20) COMMENT '接收部门ID',
    create_by BIGINT(20) COMMENT '发送者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (message_id),
    KEY idx_dept_id (dept_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 8. 员工消息阅读状态表
CREATE TABLE IF NOT EXISTS message_read (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    message_id BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    is_read TINYINT(1) DEFAULT 0,
    read_time DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_message_user (message_id, user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='消息阅读表';

-- 插入默认部门
INSERT INTO sys_dept (dept_name) VALUES ('技术部'), ('市场部'), ('人事部'), ('财务部'), ('运营部');

-- 插入默认管理员账号 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, dept, role) VALUES ('admin', 'admin123', '系统管理员', '人事部', 'admin');

-- 插入测试员工账号 (密码: user123)
INSERT INTO sys_user (username, password, real_name, dept, position, role) VALUES ('zhangsan', 'user123', '张三', '技术部', '工程师', 'user');
INSERT INTO sys_user (username, password, real_name, dept, position, role) VALUES ('lisi', 'user123', '李四', '市场部', '经理', 'user');
