package com.jiayou.pets.service;

import java.util.HashMap;

import com.jiayou.pets.dto.user.LoginRequest;
import com.jiayou.pets.pojo.User;
import com.jiayou.pets.response.ResponseEntity;

public interface UserService {
    // 用户注册方法
    ResponseEntity<HashMap<String,Object>> register(User user);

    // 用户登录方法
    ResponseEntity<HashMap<String,Object>> login(LoginRequest request);
}
