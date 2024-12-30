package com.schedule.service;

import com.schedule.entity.Schedule;
import com.schedule.mapper.ScheduleMapper;
import com.schedule.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 日程服务类
 * 处理日程相关的业务逻辑
 */
@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    private final UserMapper userMapper;
    
    public ScheduleService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    /**
     * 获取用户的个人日程列表
     * @param username 用户名
     * @return 日程列表
     */
    public List<Schedule> getPersonalSchedules(String username) {
        Long userId = userMapper.findByUsername(username).getId();
        return scheduleMapper.findByUserId(userId);
    }
    
    /**
     * 创建个人日程
     * @param username 用户名
     * @param schedule 日程信息
     */
    @Transactional
    public void createPersonalSchedule(String username, Schedule schedule) {
        Long userId = userMapper.findByUsername(username).getId();
        schedule.setUserId(userId);
        schedule.setEventType("personal");
        scheduleMapper.insert(schedule);
    }
    
    @Transactional
    public void updatePersonalSchedule(String username, Long scheduleId, Schedule schedule) {
        Long userId = userMapper.findByUsername(username).getId();
        Schedule existingSchedule = scheduleMapper.findById(scheduleId);
        
        if (existingSchedule == null || !existingSchedule.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此日程");
        }
        
        schedule.setId(scheduleId);
        schedule.setUserId(userId);
        scheduleMapper.update(schedule);
    }
    
    @Transactional
    public void deletePersonalSchedule(String username, Long scheduleId) {
        Long userId = userMapper.findByUsername(username).getId();
        Schedule schedule = scheduleMapper.findById(scheduleId);
        
        if (schedule == null || !schedule.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此日程");
        }
        
        scheduleMapper.delete(scheduleId);
    }
    
    public List<Schedule> getUpcomingSchedules(LocalDate date, LocalTime time) {
        return scheduleMapper.findUpcoming(date, time);
    }
    
    // 获取教师的日程列表
    public List<Schedule> getTeacherSchedules(String username) {
        return scheduleMapper.findByUsername(username);
    }
    
    // 更新教师的日程
    public void updateTeacherSchedule(String username, Long id, Schedule schedule) {
        // 先检查日程是否属于该教师
        Schedule existingSchedule = scheduleMapper.findById(id);
        if (existingSchedule == null) {
            throw new RuntimeException("日程不存在");
        }
        
        // 检查权限（确保日程属于当前用户）
        if (!isScheduleOwner(username, existingSchedule)) {
            throw new RuntimeException("无权限修改此日程");
        }
        
        // 设置ID和用户ID
        schedule.setId(id);
        schedule.setUserId(existingSchedule.getUserId());
        
        // 更新日程
        scheduleMapper.update(schedule);
    }
    
    // 根据ID查找日程
    public Schedule findById(Long id) {
        return scheduleMapper.findById(id);
    }
    
    // 检查日程是否属于指定用户
    private boolean isScheduleOwner(String username, Schedule schedule) {
        // 通过用户名获取用户ID
        Long userId = scheduleMapper.findUserIdByUsername(username);
        return schedule != null && schedule.getUserId().equals(userId);
    }
} 