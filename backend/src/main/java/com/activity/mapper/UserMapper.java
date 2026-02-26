package com.activity.mapper;

import com.activity.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM sys_user WHERE user_id = #{userId}")
    User findById(@Param("userId") Long userId);
    
    @Select("SELECT * FROM sys_user WHERE role = 'user' ORDER BY create_time DESC")
    List<User> findAllUsers();
    
    @Insert("INSERT INTO sys_user(username, password, real_name, phone, email, dept, position, role, status) " +
            "VALUES(#{username}, #{password}, #{realName}, #{phone}, #{email}, #{dept}, #{position}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);
    
    @Update("UPDATE sys_user SET real_name=#{realName}, phone=#{phone}, email=#{email}, " +
            "dept=#{dept}, position=#{position}, status=#{status} WHERE user_id=#{userId}")
    int update(User user);
    
    @Update("UPDATE sys_user SET password=#{password} WHERE user_id=#{userId}")
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);
    
    @Delete("DELETE FROM sys_user WHERE user_id = #{userId}")
    int delete(@Param("userId") Long userId);
}
