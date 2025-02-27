package com.jiayou.pets.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jiayou.pets.response.ResponseEntity;
import com.jiayou.pets.service.impl.ValidateServiceImpl;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ValidateServiceImpl validateServiceImpl;

    public PublicController(ValidateServiceImpl validateServiceImpl) {
        this.validateServiceImpl = validateServiceImpl;
    }

    @GetMapping("/email/code")
    public ResponseEntity<HashMap<String, Object>> sendCode(@RequestParam String email) {
        try {
            return validateServiceImpl.sendEmailCode(email);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }

    @GetMapping("/imgCode")
    public ResponseEntity<HashMap<String, Object>> sendImgCode() {
        try {
            return validateServiceImpl.sendImgCode();
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }

    @PostMapping("/validateImgCode")
    public ResponseEntity<HashMap<String, Object>> validateImgCode(@RequestBody HashMap<String, Object> map) {
        try {
            String id = (String) map.get("id");
            String code = (String) map.get("code");
            return validateServiceImpl.validateImgCode(id, code);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }

    @PostMapping("/validateEmailCode")
    public ResponseEntity<HashMap<String, Object>> validateEmailCode(@RequestBody HashMap<String, Object> map) {
        try {
            String email = (String) map.get("email");
            String code = (String) map.get("code");
            return validateServiceImpl.validateEmailCode(email, code);
        } catch (Exception e) {
            return ResponseEntity.error(500, e.getMessage());
        }
    }
}