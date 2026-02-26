package com.activity.mapper;

import com.activity.entity.Activity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ActivityMapper {
    
    @Select("SELECT * FROM activity ORDER BY create_time DESC")
    List<Activity> findAll();
    
    @Select("SELECT * FROM activity WHERE activity_id = #{activityId}")
    Activity findById(@Param("activityId") Long activityId);
    
    @Select("<script>" +
            "SELECT * FROM activity " +
            "<where>" +
            "<if test='activityName != null and activityName != \"\"'> AND activity_name LIKE CONCAT('%', #{activityName}, '%') </if>" +
            "<if test='activityType != null and activityType != \"\"'> AND activity_type = #{activityType} </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "</where>" +
            " ORDER BY create_time DESC" +
            "</script>")
    List<Activity> findList(Activity activity);
    
    @Select("SELECT * FROM activity WHERE status IN ('0', '1') ORDER BY start_time ASC")
    List<Activity> findActiveList();
    
    @Insert("INSERT INTO activity(activity_name, activity_type, location, start_time, end_time, description, " +
            "max_participants, current_participants, registration_deadline, status, create_by) " +
            "VALUES(#{activityName}, #{activityType}, #{location}, #{startTime}, #{endTime}, #{description}, " +
            "#{maxParticipants}, #{currentParticipants}, #{registrationDeadline}, #{status}, #{createBy})")
    @Options(useGeneratedKeys = true, keyProperty = "activityId", keyColumn = "activity_id")
    int insert(Activity activity);
    
    @Update("UPDATE activity SET activity_name=#{activityName}, activity_type=#{activityType}, location=#{location}, " +
            "start_time=#{startTime}, end_time=#{endTime}, description=#{description}, " +
            "max_participants=#{maxParticipants}, registration_deadline=#{registrationDeadline}, status=#{status} " +
            "WHERE activity_id=#{activityId}")
    int update(Activity activity);
    
    @Update("UPDATE activity SET current_participants = current_participants + #{delta} WHERE activity_id = #{activityId}")
    int updateParticipants(@Param("activityId") Long activityId, @Param("delta") Integer delta);
    
    @Delete("DELETE FROM activity WHERE activity_id = #{activityId}")
    int delete(@Param("activityId") Long activityId);
}
