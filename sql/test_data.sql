-- 测试数据脚本
USE activity_system;

-- 清空现有测试数据（保留系统账号）
DELETE FROM message_read;
DELETE FROM message;
DELETE FROM comment;
DELETE FROM registration;
DELETE FROM activity;
DELETE FROM sys_user WHERE user_id > 1;

-- 重置自增ID
ALTER TABLE sys_user AUTO_INCREMENT = 2;
ALTER TABLE activity AUTO_INCREMENT = 1;
ALTER TABLE registration AUTO_INCREMENT = 1;
ALTER TABLE comment AUTO_INCREMENT = 1;
ALTER TABLE message AUTO_INCREMENT = 1;
ALTER TABLE message_read AUTO_INCREMENT = 1;

-- 插入测试员工（不同部门）
INSERT INTO sys_user (username, password, real_name, dept, position, role, status) VALUES
('zhangsan', '123456', '张三', '技术部', '工程师', 'user', '1'),
('lisi', '123456', '李四', '市场部', '经理', 'user', '1'),
('wangwu', '123456', '王五', '技术部', '高级工程师', 'user', '1'),
('zhaoliu', '123456', '赵六', '人事部', 'HR', 'user', '1'),
('sunqi', '123456', '孙七', '财务部', '会计', 'user', '1'),
('zhouba', '123456', '周八', '运营部', '运营专员', 'user', '1'),
('wujiu', '123456', '吴九', '技术部', '测试工程师', 'user', '1');

-- 插入活动（不同状态，不同部门）
INSERT INTO activity (activity_name, activity_type, location, start_time, end_time, description, max_participants, current_participants, registration_deadline, status, create_by, dept_ids) VALUES
-- 进行中的活动（可报名）
('周末篮球赛', '体育运动', '公司篮球场', '2026-02-28 14:00:00', '2026-02-28 18:00:00', '部门间篮球比赛，欢迎大家报名参加', 20, 5, '2026-02-28 12:00:00', '1', 1, '1,2,3'),
('户外拓展训练', '户外拓展', '森林公园', '2026-03-05 09:00:00', '2026-03-05 17:00:00', '团队户外拓展活动，增强凝聚力', 30, 8, '2026-03-04 18:00:00', '0', 1, '1,2'),

-- 报名中的活动
('技术分享会', '文艺活动', '会议室A', '2026-03-10 15:00:00', '2026-03-10 17:00:00', '技术部内部分享会，主题：微服务架构实践', 50, 12, '2026-03-10 12:00:00', '0', 1, '1'),
('团建聚餐', '团建聚餐', '金色港湾餐厅', '2026-03-15 18:00:00', '2026-03-15 21:00:00', '月度团建聚餐，大家放松交流', 40, 15, '2026-03-14 18:00:00', '0', 1, '1,2,3,4,5,6'),

-- 已结束的活动
('年会活动', '文艺活动', '酒店宴会厅', '2026-01-20 18:00:00', '2026-01-20 22:00:00', '2026年度总结大会', 200, 180, '2026-01-19 18:00:00', '2', 1, '1,2,3,4,5,6'),
('元旦趣味运动会', '体育运动', '公司操场', '2026-01-01 09:00:00', '2026-01-01 12:00:00', '趣味运动项目，丰富员工生活', 100, 85, '2025-12-31 18:00:00', '2', 1, '1,2,3,4,5,6'),

-- 针对特定部门的活动
('技术部内部培训', '志愿服务', '培训室B', '2026-03-20 14:00:00', '2026-03-20 17:00:00', '新技术培训，欢迎技术部同事参加', 30, 5, '2026-03-19 18:00:00', '0', 1, '1'),

-- 已取消的活动
('原计划旅游', '户外拓展', '待定', '2026-04-01 08:00:00', '2026-04-03 18:00:00', '因天气原因取消', 50, 0, '2026-03-25 18:00:00', '3', 1, '1,2,3,4,5,6');

-- 插入报名记录
INSERT INTO registration (activity_id, user_id, status, registration_time) VALUES
-- 张三的报名
(1, 2, '1', '2026-02-25 10:00:00'),
(2, 2, '0', '2026-02-26 11:00:00'),
(5, 2, '1', '2026-01-15 14:00:00'),
(6, 2, '1', '2025-12-28 09:00:00'),

