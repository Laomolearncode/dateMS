package com.schedule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 通知服务类
 * 处理邮件和短信通知的发送
 */
@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    /**
     * 发送邮件通知
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendEmailNotification(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            logger.info("邮件发送成功：{} -> {}", fromEmail, to);
        } catch (MailAuthenticationException e) {
            logger.error("邮件认证失败，请检查邮箱配置和授权码: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("邮件发送失败: {}", e.getMessage());
        }
    }
    
    /**
     * 发送短信通知
     * @param phone 手机号码
     * @param message 短信内容
     */
    public void sendSmsNotification(String phone, String message) {
        // TODO: 实现短信发送逻辑
        // 这里可以集成第三方短信服务，如阿里云短信服务
        System.out.println("Sending SMS to " + phone + ": " + message);
    }
} 