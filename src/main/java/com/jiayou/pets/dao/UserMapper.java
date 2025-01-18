/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 16:19:37
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-18 20:30:42
 * @Description: 用户
 */
package com.jiayou.pets.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiayou.pets.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
