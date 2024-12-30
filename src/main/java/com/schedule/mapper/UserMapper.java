package com.schedule.mapper;

import com.schedule.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO user(username, password, role, role_id, phone, email) " +
            "VALUES(#{username}, #{password}, #{role}, #{roleId}, #{phone}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Update("UPDATE user SET username=#{username}, password=#{password}, " +
            "role=#{role}, role_id=#{roleId}, phone=#{phone}, email=#{email} " +
            "WHERE id=#{id}")
    void update(User user);

    @Delete("DELETE FROM user WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM user WHERE role = #{role}")
    List<User> findByRole(String role);
} 