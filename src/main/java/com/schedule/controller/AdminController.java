package com.schedule.controller;

import com.schedule.annotation.RequireRole;
import com.schedule.common.Result;
import com.schedule.dto.ExamDTO;
import com.schedule.entity.User;
import com.schedule.entity.Exam;
import com.schedule.entity.Meeting;
import com.schedule.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;

    // 用户管理接口
    @RequireRole("admin")
    @GetMapping("/users")
    @Operation(summary = "获取所有用户")
    public Result<List<User>> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }

    @RequireRole("admin")
    @PostMapping("/users")
    @Operation(summary = "创建用户")
    public Result<User> createUser(@RequestBody User user) {
        try {
            // 先检查用户名是否存在
            User existingUser = adminService.findByUsername(user.getUsername());
            if (existingUser != null) {
                return Result.error("用户名 '" + user.getUsername() + "' 已存在");
            }

            // 创建用户
            adminService.createUser(user);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("创建用户失败：" + e.getMessage());
        }
    }

    @RequireRole("admin")
    @PutMapping("/users/{id}")
    @Operation(summary = "更新用户信息")
    public Result<User> updateUser(
        @Parameter(description = "用户ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "用户信息", required = true)
        @RequestBody User user
    ) {
        try {
            // 检查用户是否存在
            User existingUser = adminService.findById(id);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            // 如果要修改用户名，检查新用户名是否已存在
            if (!existingUser.getUsername().equals(user.getUsername()) && 
                adminService.findByUsername(user.getUsername()) != null) {
                return Result.error("新用户名已存在");
            }

            user.setId(id);  // 确保ID正确
            adminService.updateUser(user);
            
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("更新用户失败：" + e.getMessage());
        }
    }

    @RequireRole("admin")
    @DeleteMapping("/users/{id}")
    @Operation(summary = "删除用户")
    public Result<?> deleteUser(
        @Parameter(description = "用户ID", required = true)
        @PathVariable Long id
    ) {
        try {
            // 检查用户是否存在
            User existingUser = adminService.findById(id);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            // 不允许删除admin用户
            if ("admin".equals(existingUser.getRole())) {
                return Result.error("不能删除管理员用户");
            }

            adminService.deleteUser(id);
            return Result.success("用户删除成功");
        } catch (Exception e) {
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }

    // 监考管理接口
    @RequireRole("admin")
    @GetMapping("/exams")
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(adminService.getAllExams());
    }

    @RequireRole("admin")
    @PostMapping("/exams")
    @Operation(summary = "创建考试")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "403", description = "无权限创建")
    })
    public Result<ExamDTO> createExam(
        @Parameter(description = "考试信息", required = true)
        @RequestBody @Valid ExamDTO examDTO
    ) {
        try {
            System.out.println("正在创建考试");
            System.out.println("创建内容: " + examDTO);

            // 转换DTO为实体
            Exam exam = convertToEntity(examDTO);
            
            // 创建考试
            adminService.createExam(exam);
            
            // 获取创建后的数据（包含ID）
            ExamDTO createdDto = convertToDTO(exam);
            
            return Result.success(createdDto);
        } catch (Exception e) {
            System.err.println("创建考试失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("创建考试失败：" + e.getMessage());
        }
    }

    // DTO转换方法
    private ExamDTO convertToDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        BeanUtils.copyProperties(exam, dto);
        return dto;
    }

    private Exam convertToEntity(ExamDTO dto) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(dto, exam);
        return exam;
    }

    @RequireRole("admin")
    @DeleteMapping("/exams/{id}")
    @Operation(summary = "删除考试")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "403", description = "无权限删除"),
        @ApiResponse(responseCode = "404", description = "考试不存在")
    })
    public Result<?> deleteExam(
        @Parameter(description = "考试ID", required = true)
        @PathVariable Long id
    ) {
        try {
            // 检查考试是否存在
            Exam existingExam = adminService.findExamById(id);
            if (existingExam == null) {
                return Result.error("考试不存在");
            }

            // 删除考试
            adminService.deleteExam(id);
            return Result.success("考试删除成功");
        } catch (Exception e) {
            System.err.println("删除考试失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("删除考试失败：" + e.getMessage());
        }
    }

    // 会议管理接口
    @RequireRole("admin")
    @GetMapping("/meetings")
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        return ResponseEntity.ok(adminService.getAllMeetings());
    }

    @RequireRole("admin")
    @PostMapping("/meetings")
    @Operation(summary = "创建会议")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "403", description = "无权限创建")
    })
    public Result<?> createMeeting(
        @Parameter(description = "会议信息", required = true)
        @RequestBody MeetingRequest request
    ) {
        try {
            System.out.println("正在创建会议：" + request.getMeeting().getTitle());
            System.out.println("参会人员：" + request.getParticipantIds());

            // 验证组织者是否存在
            User organizer = adminService.findById(request.getMeeting().getOrganizerId());
            if (organizer == null) {
                return Result.error("组织者不存在");
            }

            // 验证参会人员是否都存在
            for (Long userId : request.getParticipantIds()) {
                if (adminService.findById(userId) == null) {
                    return Result.error("参会人员ID无效：" + userId);
                }
            }

            // 创建会议
            adminService.createMeeting(request.getMeeting(), request.getParticipantIds());
            
            return Result.success("会议创建成功");
        } catch (Exception e) {
            System.err.println("创建会议失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("创建会议失败：" + e.getMessage());
        }
    }

    @RequireRole("admin")
    @PutMapping("/meetings/{id}")
    @Operation(summary = "更新会议信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "403", description = "无权限修改"),
        @ApiResponse(responseCode = "404", description = "会议不存在")
    })
    public Result<?> updateMeeting(
        @Parameter(description = "会议ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "会议信息", required = true)
        @RequestBody MeetingRequest request
    ) {
        try {
            System.out.println("正在更新会议，ID: " + id);
            System.out.println("更新内容: " + request.getMeeting());
            System.out.println("参会人员: " + request.getParticipantIds());

            // 检查会议是否存在
            Meeting existingMeeting = adminService.findMeetingById(id);
            if (existingMeeting == null) {
                return Result.error("会议不存在");
            }

            // 验证组织者是否存在
            User organizer = adminService.findById(request.getMeeting().getOrganizerId());
            if (organizer == null) {
                return Result.error("组织者不存在");
            }

            // 验证参会人员是否都存在
            for (Long userId : request.getParticipantIds()) {
                if (adminService.findById(userId) == null) {
                    return Result.error("参会人员ID无效：" + userId);
                }
            }

            // 设置会议ID
            request.getMeeting().setId(id);
            
            // 更新会议
            adminService.updateMeeting(request.getMeeting(), request.getParticipantIds());
            
            return Result.success("会议更新成功");
        } catch (Exception e) {
            System.err.println("更新会议失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("更新会议失败：" + e.getMessage());
        }
    }

    @RequireRole("admin")
    @DeleteMapping("/meetings/{id}")
    @Operation(summary = "删除会议")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "403", description = "无权限删除"),
        @ApiResponse(responseCode = "404", description = "会议不存在")
    })
    public Result<?> deleteMeeting(
        @Parameter(description = "会议ID", required = true)
        @PathVariable Long id
    ) {
        try {
            // 检查会议是否存在
            Meeting existingMeeting = adminService.findMeetingById(id);
            if (existingMeeting == null) {
                return Result.error("会议不存在");
            }

            // 删除会议
            adminService.deleteMeeting(id);
            return Result.success("会议删除成功");
        } catch (Exception e) {
            System.err.println("删除会议失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("删除会议失败：" + e.getMessage());
        }
    }

    @RequireRole("admin")
    @GetMapping("/meetings/{id}/participants")
    public ResponseEntity<?> getMeetingParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getMeetingParticipants(id));
    }

    @RequireRole("admin")
    @GetMapping("/teachers")
    @Operation(summary = "获取所有教师")
    public Result<List<User>> getAllTeachers() {
        try {
            List<User> teachers = adminService.getAllTeachers();
            return Result.success(teachers);
        } catch (Exception e) {
            return Result.error("获取教师列表失败：" + e.getMessage());
        }
    }
}

@Data
class MeetingRequest {
    @Schema(description = "会议信息")
    private Meeting meeting;

    @Schema(description = "参会人员ID列表")
    private List<Long> participantIds;

    // Getters and Setters
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
} 