package com.activity.controller;

import com.activity.entity.Registration;
import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegistrationController {
    
    @Autowired
    private RegistrationService registrationService;
    
    @GetMapping("/admin/registrations")
    public Result<List<Registration>> getAllRegistrations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(registrationService.getAllRegistrations());
    }
    
    @GetMapping("/admin/registrations/{activityId}")
    public Result<List<Registration>> getRegistrationsByActivity(@PathVariable Long activityId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(registrationService.getRegistrationsByActivity(activityId));
    }
    
    @GetMapping("/user/registrations")
    public Result<List<Registration>> getMyRegistrations(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(registrationService.getRegistrationsByUser(user.getUserId()));
    }
    
    @PostMapping("/user/register/{activityId}")
    public Result<Void> register(@PathVariable Long activityId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setUserId(user.getUserId());
        registration.setStatus("0");
        registrationService.register(registration);
        return Result.success();
    }
    
    @PostMapping("/user/cancel/{registrationId}")
    public Result<Void> cancelRegistration(@PathVariable Long registrationId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        registrationService.cancelRegistration(registrationId);
        return Result.success();
    }
    
    @PutMapping("/admin/registration/audit")
    public Result<Void> auditRegistration(@RequestBody Registration registration, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        registrationService.auditRegistration(registration);
        return Result.success();
    }
}
