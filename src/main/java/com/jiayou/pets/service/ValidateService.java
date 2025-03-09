package com.jiayou.pets.service;

import java.util.HashMap;
import com.jiayou.pets.dto.response.ResEntity;
public interface ValidateService {
    ResEntity<HashMap<String,Object>> sendEmailCode(String email);
    ResEntity<HashMap<String,Object>> sendImgCode();
    ResEntity<HashMap<String,Object>> validateImgCode(String id, String code);
    ResEntity<HashMap<String,Object>> validateEmailCode(String id, String code);
}
