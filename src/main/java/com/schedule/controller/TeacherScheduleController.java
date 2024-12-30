package com.schedule.controller;

import com.schedule.common.Result;
import com.schedule.dto.ScheduleDTO;
import com.schedule.entity.Schedule;
import com.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher/schedules")
public class TeacherScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @GetMapping
    @Operation(summary = "获取教师日程列表")
    public Result<List<ScheduleDTO>> getSchedules(HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            List<Schedule> schedules = scheduleService.getTeacherSchedules(username);
            List<ScheduleDTO> dtos = schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return Result.success(dtos);
        } catch (Exception e) {
            return Result.error("获取日程失败：" + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新日程")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "403", description = "无权限修改此日程")
    })
    public Result<ScheduleDTO> updateSchedule(
        @Parameter(description = "日程ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "日程信息", required = true)
        @RequestBody @Valid ScheduleDTO dto,
        HttpServletRequest request
    ) {
        try {
            String username = (String) request.getAttribute("username");
            System.out.println("正在更新日程，ID: " + id + ", 用户: " + username);
            System.out.println("更新内容: " + dto);
            
            Schedule schedule = convertToEntity(dto);
            scheduleService.updateTeacherSchedule(username, id, schedule);
            
            // 获取更新后的数据
            Schedule updatedSchedule = scheduleService.findById(id);
            ScheduleDTO updatedDto = convertToDTO(updatedSchedule);
            
            return Result.success(updatedDto);
        } catch (Exception e) {
            System.err.println("更新日程失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("更新日程失败：" + e.getMessage());
        }
    }
    
    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);
        return dto;
    }
    
    private Schedule convertToEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(dto, schedule);
        return schedule;
    }
} 