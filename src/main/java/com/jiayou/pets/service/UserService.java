package com.jiayou.pets.service;

import java.util.HashMap;

import com.jiayou.pets.dto.user.LoginReq;
import com.jiayou.pets.pojo.User;
import com.jiayou.pets.dto.response.ResEntity;

public interface UserService {
    // 用户注册方法
    ResEntity<HashMap<String,Object>> register(User user);

    // 用户登录方法
    ResEntity<HashMap<String,Object>> login(LoginReq request);
}
