package com.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 日程实体类
 * 用于存储用户的日程信息
 */
@Data
public class Schedule {
    /** 日程ID */
    private Long id;
    
    /** 用户ID（日程所属用户） */
    private Long userId;
    
    /** 日程日期 */
    private LocalDate date;
    
    /** 日程时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    
    /** 事件类型（meeting-会议, exam-考试, personal-个人事务） */
    private String eventType;
    
    /** 日程标题 */
    private String title;
    
    /** 日程内容详情 */
    private String content;
} 