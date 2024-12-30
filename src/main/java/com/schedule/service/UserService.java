package com.schedule.service;

import com.schedule.entity.User;
import com.schedule.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    public void createUser(User user) {
        userMapper.insert(user);
    }
} 