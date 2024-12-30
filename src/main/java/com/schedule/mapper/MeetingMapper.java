package com.schedule.mapper;

import com.schedule.entity.Meeting;
import com.schedule.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Mapper
public interface MeetingMapper {
    @Select("SELECT * FROM meeting")
    List<Meeting> findAll();

    @Select("SELECT * FROM meeting WHERE id = #{id}")
    Meeting findById(Long id);

    @Select("SELECT m.* FROM meeting m " +
            "JOIN meeting_participant mp ON m.id = mp.meeting_id " +
            "WHERE mp.user_id = #{userId}")
    List<Meeting> findByParticipantId(Long userId);

    @Insert("INSERT INTO meeting(title, content, location, organizer_id, date, time, duration) " +
            "VALUES(#{title}, #{content}, #{location}, #{organizerId}, #{date}, #{time}, #{duration})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Meeting meeting);

    @Update("UPDATE meeting SET " +
            "title = #{title}, " +
            "content = #{content}, " +
            "location = #{location}, " +
            "organizer_id = #{organizerId}, " +
            "date = #{date}, " +
            "time = #{time}, " +
            "duration = #{duration} " +
            "WHERE id = #{id}")
    void update(Meeting meeting);

    @Delete("DELETE FROM meeting WHERE id = #{id}")
    void delete(Long id);

    @Insert("INSERT INTO meeting_participant(meeting_id, user_id, status) " +
            "VALUES(#{meetingId}, #{userId}, 'pending')")
    void insertParticipant(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    @Delete("DELETE FROM meeting_participant WHERE meeting_id = #{meetingId}")
    void deleteAllParticipants(Long meetingId);

    @Select("SELECT u.* FROM user u " +
            "JOIN meeting_participant mp ON u.id = mp.user_id " +
            "WHERE mp.meeting_id = #{meetingId}")
    List<User> findParticipants(Long meetingId);

    @Update("UPDATE meeting_participant SET status = #{status} " +
            "WHERE meeting_id = #{meetingId} AND user_id = #{userId}")
    void updateParticipantStatus(
        @Param("meetingId") Long meetingId, 
        @Param("userId") Long userId, 
        @Param("status") String status
    );

    @Select("SELECT * FROM meeting WHERE date = #{date} AND time <= #{time}")
    List<Meeting> findUpcoming(
        @Param("date") LocalDate date, 
        @Param("time") LocalTime time
    );
} 