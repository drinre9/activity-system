package com.activity.service;

import com.activity.entity.Activity;
import com.activity.entity.Registration;
import com.activity.mapper.RegistrationMapper;
import com.activity.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RegistrationService {
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private ActivityMapper activityMapper;
    
    public List<Registration> getAllRegistrations() {
        return registrationMapper.findAll();
    }
    
    public List<Registration> getRegistrationsByActivity(Long activityId) {
        return registrationMapper.findByActivityId(activityId);
    }
    
    public List<Registration> getRegistrationsByUser(Long userId) {
        return registrationMapper.findByUserId(userId);
    }
    
    @Transactional
    public int register(Registration registration) {
        Registration exist = registrationMapper.findByActivityAndUser(
            registration.getActivityId(), registration.getUserId());
        if (exist != null) {
            throw new RuntimeException("您已报名此活动");
        }
        
        Activity activity = activityMapper.findById(registration.getActivityId());
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        int rows = registrationMapper.insert(registration);
        activityMapper.updateParticipants(registration.getActivityId(), 1);
        return rows;
    }
    
    public int auditRegistration(Registration registration) {
        return registrationMapper.update(registration);
    }
    
    @Transactional
    public int cancelRegistration(Long registrationId) {
        Registration registration = registrationMapper.findById(registrationId);
        if (registration != null) {
            activityMapper.updateParticipants(registration.getActivityId(), -1);
        }
        return registrationMapper.delete(registrationId);
    }
}
