package com.schedule.service;

import com.schedule.entity.User;
import com.schedule.entity.Exam;
import com.schedule.entity.Meeting;
import com.schedule.mapper.UserMapper;
import com.schedule.mapper.ExamMapper;
import com.schedule.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    
    private final UserMapper userMapper;
    private final ExamMapper examMapper;
    private final MeetingMapper meetingMapper;
    
    @Autowired
    public AdminService(UserMapper userMapper, ExamMapper examMapper, MeetingMapper meetingMapper) {
        this.userMapper = userMapper;
        this.examMapper = examMapper;
        this.meetingMapper = meetingMapper;
    }

    // 用户管理
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Transactional
    public void createUser(User user) {
        // 检查用户名是否存在
        if (findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 验证角色
        if (!"admin".equals(user.getRole()) && !"teacher".equals(user.getRole())) {
            throw new RuntimeException("无效的角色类型");
        }

        // 设置roleId
        if ("admin".equals(user.getRole())) {
            user.setRoleId(1);
        } else {
            user.setRoleId(2);
        }

        userMapper.insert(user);
    }

    @Transactional
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userMapper.delete(id);
    }

    // 监考管理
    public List<Exam> getAllExams() {
        return examMapper.findAll();
    }

    @Transactional
    public Exam createExam(Exam exam) {
        // 验证监考教师是否存在
        User supervisor = userMapper.findById(exam.getSupervisorId());
        if (supervisor == null || !"teacher".equals(supervisor.getRole())) {
            throw new RuntimeException("监考教师不存在或角色不正确");
        }
        
        examMapper.insert(exam);
        return exam;  // MyBatis会自动填充生成的ID
    }

    @Transactional
    public void updateExam(Exam exam) {
        Exam existingExam = examMapper.findById(exam.getId());
        if (existingExam == null) {
            throw new RuntimeException("考试不存在");
        }
        examMapper.update(exam);
    }

    @Transactional
    public void deleteExam(Long id) {
        examMapper.delete(id);
    }

    // 会议管理
    public List<Meeting> getAllMeetings() {
        return meetingMapper.findAll();
    }

    @Transactional
    public void createMeeting(Meeting meeting, List<Long> participantIds) {
        meetingMapper.insert(meeting);
        for (Long userId : participantIds) {
            meetingMapper.insertParticipant(meeting.getId(), userId);
        }
    }

    @Transactional
    public void updateMeeting(Meeting meeting, List<Long> participantIds) {
        meetingMapper.update(meeting);
        meetingMapper.deleteAllParticipants(meeting.getId());
        for (Long userId : participantIds) {
            meetingMapper.insertParticipant(meeting.getId(), userId);
        }
    }

    @Transactional
    public void deleteMeeting(Long id) {
        meetingMapper.deleteAllParticipants(id);
        meetingMapper.delete(id);
    }

    public List<User> getMeetingParticipants(Long meetingId) {
        return meetingMapper.findParticipants(meetingId);
    }

    // 根据ID查找考试
    public Exam findExamById(Long id) {
        return examMapper.findById(id);
    }

    public List<User> getAllTeachers() {
        return userMapper.findByRole("teacher");
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    // 根据ID查找用户
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    // 根据ID查找会议
    public Meeting findMeetingById(Long id) {
        return meetingMapper.findById(id);
    }
} 