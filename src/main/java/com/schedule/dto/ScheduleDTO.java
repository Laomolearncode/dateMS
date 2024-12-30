package com.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "日程信息")
public class ScheduleDTO {
    @Schema(description = "日程ID")
    private Long id;
    
    @Schema(description = "日期", example = "2024-12-29")
    private LocalDate date;
    
    @Schema(description = "时间", example = "14:30:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    
    @Schema(description = "标题", example = "会议")
    private String title;
    
    @Schema(description = "内容", example = "项目进度讨论")
    private String content;
    
    @Schema(description = "事件类型", example = "meeting")
    private String eventType;
} 