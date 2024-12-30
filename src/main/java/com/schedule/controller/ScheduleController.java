package com.schedule.controller;

import com.schedule.common.Result;
import com.schedule.dto.ScheduleDTO;
import com.schedule.entity.Schedule;
import com.schedule.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 日程管理控制器
 * 处理日程的CRUD操作
 */
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "日程管理", description = "日程管理相关接口")
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    /**
     * 获取个人日程列表
     * @param request HTTP请求对象，用于获取当前登录用户信息
     * @return 日程列表
     */
    @GetMapping
    @Operation(summary = "获取个人日程列表")
    public Result<List<ScheduleDTO>> getSchedules(HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            List<Schedule> schedules = scheduleService.getPersonalSchedules(username);
            List<ScheduleDTO> dtos = schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return Result.success(dtos);
        } catch (Exception e) {
            return Result.error("获取日程失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建个人日程
     * @param dto 日程信息
     * @param request HTTP请求对象
     * @return 创建结果
     */
    @PostMapping
    @Operation(summary = "创建个人日程")
    public Result<?> createSchedule(@RequestBody @Valid ScheduleDTO dto, 
                                  HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            Schedule schedule = convertToEntity(dto);
            scheduleService.createPersonalSchedule(username, schedule);
            return Result.success();
        } catch (Exception e) {
            return Result.error("创建日程失败：" + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新日程")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "403", description = "无权限修改"),
        @ApiResponse(responseCode = "404", description = "日程不存在")
    })
    public Result<?> updateSchedule(
        @Parameter(description = "日程ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "日程信息", required = true)
        @RequestBody ScheduleDTO scheduleDTO,
        HttpServletRequest request
    ) {
        try {
            // 获取当前用户ID
            Long userId = (Long) request.getAttribute("userId");
            
            // 检查日程是否存在
            Schedule existingSchedule = scheduleService.findById(id);
            if (existingSchedule == null) {
                return Result.error("日程不存在");
            }
            
            // 检查是否有权限修改（只能修改自己的日程）
            if (!existingSchedule.getUserId().equals(userId)) {
                return Result.error("无权修改此日程");
            }

            // 设置正确的ID和用户ID
            Schedule schedule = new Schedule();
            BeanUtils.copyProperties(scheduleDTO, schedule);
            schedule.setId(id);
            schedule.setUserId(userId);
            
            // 更新日程
            scheduleService.updatePersonalSchedule(request.getAttribute("username").toString(), id, schedule);
            return Result.success("日程更新成功");
        } catch (Exception e) {
            return Result.error("更新日程失败：" + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<?> deleteSchedule(@PathVariable Long id, 
                                  HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            scheduleService.deletePersonalSchedule(username, id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除日程失败：" + e.getMessage());
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