package com.activity.service;

import com.activity.entity.User;
import com.activity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public User getUserById(Long userId) {
        return userMapper.findById(userId);
    }
    
    public List<User> getAllUsers() {
        return userMapper.findAllUsers();
    }
    
    public int addUser(User user) {
        User existUser = userMapper.findByUsername(user.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setStatus("1");
        if (user.getRole() == null) {
            user.setRole("user");
        }
        return userMapper.insert(user);
    }
    
    public int updateUser(User user) {
        return userMapper.update(user);
    }
    
    public int deleteUser(Long userId) {
        return userMapper.delete(userId);
    }
    
    public int updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("原密码错误");
        }
        return userMapper.updatePassword(userId, newPassword);
    }
}
