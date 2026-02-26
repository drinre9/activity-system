package com.activity.mapper;

import com.activity.entity.Feedback;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FeedbackMapper {
    
    @Select("SELECT f.*, u.real_name as userName, a.activity_name as activityName " +
            "FROM feedback f " +
            "LEFT JOIN sys_user u ON f.user_id = u.user_id " +
            "LEFT JOIN activity a ON f.activity_id = a.activity_id " +
            "ORDER BY f.feedback_time DESC")
    List<Feedback> findAll();
    
    @Select("SELECT f.*, u.real_name as userName, a.activity_name as activityName " +
            "FROM feedback f " +
            "LEFT JOIN sys_user u ON f.user_id = u.user_id " +
            "LEFT JOIN activity a ON f.activity_id = a.activity_id " +
            "WHERE f.activity_id = #{activityId} " +
            "ORDER BY f.feedback_time DESC")
    List<Feedback> findByActivityId(@Param("activityId") Long activityId);
    
    @Select("SELECT f.*, u.real_name as userName, a.activity_name as activityName " +
            "FROM feedback f " +
            "LEFT JOIN sys_user u ON f.user_id = u.user_id " +
            "LEFT JOIN activity a ON f.activity_id = a.activity_id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.feedback_time DESC")
    List<Feedback> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM feedback WHERE activity_id = #{activityId} AND user_id = #{userId}")
    Feedback findByActivityAndUser(@Param("activityId") Long activityId, @Param("userId") Long userId);
    
    @Insert("INSERT INTO feedback(activity_id, user_id, rating, content) " +
            "VALUES(#{activityId}, #{userId}, #{rating}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "feedbackId")
    int insert(Feedback feedback);
    
    @Delete("DELETE FROM feedback WHERE feedback_id = #{feedbackId}")
    int delete(@Param("feedbackId") Long feedbackId);
    
    @Delete("DELETE FROM feedback WHERE activity_id = #{activityId}")
    int deleteByActivityId(@Param("activityId") Long activityId);
}
