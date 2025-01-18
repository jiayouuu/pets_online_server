/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 16:25:42
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-18 20:39:04
 * @Description: 用户相关接口实现
 */
package com.jiayou.pets.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiayou.pets.dao.UserMapper;
import com.jiayou.pets.pojo.User;
import com.jiayou.pets.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User add(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", user.getNickname());
        List<User> u = userMapper.selectList(queryWrapper);
        if (u != null) throw new Exception("用户名已存在");
        userMapper.insert(user);
        return user;

    }

}
