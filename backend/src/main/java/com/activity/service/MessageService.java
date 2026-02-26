package com.activity.service;

import com.activity.entity.Message;
import com.activity.mapper.MessageMapper;
import com.activity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public List<Message> getAllMessages() {
        return messageMapper.findAll();
    }
    
    public List<Message> getMyMessages(Long userId) {
        return messageMapper.findMyMessages(userId);
    }
    
    public int getUnreadCount(Long userId) {
        return messageMapper.getUnreadCount(userId);
    }
    
    public int sendMessage(Message message) {
        return messageMapper.insert(message);
    }
    
    public int deleteMessage(Long messageId) {
        return messageMapper.delete(messageId);
    }
    
    public void markAsRead(Long messageId, Long userId) {
        int exists = messageMapper.checkRead(messageId, userId);
        if (exists == 0) {
            messageMapper.insertReadRecord(messageId, userId);
        }
    }
}
