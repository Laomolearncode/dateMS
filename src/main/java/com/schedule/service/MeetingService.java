package com.schedule.service;

import com.schedule.entity.Meeting;
import com.schedule.mapper.MeetingMapper;
import com.schedule.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import com.schedule.entity.User;

@Service
public class MeetingService {
    
    private final MeetingMapper meetingMapper;
    private final UserMapper userMapper;
    
    public MeetingService(MeetingMapper meetingMapper, UserMapper userMapper) {
        this.meetingMapper = meetingMapper;
        this.userMapper = userMapper;
    }
    
    public List<Meeting> getTeacherMeetings(String username) {
        Long userId = userMapper.findByUsername(username).getId();
        return meetingMapper.findByParticipantId(userId);
    }
    
    @Transactional
    public void respondToMeeting(String username, Long meetingId, String response) {
        Long userId = userMapper.findByUsername(username).getId();
        meetingMapper.updateParticipantStatus(meetingId, userId, response);
    }
    
    public List<Meeting> getUpcomingMeetings(LocalDate date, LocalTime time) {
        return meetingMapper.findUpcoming(date, time);
    }
    
    public List<User> getMeetingParticipants(Long meetingId) {
        return meetingMapper.findParticipants(meetingId);
    }
} 