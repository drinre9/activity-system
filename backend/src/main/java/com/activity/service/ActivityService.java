package com.activity.service;

import com.activity.entity.Activity;
import com.activity.entity.Message;
import com.activity.mapper.ActivityMapper;
import com.activity.mapper.RegistrationMapper;
import com.activity.mapper.FeedbackMapper;
import com.activity.mapper.CommentMapper;
import com.activity.mapper.MessageMapper;
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
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private MessageMapper messageMapper;
    
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
        
        if (activity.getDeptIds() != null && !activity.getDeptIds().isEmpty()) {
            String[] deptIdArr = activity.getDeptIds().split(",");
            for (String deptId : deptIdArr) {
                Message msg = new Message();
                msg.setTitle("新活动通知");
                msg.setContent("您部门有新活动可以参加：" + activity.getActivityName() + "，活动地点：" + activity.getLocation());
                msg.setDeptId(Long.parseLong(deptId.trim()));
                msg.setCreateBy(activity.getCreateBy());
                messageMapper.insert(msg);
            }
        }
        
        return result;
    }
    
    public int updateActivity(Activity activity) {
        return activityMapper.update(activity);
    }
    
    @Transactional
    public int deleteActivity(Long activityId) {
        commentMapper.deleteByActivityId(activityId);
        registrationMapper.deleteByActivityId(activityId);
        feedbackMapper.deleteByActivityId(activityId);
        return activityMapper.delete(activityId);
    }
    
    @Transactional
    public int updateParticipants(Long activityId, Integer delta) {
        return activityMapper.updateParticipants(activityId, delta);
    }
}
