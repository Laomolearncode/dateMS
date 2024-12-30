package com.schedule.entity;

import lombok.Data;

/**
 * 用户实体类
 * 存储系统用户信息，包括管理员和教师
 */
@Data
public class User {
    /** 用户ID */
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 密码 */
    private String password;
    
    /** 角色（admin-管理员, teacher-教师） */
    private String role;
    
    /** 角色ID（1-管理员, 2-教师） */
    private Integer roleId;
    
    /** 手机号码 */
    private String phone;
    
    /** 电子邮箱 */
    private String email;
} 