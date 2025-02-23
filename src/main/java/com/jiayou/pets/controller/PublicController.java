package com.jiayou.pets.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiayou.pets.response.ResponseEntity;
import com.jiayou.pets.service.impl.ValidateServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ValidateServiceImpl validateServiceImpl;

    public PublicController(ValidateServiceImpl validateServiceImpl) {
        this.validateServiceImpl = validateServiceImpl;
    }

    @GetMapping("/email/code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        try {
            boolean flag = validateServiceImpl.sendEmailCode(email);
            return flag ? ResponseEntity.success(null) : ResponseEntity.error(400, "发送失败");
        } catch (Exception e) {
            return ResponseEntity.error(400, e.getMessage());
        }
    }

    @GetMapping("/imgCode")
    public ResponseEntity<Map<String,String>> sendImgCode() {
        try {
            return ResponseEntity.success(validateServiceImpl.sendImgCode());
        } catch (Exception e) {
            return ResponseEntity.error(400, e.getMessage());
        }
    }

    @GetMapping("/validateImgCode")
    public ResponseEntity<String> validateImgCode(@RequestParam String code,@RequestParam String id) {
        try {
            boolean flag = validateServiceImpl.validateCode(id, code);
            return ResponseEntity.success(flag ? "1" : "0");
        } catch (Exception e) {
            return ResponseEntity.error(400, e.getMessage());
        }
    }

}