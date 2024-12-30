package com.schedule.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private String phone;
    private String email;
} 