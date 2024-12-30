package com.schedule.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 考试实体类
 * 存储考试相关信息
 */
@Data
public class Exam {
    /** 考试ID */
    private Long id;
    
    /** 考试名称 */
    private String name;
    
    /** 考试地点 */
    private String location;
    
    /** 监考教师ID */
    private Long supervisorId;
    
    /** 考试日期 */
    private LocalDate date;
    
    /** 考试时间 */
    private LocalTime time;
    
    /** 考试时长（分钟） */
    private Integer duration;
} 