-- 李四的报名
(1, 3, '1', '2026-02-25 11:00:00'),
(3, 3, '1', '2026-02-27 15:00:00'),
(4, 3, '0', '2026-02-28 10:00:00'),
(5, 3, '1', '2026-01-16 10:00:00'),

-- 王五的报名
(1, 4, '1', '2026-02-25 14:00:00'),
(2, 4, '1', '2026-02-26 09:00:00'),
(7, 4, '0', '2026-02-27 16:00:00'),
(5, 4, '1', '2026-01-15 11:00:00'),

-- 赵六的报名
(1, 5, '0', '2026-02-26 10:00:00'),
(4, 5, '1', '2026-02-28 14:00:00'),
(5, 5, '1', '2026-01-16 09:00:00');

-- 更新活动的当前报名人数
UPDATE activity SET current_participants = (SELECT COUNT(*) FROM registration WHERE activity_id = activity.activity_id AND status != '3');

-- 插入评论（针对已结束的活动）
INSERT INTO comment (activity_id, user_id, content, create_time) VALUES
-- 年会活动评论
(5, 2, '年会办得很不错，期待明年！', '2026-01-20 22:30:00'),
(5, 3, '节目很精彩，奖品也很丰厚', '2026-01-20 22:45:00'),
(5, 4, '建议明年增加更多互动环节', '2026-01-21 09:00:00'),
(5, 5, '整体很棒！', '2026-01-21 10:30:00'),

-- 元旦趣味运动会评论
(6, 2, '趣味项目很有趣，氛围很好', '2026-01-01 12:30:00'),
(6, 3, '希望多举办类似的体育活动', '2026-01-01 13:00:00'),
(6, 4, '活动组织得很到位', '2026-01-02 09:00:00');

-- 插入消息
INSERT INTO message (title, content, dept_id, create_by, create_time) VALUES
('周末篮球赛通知', '技术部、市场部、人事部的同事们，周末篮球赛即将开始，欢迎报名参加！', 1, 1, '2026-02-25 10:00:00'),
('户外拓展训练通知', '技术部、市场部的同事们，本周日有户外拓展训练，名额有限速速报名！', 1, 1, '2026-02-26 09:00:00'),
('技术分享会通知', '技术部的同事们，本周五下午有技术分享会，欢迎参加！', 1, 1, '2026-02-27 14:00:00'),
('团建聚餐通知', '全体同事们，本月团建聚餐将于3月15日举行，请各部门同事积极报名！', 1, 1, '2026-02-28 10:00:00'),
('技术部培训通知', '技术部的同事们，下周有内部技术培训，请准时参加！', 1, 1, '2026-03-01 11:00:00');

-- 插入消息阅读记录（模拟部分已读）
INSERT INTO message_read (message_id, user_id, is_read, read_time) VALUES
(1, 2, 1, '2026-02-25 10:30:00'),
(1, 3, 1, '2026-02-25 11:00:00'),
(2, 2, 1, '2026-02-26 10:00:00'),
(2, 4, 1, '2026-02-26 11:00:00'),
(3, 4, 1, '2026-02-27 15:00:00');

-- 查询测试数据
SELECT '=== 测试账号 ===' as info;
SELECT username, password, real_name, dept, position, role FROM sys_user;

SELECT '=== 活动数据 ===' as info;
SELECT activity_id, activity_name, activity_type, status, dept_ids FROM activity;

SELECT '=== 报名数据 ===' as info;
SELECT r.registration_id, r.activity_id, r.user_id, r.status, u.real_name, a.activity_name 
FROM registration r 
LEFT JOIN sys_user u ON r.user_id = u.user_id 
LEFT JOIN activity a ON r.activity_id = a.activity_id;

SELECT '=== 评论数据 ===' as info;
SELECT c.*, u.real_name, a.activity_name FROM comment c 
LEFT JOIN sys_user u ON c.user_id = u.user_id 
LEFT JOIN activity a ON c.activity_id = a.activity_id;

SELECT '=== 消息数据 ===' as info;
SELECT m.*, d.dept_name FROM message m 
LEFT JOIN sys_dept d ON m.dept_id = d.dept_id;
