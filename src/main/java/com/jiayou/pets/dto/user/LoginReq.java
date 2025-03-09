package com.jiayou.pets.dto.user;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String password;
    private boolean remember;
}
