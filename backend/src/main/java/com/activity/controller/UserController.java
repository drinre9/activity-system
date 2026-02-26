package com.activity.controller;

import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser == null) {
            return Result.error("用户名或密码错误");
        }
        if ("0".equals(loginUser.getStatus())) {
            return Result.error("账号已被禁用");
        }
        session.setAttribute("user", loginUser);
        Map<String, Object> data = new HashMap<>();
        data.put("user", loginUser);
        return Result.success(data);
    }
    
    @GetMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }
    
    @GetMapping("/user/info")
    public Result<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(user);
    }
    
    @GetMapping("/admin/users")
    public Result<List<User>> getAllUsers(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(userService.getAllUsers());
    }
    
    @PostMapping("/admin/user")
    public Result<Void> addUser(@RequestBody User user, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权限");
        }
        userService.addUser(user);
        return Result.success();
    }
    
    @PutMapping("/admin/user")
    public Result<Void> updateUser(@RequestBody User user, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权限");
        }
        userService.updateUser(user);
        return Result.success();
    }
    
    @DeleteMapping("/admin/user/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权限");
        }
        userService.deleteUser(userId);
        return Result.success();
    }
    
    @PutMapping("/user/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> params, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        userService.updatePassword(user.getUserId(), oldPassword, newPassword);
        return Result.success();
    }
}
