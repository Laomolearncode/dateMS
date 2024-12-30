package com.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "考试信息")
public class ExamDTO {
    @Schema(description = "考试ID")
    private Long id;

    @Schema(description = "考试名称", example = "期末考试")
    private String name;

    @Schema(description = "考试地点", example = "教学楼101")
    private String location;

    @Schema(description = "监考教师ID")
    private Long supervisorId;

    @Schema(description = "考试日期", example = "2024-12-29")
    private LocalDate date;

    @Schema(description = "考试时间", example = "14:30:00")
    private LocalTime time;

    @Schema(description = "考试时长（分钟）", example = "120")
    private Integer duration;
} 