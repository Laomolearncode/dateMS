package com.schedule.config;

import com.schedule.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行登录接口
        if (request.getRequestURI().contains("/api/auth/login")) {
            return true;
        }

        // 获取token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 验证token
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 将用户信息添加到请求属性中
        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));
        request.setAttribute("role", jwtUtil.getRoleFromToken(token));
        
        return true;
    }
} 