package com.jiayou.pets.service;

import com.jiayou.pets.pojo.User;

public interface UserService {
    // 用户注册方法
    Integer register(User user, String verificationCode);

    // 用户登录方法
    boolean login(User user);
}
