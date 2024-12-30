package com.schedule.service;

import com.schedule.entity.Exam;
import com.schedule.mapper.ExamMapper;
import com.schedule.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ExamService {
    
    private final ExamMapper examMapper;
    private final UserMapper userMapper;
    
    public ExamService(ExamMapper examMapper, UserMapper userMapper) {
        this.examMapper = examMapper;
        this.userMapper = userMapper;
    }
    
    public List<Exam> getTeacherExams(String username) {
        Long userId = userMapper.findByUsername(username).getId();
        return examMapper.findBySupervisorId(userId);
    }
    
    public List<Exam> getUpcomingExams(LocalDate date, LocalTime time) {
        return examMapper.findUpcoming(date, time);
    }
} 