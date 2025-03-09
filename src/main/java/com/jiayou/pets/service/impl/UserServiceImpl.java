package com.jiayou.pets.service.impl;

import com.jiayou.pets.pojo.User;
import com.jiayou.pets.dto.response.ResEntity;
import com.jiayou.pets.service.UserService;
import com.jiayou.pets.utils.Encrypt;
import com.jiayou.pets.utils.JwtUtil;
import com.jiayou.pets.dao.UserMapper;
import com.jiayou.pets.dto.user.LoginReq;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper,JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResEntity<HashMap<String, Object>> register(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenEmail = authentication.getName();
        if (!tokenEmail.equals(user.getEmail())) {
            return ResEntity.error(400, "邮箱不匹配,请重试");
        }
        HashMap<String, Object> map = new HashMap<>();
        // 检查用户是否已存在
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResEntity.error(400, "邮箱已经注册，请登录");
        }
        // 密码加密
        user.setPassword(Encrypt.hashPassword(user.getPassword()));
        // 保存新用户
        if (0 == userMapper.insert(user)) {
            return ResEntity.error(400, "注册失败");
        }
        return ResEntity.success(map);
    }

    @Override
    public ResEntity<HashMap<String, Object>> login(LoginReq request) {
        HashMap<String, Object> map = new HashMap<>();
        User existUser = userMapper.findByEmail(request.getEmail());
        if (existUser == null) {
            return ResEntity.error(400, "邮箱未注册");
        }
        if (!Encrypt.checkPassword(request.getPassword(), existUser.getPassword())) {
            return ResEntity.error(400, "密码错误");
        }
        String token;
        if (request.isRemember()) {
            token = jwtUtil.generateToken(new HashMap<>(){{
                put("email", request.getEmail());
                put("userId",existUser.getUserId());
            }}, 7, TimeUnit.DAYS);
        } else {
            token = jwtUtil.generateToken(new HashMap<>(){{
                put("email", request.getEmail());
                put("userId",existUser.getUserId());
            }}, 6, TimeUnit.HOURS);
        }
        map.put("token",token);
        return ResEntity.success(map);
    }
}