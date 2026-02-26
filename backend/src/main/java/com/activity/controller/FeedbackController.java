package com.activity.controller;

import com.activity.entity.Feedback;
import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @GetMapping("/admin/feedbacks")
    public Result<List<Feedback>> getAllFeedbacks(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(feedbackService.getAllFeedbacks());
    }
    
    @GetMapping("/admin/feedbacks/{activityId}")
    public Result<List<Feedback>> getFeedbacksByActivity(@PathVariable Long activityId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(feedbackService.getFeedbacksByActivity(activityId));
    }
    
    @GetMapping("/user/feedbacks")
    public Result<List<Feedback>> getMyFeedbacks(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(feedbackService.getFeedbacksByUser(user.getUserId()));
    }
    
    @PostMapping("/user/feedback")
    public Result<Void> addFeedback(@RequestBody Feedback feedback, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        feedback.setUserId(user.getUserId());
        feedbackService.addFeedback(feedback);
        return Result.success();
    }
    
    @DeleteMapping("/admin/feedback/{id}")
    public Result<Void> deleteFeedback(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        feedbackService.deleteFeedback(id);
        return Result.success();
    }
}
