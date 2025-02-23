package com.jiayou.pets.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encrypt {
    /**
     * 使用BCrypt加密密码
     * 
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 验证密码是否匹配
     * 
     * @param password       明文密码
     * @param hashedPassword 加密后的密码
     * @return 如果密码匹配，返回true；否则返回false
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        if (password == null || password.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("密码和加密后的密码均不能为空");
        }
        return BCrypt.checkpw(password, hashedPassword);
    }

}
