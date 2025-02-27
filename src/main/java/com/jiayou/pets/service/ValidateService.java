package com.jiayou.pets.service;

import java.util.HashMap;
import com.jiayou.pets.response.ResponseEntity;
public interface ValidateService {
    ResponseEntity<HashMap<String,Object>> sendEmailCode(String email);
    ResponseEntity<HashMap<String,Object>> sendImgCode();
    ResponseEntity<HashMap<String,Object>> validateImgCode(String id, String code);
    ResponseEntity<HashMap<String,Object>> validateEmailCode(String id, String code);
}
