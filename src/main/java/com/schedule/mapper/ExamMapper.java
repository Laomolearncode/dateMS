package com.schedule.mapper;

import com.schedule.entity.Exam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Mapper
public interface ExamMapper {
    @Select("SELECT * FROM exam")
    List<Exam> findAll();

    @Select("SELECT * FROM exam WHERE supervisor_id = #{supervisorId}")
    List<Exam> findBySupervisorId(Long supervisorId);

    @Insert("INSERT INTO exam(name, location, supervisor_id, date, time, duration) " +
            "VALUES(#{name}, #{location}, #{supervisorId}, #{date}, #{time}, #{duration})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Exam exam);

    @Update("UPDATE exam SET " +
            "name = #{name}, " +
            "location = #{location}, " +
            "supervisor_id = #{supervisorId}, " +
            "date = #{date}, " +
            "time = #{time}, " +
            "duration = #{duration} " +
            "WHERE id = #{id}")
    void update(Exam exam);

    @Delete("DELETE FROM exam WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT * FROM exam WHERE date = #{date} AND time <= #{time}")
    List<Exam> findUpcoming(LocalDate date, LocalTime time);

    @Select("SELECT * FROM exam WHERE id = #{id}")
    Exam findById(Long id);
} 