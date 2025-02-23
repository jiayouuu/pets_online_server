package com.jiayou.pets.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jiayou.pets.pojo.User;
import com.jiayou.pets.response.ResponseEntity;
import com.jiayou.pets.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@ModelAttribute User user, String code) {
        try {
            return ResponseEntity.success(userService.register(user, code));
        } catch (Exception e) {
            return ResponseEntity.error(400, e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Integer> postMethodName(@ModelAttribute User user) {
        try {
            userService.login(user);
            return ResponseEntity.success(1);
        } catch (Exception e) {
            return ResponseEntity.error(400, e.getMessage());
        }
    }
}
