package com.schedule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // 配置LocalDate的序列化和反序列化
        javaTimeModule.addSerializer(LocalDate.class, 
            new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, 
            new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
            
        // 配置LocalTime的序列化和反序列化
        javaTimeModule.addSerializer(LocalTime.class, 
            new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, 
            new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
 