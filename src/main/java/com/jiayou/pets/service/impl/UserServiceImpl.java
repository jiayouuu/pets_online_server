package com.jiayou.pets.service.impl;

import com.jiayou.pets.pojo.User;
import com.jiayou.pets.service.UserService;
import com.jiayou.pets.utils.Encrypt;
import com.jiayou.pets.dao.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private  final UserMapper userMapper;
    private final ValidateServiceImpl emailService;
    public  UserServiceImpl(UserMapper userMapper, ValidateServiceImpl emailService) {
        this.userMapper = userMapper;
        this.emailService = emailService;
    }
    @Override
    public Integer register(User user, String verificationCode) {
        // 检查用户是否已存在
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("邮箱已经注册，请登录");
        }
        // 验证验证码
        if(!emailService.validateCode(user.getEmail(), verificationCode)){
            throw new RuntimeException("验证码错误");
        }
        // 密码加密
        user.setPassword(Encrypt.hashPassword(user.getPassword()));
        // 保存新用户
        Integer count = userMapper.insert(user);
        if (count == 0) {
            throw new RuntimeException("注册失败");
        }
        return count;
    }

    @Override
    public boolean login(User user) {
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser == null) {
            throw new RuntimeException("邮箱未注册");
        }
        if (!Encrypt.checkPassword(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return true;
    }
}