package com.activity.controller;

import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.entity.Dept;
import com.activity.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DeptController {
    
    @Autowired
    private DeptService deptService;
    
    @GetMapping("/admin/depts")
    public Result<List<Dept>> getAllDepts(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(deptService.getAllDepts());
    }
    
    @GetMapping("/depts")
    public Result<List<Dept>> getDepts() {
        return Result.success(deptService.getAllDepts());
    }
    
    @PostMapping("/admin/dept")
    public Result<Void> addDept(@RequestBody Dept dept, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权限");
        }
        try {
            deptService.addDept(dept);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/admin/dept/{deptId}")
    public Result<Void> deleteDept(@PathVariable Long deptId, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权限");
        }
        deptService.deleteDept(deptId);
        return Result.success();
    }
}
