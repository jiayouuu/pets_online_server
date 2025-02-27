package com.jiayou.pets.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jiayou.pets.dto.user.LoginRequest;
import com.jiayou.pets.pojo.User;
import com.jiayou.pets.response.ResponseEntity;
import com.jiayou.pets.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(@RequestBody User user) {
        try {
            return userService.register(user);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> postMethodName(@RequestBody LoginRequest request) {
        try {
            return userService.login(request);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }
}
