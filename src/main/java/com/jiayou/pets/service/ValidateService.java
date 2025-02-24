package com.jiayou.pets.service;

import java.util.HashMap;
import com.jiayou.pets.response.ResponseEntity;
public interface ValidateService {
    boolean sendEmailCode(String email);
    ResponseEntity<HashMap<String,Object>> sendImgCode();

    boolean validateCode(String key, String code);
}
