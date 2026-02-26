package com.activity.mapper;

import com.activity.entity.Registration;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RegistrationMapper {
    
    @Select("SELECT r.*, u.real_name as userName, u.dept, a.activity_name as activityName " +
            "FROM registration r " +
            "LEFT JOIN sys_user u ON r.user_id = u.user_id " +
            "LEFT JOIN activity a ON r.activity_id = a.activity_id " +
            "ORDER BY r.registration_time DESC")
    List<Registration> findAll();
    
    @Select("SELECT r.*, u.real_name as userName, u.dept, a.activity_name as activityName " +
            "FROM registration r " +
            "LEFT JOIN sys_user u ON r.user_id = u.user_id " +
            "LEFT JOIN activity a ON r.activity_id = a.activity_id " +
            "WHERE r.activity_id = #{activityId} " +
            "ORDER BY r.registration_time DESC")
    List<Registration> findByActivityId(@Param("activityId") Long activityId);
    
    @Select("SELECT r.*, u.real_name as userName, u.dept, a.activity_name as activityName " +
            "FROM registration r " +
            "LEFT JOIN sys_user u ON r.user_id = u.user_id " +
            "LEFT JOIN activity a ON r.activity_id = a.activity_id " +
            "WHERE r.user_id = #{userId} " +
            "ORDER BY r.registration_time DESC")
    List<Registration> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM registration WHERE registration_id = #{registrationId}")
    Registration findById(@Param("registrationId") Long registrationId);
    
    @Select("SELECT * FROM registration WHERE activity_id = #{activityId} AND user_id = #{userId}")
    Registration findByActivityAndUser(@Param("activityId") Long activityId, @Param("userId") Long userId);
    
    @Insert("INSERT INTO registration(activity_id, user_id, status, remark) " +
            "VALUES(#{activityId}, #{userId}, #{status}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "registrationId")
    int insert(Registration registration);
    
    @Update("UPDATE registration SET status=#{status}, remark=#{remark} WHERE registration_id=#{registrationId}")
    int update(Registration registration);
    
    @Delete("DELETE FROM registration WHERE registration_id = #{registrationId}")
    int delete(@Param("registrationId") Long registrationId);
    
    @Delete("DELETE FROM registration WHERE activity_id = #{activityId}")
    int deleteByActivityId(@Param("activityId") Long activityId);
}
