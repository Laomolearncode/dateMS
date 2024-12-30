package com.schedule.controller;

import com.schedule.common.Result;
import com.schedule.entity.User;
import com.schedule.service.UserService;
import com.schedule.utils.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 认证控制器
 * 处理用户登录等认证相关操作
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     * @param request 登录请求（包含用户名和密码）
     * @return 登录结果（包含token和用户信息）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "400", description = "用户名或密码错误")
    })
    public Result<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            User user = userService.findByUsername(request.getUsername());
            if (user != null && user.getPassword().equals(request.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                data.put("username", user.getUsername());
                data.put("role", user.getRole());
                
                return Result.success(data);
            }
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}

/**
 * 登录请求DTO
 */
@Data
class LoginRequest {
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
} 