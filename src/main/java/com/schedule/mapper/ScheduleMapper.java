package com.schedule.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.schedule.entity.Schedule;

/**
 * 日程数据访问接口
 * 处理日程相关的数据库操作
 */
@Mapper
public interface ScheduleMapper {
    /**
     * 根据用户ID查找日程
     * @param userId 用户ID
     * @return 日程列表
     */
    @Select("SELECT * FROM schedule WHERE user_id = #{userId}")
    List<Schedule> findByUserId(Long userId);
    
    @Select("SELECT * FROM schedule WHERE id = #{id}")
    Schedule findById(Long id);
    
    /**
     * 插入新日程
     * @param schedule 日程信息
     */
    @Insert("INSERT INTO schedule(user_id, date, time, event_type, title, content) " +
            "VALUES(#{userId}, #{date}, #{time}, #{eventType}, #{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Schedule schedule);
    
    @Update("UPDATE schedule SET date=#{date}, time=#{time}, " +
            "event_type=#{eventType}, title=#{title}, content=#{content} " +
            "WHERE id=#{id} AND user_id=#{userId}")
    void update(Schedule schedule);
    
    @Delete("DELETE FROM schedule WHERE id = #{id}")
    void delete(Long id);
    
    @Select("SELECT * FROM schedule WHERE date = #{date} AND time <= #{time}")
    List<Schedule> findUpcoming(LocalDate date, LocalTime time);
    
    @Select("SELECT s.* FROM schedule s " +
            "JOIN user u ON s.user_id = u.id " +
            "WHERE u.username = #{username}")
    List<Schedule> findByUsername(String username);
    
    @Select("SELECT id FROM user WHERE username = #{username}")
    Long findUserIdByUsername(String username);
} 