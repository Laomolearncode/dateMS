package com.schedule.entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 会议实体类
 * 存储会议相关信息
 */
public class Meeting {
    /** 会议ID */
    private Long id;
    
    /** 会议标题 */
    private String title;
    
    /** 会议内容/议程 */
    private String content;
    
    /** 会议地点 */
    private String location;
    
    /** 组织者ID */
    private Long organizerId;
    
    /** 会议日期 */
    private LocalDate date;
    
    /** 会议时间 */
    private LocalTime time;
    
    /** 会议时长（分钟） */
    private Integer duration;

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getLocation() { return location; }
    public Long getOrganizerId() { return organizerId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public Integer getDuration() { return duration; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setLocation(String location) { this.location = location; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setDuration(Integer duration) { this.duration = duration; }
} 