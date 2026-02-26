package com.activity.controller;

import com.activity.entity.Comment;
import com.activity.entity.Result;
import com.activity.entity.User;
import com.activity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/activity/{activityId}/comments")
    public Result<List<Comment>> getComments(@PathVariable Long activityId) {
        return Result.success(commentService.getCommentsByActivity(activityId));
    }
    
    @PostMapping("/user/comment")
    public Result<Void> addComment(@RequestBody Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        comment.setUserId(user.getUserId());
        commentService.addComment(comment);
        return Result.success();
    }
    
    @DeleteMapping("/admin/comment/{commentId}")
    public Result<Void> deleteComment(@PathVariable Long commentId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return Result.error(403, "无权限");
        }
        commentService.deleteComment(commentId);
        return Result.success();
    }
}
