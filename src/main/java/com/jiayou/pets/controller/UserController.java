/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 15:35:06
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-18 20:35:58
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
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("add")
    public ResponseEntity<User> add(@ModelAttribute User user) {
        try {
            User savedUser = userService.add(user);
            return ResponseEntity.success(savedUser);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }
}
