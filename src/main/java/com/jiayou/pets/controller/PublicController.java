package com.jiayou.pets.controller;

import com.jiayou.pets.service.ValidateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jiayou.pets.dto.response.ResEntity;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ValidateService validateService;

    public PublicController(ValidateService validateService) {
        this.validateService = validateService;
    }

    @GetMapping("/email/code")
    public ResEntity<HashMap<String, Object>> sendCode(@RequestParam String email) {
        try {
            return validateService.sendEmailCode(email);
        } catch (Exception e) {
            return ResEntity.error(500, e.getMessage());
        }
    }

    @GetMapping("/imgCode")
    public ResEntity<HashMap<String, Object>> sendImgCode() {
        try {
            return validateService.sendImgCode();
        } catch (Exception e) {
            return ResEntity.error(500, e.getMessage());
        }
    }

    @PostMapping("/validateImgCode")
    public ResEntity<HashMap<String, Object>> validateImgCode(@RequestBody HashMap<String, Object> map) {
        try {
            String id = (String) map.get("id");
            String code = (String) map.get("code");
            return validateService.validateImgCode(id, code);
        } catch (Exception e) {
            return ResEntity.error(500, e.getMessage());
        }
    }

    @PostMapping("/validateEmailCode")
    public ResEntity<HashMap<String, Object>> validateEmailCode(@RequestBody HashMap<String, Object> map) {
        try {
            String email = (String) map.get("email");
            String code = (String) map.get("code");
            return validateService.validateEmailCode(email, code);
        } catch (Exception e) {
            return ResEntity.error(500, e.getMessage());
        }
    }
}