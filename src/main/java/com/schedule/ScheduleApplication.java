package com.schedule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.schedule.entity.User;
import com.schedule.service.UserService;
import com.schedule.service.ScheduleService;
import com.schedule.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 日程管理系统启动程序
 */
@SpringBootApplication
@MapperScan("com.schedule.mapper")
@EnableTransactionManagement
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "日程管理系统API",
        version = "1.0.0",
        description = "日程管理系统的所有接口文档"
    )
)
public class ScheduleApplication implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    public static void main(String[] args) {
        try {
            SpringApplication.run(ScheduleApplication.class, args);
            System.out.println("==========日程管理系统启动成功==========");
        } catch (Exception e) {
            System.err.println("==========日程管理系统启动失败==========");
            e.printStackTrace();
        }
    }
    
    @Override
    public void run(String... args) {
        System.out.println("正在执行系统初始化...");
        try {
            // 创建管理员账户
            if (userService.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("123456");
                admin.setRole("admin");
                admin.setRoleId(1);
                admin.setPhone("13800138000");
                admin.setEmail("admin@example.com");
                userService.createUser(admin);
                System.out.println("已创建默认管理员账户，ID为：" + admin.getId());
            }

            // 创建教师账户
            if (userService.findByUsername("teacher") == null) {
                User teacher = new User();
                teacher.setUsername("teacher");
                teacher.setPassword("123456");
                teacher.setRole("teacher");
                teacher.setRoleId(2);
                teacher.setPhone("13800138001");
                teacher.setEmail("teacher@example.com");
                userService.createUser(teacher);
                System.out.println("已创建默认教师账户，ID为：" + teacher.getId());
            }

            // 创建监考教师账户
            if (userService.findByUsername("supervisor") == null) {
                User supervisor = new User();
                supervisor.setUsername("supervisor");
                supervisor.setPassword("123456");
                supervisor.setRole("teacher");
                supervisor.setRoleId(2);
                supervisor.setPhone("13800138002");
                supervisor.setEmail("supervisor@example.com");
                userService.createUser(supervisor);
                System.out.println("已创建监考教师账户，ID为：" + supervisor.getId());
            }

            System.out.println("系统初始化完成");
        } catch (Exception e) {
            System.err.println("系统初始化失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
} 