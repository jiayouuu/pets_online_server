package com.jiayou.pets.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiayou.pets.pojo.User;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 根据邮箱查找用户
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
    
}
