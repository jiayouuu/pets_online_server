package com.jiayou.pets.service;

import java.util.Map;

public interface ValidateService {
    boolean sendEmailCode(String email);

    Map<String, String> sendImgCode();

    boolean validateCode(String key, String code);
}
