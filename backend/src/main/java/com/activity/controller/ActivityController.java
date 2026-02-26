package com.activity.controller;

import com.activity.entity.Activity;
import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.mapper.ActivityMapper;
import com.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @GetMapping("/activities")
    public Result<List<Activity>> getAllActivities() {
        return Result.success(activityService.getAllActivities());
    }
    
    @GetMapping("/activities/active")
    public Result<List<Activity>> getActiveActivities() {
        return Result.success(activityService.getActiveActivities());
    }
    
    @GetMapping("/activity/{id}")
    public Result<Activity> getActivityById(@PathVariable Long id) {
        return Result.success(activityService.getActivityById(id));
    }
    
    @GetMapping("/activities/list")
    public Result<List<Activity>> getActivityList(Activity activity) {
        return Result.success(activityService.getActivityList(activity));
    }
    
    @PostMapping("/admin/activity")
    public Result<Void> addActivity(@RequestBody Activity activity, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        activity.setCreateBy(user.getUserId());
        activityService.addActivity(activity);
        return Result.success();
    }
    
    @PutMapping("/admin/activity")
    public Result<Void> updateActivity(@RequestBody Activity activity, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        activityService.updateActivity(activity);
        return Result.success();
    }
    
    @DeleteMapping("/admin/activity/{id}")
    public Result<Void> deleteActivity(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        activityService.deleteActivity(id);
        return Result.success();
    }
}
