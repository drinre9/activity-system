package com.activity.service;

import com.activity.entity.Feedback;
import com.activity.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    public List<Feedback> getAllFeedbacks() {
        return feedbackMapper.findAll();
    }
    
    public List<Feedback> getFeedbacksByActivity(Long activityId) {
        return feedbackMapper.findByActivityId(activityId);
    }
    
    public List<Feedback> getFeedbacksByUser(Long userId) {
        return feedbackMapper.findByUserId(userId);
    }
    
    public int addFeedback(Feedback feedback) {
        Feedback exist = feedbackMapper.findByActivityAndUser(
            feedback.getActivityId(), feedback.getUserId());
        if (exist != null) {
            throw new RuntimeException("您已提交过反馈");
        }
        return feedbackMapper.insert(feedback);
    }
    
    public int deleteFeedback(Long feedbackId) {
        return feedbackMapper.delete(feedbackId);
    }
}
