package com.schedule.service;

import com.schedule.entity.Schedule;
import com.schedule.entity.Exam;
import com.schedule.entity.Meeting;
import com.schedule.entity.User;
import com.schedule.mapper.UserMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleReminderService {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private ExamService examService;
    
    @Autowired
    private MeetingService meetingService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserMapper userMapper;

    // 每小时检查一次即将到来的日程
    // @Scheduled(fixedRate = 3600000)  // 注释掉这行来禁用定时任务
    public void checkUpcomingSchedules() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(24); // 提前24小时提醒
        
        // 检查个人日程
        checkPersonalSchedules(now, reminderTime);
        
        // 检查监考安排
        checkExams(now, reminderTime);
        
        // 检查会议安排
        checkMeetings(now, reminderTime);
    }
    
    private void checkPersonalSchedules(LocalDateTime now, LocalDateTime reminderTime) {
        List<Schedule> schedules = scheduleService.getUpcomingSchedules(
                reminderTime.toLocalDate(), 
                reminderTime.toLocalTime());
                
        for (Schedule schedule : schedules) {
            User user = userMapper.findById(schedule.getUserId());
            if (user != null) {
                // 发送邮件提醒
                String subject = "日程提醒";
                String content = String.format("您有一个日程即将开始：\n标题：%s\n时间：%s %s\n内容：%s",
                        schedule.getTitle(),
                        schedule.getDate(),
                        schedule.getTime(),
                        schedule.getContent());
                        
                notificationService.sendEmailNotification(user.getEmail(), subject, content);
                
                // 如果配置了手机号，发送短信提醒
                if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                    String message = String.format("日程提醒：%s，时间：%s %s",
                            schedule.getTitle(),
                            schedule.getDate(),
                            schedule.getTime());
                    notificationService.sendSmsNotification(user.getPhone(), message);
                }
            }
        }
    }
    
    private void checkExams(LocalDateTime now, LocalDateTime reminderTime) {
        List<Exam> exams = examService.getUpcomingExams(
                reminderTime.toLocalDate(), 
                reminderTime.toLocalTime());
                
        for (Exam exam : exams) {
            User supervisor = userMapper.findById(exam.getSupervisorId());
            if (supervisor != null) {
                String subject = "监考提醒";
                String content = String.format("您有一个监考任务即将开始：\n考试：%s\n地点：%s\n时间：%s %s\n时长：%d分钟",
                        exam.getName(),
                        exam.getLocation(),
                        exam.getDate(),
                        exam.getTime(),
                        exam.getDuration());
                        
                notificationService.sendEmailNotification(supervisor.getEmail(), subject, content);
                
                if (supervisor.getPhone() != null && !supervisor.getPhone().isEmpty()) {
                    String message = String.format("监考提醒：%s，地点：%s，时间：%s %s",
                            exam.getName(),
                            exam.getLocation(),
                            exam.getDate(),
                            exam.getTime());
                    notificationService.sendSmsNotification(supervisor.getPhone(), message);
                }
            }
        }
    }
    
    private void checkMeetings(LocalDateTime now, LocalDateTime reminderTime) {
        List<Meeting> meetings = meetingService.getUpcomingMeetings(
                reminderTime.toLocalDate(), 
                reminderTime.toLocalTime());
                
        for (Meeting meeting : meetings) {
            // 获取所有参会人员
            List<User> participants = meetingService.getMeetingParticipants(meeting.getId());
            
            for (User participant : participants) {
                String subject = "会议提醒";
                String content = String.format("您有一个会议即将开始：\n主题：%s\n地点：%s\n时间：%s %s\n时长：%d分钟",
                        meeting.getTitle(),
                        meeting.getLocation(),
                        meeting.getDate(),
                        meeting.getTime(),
                        meeting.getDuration());
                        
                notificationService.sendEmailNotification(participant.getEmail(), subject, content);
                
                if (participant.getPhone() != null && !participant.getPhone().isEmpty()) {
                    String message = String.format("会议提醒：%s，地点：%s，时间：%s %s",
                            meeting.getTitle(),
                            meeting.getLocation(),
                            meeting.getDate(),
                            meeting.getTime());
                    notificationService.sendSmsNotification(participant.getPhone(), message);
                }
            }
        }
    }
} 