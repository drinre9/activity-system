package com.activity.controller;

import com.activity.entity.Message;
import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @GetMapping("/admin/messages")
    public Result<List<Message>> getAllMessages(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        return Result.success(messageService.getAllMessages());
    }
    
    @GetMapping("/user/messages")
    public Result<List<Message>> getMyMessages(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(messageService.getMyMessages(user.getUserId()));
    }
    
    @GetMapping("/user/messages/unread")
    public Result<Map<String, Integer>> getUnreadCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("count", messageService.getUnreadCount(user.getUserId()));
        return Result.success(result);
    }
    
    @PostMapping("/admin/message")
    public Result<Void> sendMessage(@RequestBody Message message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        message.setCreateBy(user.getUserId());
        messageService.sendMessage(message);
        return Result.success();
    }
    
    @DeleteMapping("/admin/message/{messageId}")
    public Result<Void> deleteMessage(@PathVariable Long messageId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        messageService.deleteMessage(messageId);
        return Result.success();
    }
    
    @PostMapping("/user/message/{messageId}/read")
    public Result<Void> markAsRead(@PathVariable Long messageId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        messageService.markAsRead(messageId, user.getUserId());
        return Result.success();
    }
}
