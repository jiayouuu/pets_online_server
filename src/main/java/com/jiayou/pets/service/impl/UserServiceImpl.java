/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 16:25:42
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-22 17:49:22
 * @Description: 用户相关接口实现
 */
package com.jiayou.pets.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiayou.pets.dao.UserMapper;
import com.jiayou.pets.pojo.User;
import com.jiayou.pets.service.UserService;
import com.jiayou.pets.utils.Encrypt;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer add(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null)
            throw new Exception("邮箱已经注册账号，请直接登录");
        user.setPassword(Encrypt.hashPassword(user.getPassword()));
        return userMapper.insert(user);

    }

}
