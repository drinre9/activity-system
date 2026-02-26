package com.activity.service;

import com.activity.entity.Comment;
import com.activity.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    public List<Comment> getCommentsByActivity(Long activityId) {
        return commentMapper.findByActivityId(activityId);
    }
    
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }
    
    public int deleteComment(Long commentId) {
        return commentMapper.delete(commentId);
    }
    
    public int deleteByActivityId(Long activityId) {
        return commentMapper.deleteByActivityId(activityId);
    }
}
