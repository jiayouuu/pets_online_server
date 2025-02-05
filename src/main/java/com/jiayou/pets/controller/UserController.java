/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 15:35:06
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-22 17:56:06
 * @Description: 用户相关控制器
 */
package com.jiayou.pets.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jiayou.pets.pojo.User;
import com.jiayou.pets.response.ResponseEntity;
import com.jiayou.pets.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public  ResponseEntity<Integer> register(@ModelAttribute User user) {
        try {
            int addCount = userService.add(user);
            return ResponseEntity.success(addCount);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }
}
