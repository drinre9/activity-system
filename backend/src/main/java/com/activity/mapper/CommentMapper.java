package com.activity.mapper;

import com.activity.entity.Comment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CommentMapper {
    
    @Select("SELECT c.*, u.real_name as userName, u.dept as deptName " +
            "FROM comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.user_id " +
            "WHERE c.activity_id = #{activityId} " +
            "ORDER BY c.create_time DESC")
    List<Comment> findByActivityId(@Param("activityId") Long activityId);
    
    @Insert("INSERT INTO comment(activity_id, user_id, content) VALUES(#{activityId}, #{userId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    int insert(Comment comment);
    
    @Delete("DELETE FROM comment WHERE comment_id = #{commentId}")
    int delete(@Param("commentId") Long commentId);
    
    @Delete("DELETE FROM comment WHERE activity_id = #{activityId}")
    int deleteByActivityId(@Param("activityId") Long activityId);
}
