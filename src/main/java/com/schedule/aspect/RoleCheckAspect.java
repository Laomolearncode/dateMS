package com.schedule.aspect;

import com.schedule.annotation.RequireRole;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userRole = (String) request.getAttribute("role");

        if (userRole == null || !userRole.equals(requireRole.value())) {
            throw new RuntimeException("无权限访问");
        }

        return joinPoint.proceed();
    }
} 