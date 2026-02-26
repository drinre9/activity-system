package com.activity.mapper;

import com.activity.entity.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MessageMapper {
    
    @Select("SELECT m.*, d.dept_name as deptName, u.real_name as createByName " +
            "FROM message m " +
            "LEFT JOIN sys_dept d ON m.dept_id = d.dept_id " +
            "LEFT JOIN sys_user u ON m.create_by = u.user_id " +
            "ORDER BY m.create_time DESC")
    List<Message> findAll();
    
    @Select("SELECT m.*, d.dept_name as deptName, u.real_name as createByName " +
            "FROM message m " +
            "LEFT JOIN sys_dept d ON m.dept_id = d.dept_id " +
            "LEFT JOIN sys_user u ON m.create_by = u.user_id " +
            "WHERE m.dept_id = #{deptId} " +
            "ORDER BY m.create_time DESC")
    List<Message> findByDeptId(@Param("deptId") Long deptId);
    
    @Insert("INSERT INTO message(title, content, dept_id, create_by) VALUES(#{title}, #{content}, #{deptId}, #{createBy})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    int insert(Message message);
    
    @Delete("DELETE FROM message WHERE message_id = #{messageId}")
    int delete(@Param("messageId") Long messageId);
    
    @Insert("INSERT INTO message_read(message_id, user_id, is_read, read_time) VALUES(#{messageId}, #{userId}, 1, NOW())")
    int insertReadRecord(@Param("messageId") Long messageId, @Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM message_read WHERE message_id = #{messageId} AND user_id = #{userId} AND is_read = 1")
    int checkRead(@Param("messageId") Long messageId, @Param("userId") Long userId);
    
    @Select("SELECT m.*, d.dept_name as deptName, u.real_name as createByName, " +
            "(SELECT COUNT(*) FROM message_read mr WHERE mr.message_id = m.message_id AND mr.user_id = #{userId} AND mr.is_read = 0) as unreadCount " +
            "FROM message m " +
            "LEFT JOIN sys_dept d ON m.dept_id = d.dept_id " +
            "LEFT JOIN sys_user u ON m.create_by = u.user_id " +
            "WHERE d.dept_name = (SELECT dept FROM sys_user WHERE user_id = #{userId}) " +
            "ORDER BY m.create_time DESC")
    List<Message> findMyMessages(@Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM message m " +
            "LEFT JOIN sys_dept d ON m.dept_id = d.dept_id " +
            "WHERE d.dept_name = (SELECT dept FROM sys_user WHERE user_id = #{userId}) " +
            "AND m.message_id NOT IN (SELECT message_id FROM message_read WHERE user_id = #{userId})")
    int getUnreadCount(@Param("userId") Long userId);
}
