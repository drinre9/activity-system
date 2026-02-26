package com.activity.service;

import com.activity.entity.Activity;
import com.activity.mapper.ActivityMapper;
import com.activity.mapper.RegistrationMapper;
import com.activity.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ActivityService {
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    public List<Activity> getAllActivities() {
        return activityMapper.findAll();
    }
    
    public Activity getActivityById(Long activityId) {
        return activityMapper.findById(activityId);
    }
    
    public List<Activity> getActivityList(Activity activity) {
        return activityMapper.findList(activity);
    }
    
    public List<Activity> getActiveActivities() {
        return activityMapper.findActiveList();
    }
    
    @Transactional
    public int addActivity(Activity activity) {
        activity.setCurrentParticipants(0);
        int result = activityMapper.insert(activity);
        System.out.println("Insert result: " + result + ", activityId: " + activity.getActivityId());
        return result;
    }
    
    public int updateActivity(Activity activity) {
        return activityMapper.update(activity);
    }
    
    @Transactional
    public int deleteActivity(Long activityId) {
        registrationMapper.deleteByActivityId(activityId);
        feedbackMapper.deleteByActivityId(activityId);
        return activityMapper.delete(activityId);
    }
    
    @Transactional
    public int updateParticipants(Long activityId, Integer delta) {
        return activityMapper.updateParticipants(activityId, delta);
    }
}
