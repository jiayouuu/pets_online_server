package com.jiayou.pets.service.impl;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jiayou.pets.service.ValidateService;
import com.jiayou.pets.utils.EmailUtil;
import com.jiayou.pets.utils.RedisUtil;

@Service
public class ValidateServiceImpl implements ValidateService {
    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;

    @Autowired
    private DefaultKaptcha kaptcha;

    public ValidateServiceImpl(EmailUtil emailUtil, RedisUtil redisUtil) {
        this.emailUtil = emailUtil;
        this.redisUtil = redisUtil;
    }

    private String generateBumberCode() {
        return String.format("%06d", (int) (Math.random() * 999999));
    }

    private String generateStringCode(int length) {
        Random random = new Random();
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    @Override
    public boolean sendEmailCode(String email) {
        String preCode = (String) redisUtil.get(email);
        if (preCode != null) {
            long expire = redisUtil.getExpire(email, java.util.concurrent.TimeUnit.SECONDS);
            if (expire > 0) {
                return false;
            }
        }
        String code = generateBumberCode();
        redisUtil.setWithExpire(email, code, 60, java.util.concurrent.TimeUnit.SECONDS);
        return emailUtil.sendSimpleEmail(email, "验证码", String.format("您的验证码为：%s，有效期1分钟，请勿泄露。", code));
    }

    @Override
    public boolean validateCode(String key, String code) {
        String preCode = (String) redisUtil.get(key);
        boolean flag = preCode != null && preCode.toLowerCase().equals(code.toLowerCase());
        if (flag) {
            redisUtil.delete(key);
        }
        return flag;
    }

    @Override
    public Map<String, String> sendImgCode() {
        // 生成验证码
        String code = generateStringCode(5);
        // 创建验证码图片
        BufferedImage image = kaptcha.createImage(code);
        // 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 生成一个 UUID
        String tempId = UUID.randomUUID().toString();
        HashMap<String, String> map = new HashMap<>();
        try {
            ImageIO.write(image, "png", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            String imgData = "data:image/png;base64," + base64Image;
            redisUtil.setWithExpire(tempId, code, 30, java.util.concurrent.TimeUnit.MINUTES);
            map.put("id", tempId);
            map.put("img", imgData);
        } catch (Exception e) { 
            map.put("info", e.getMessage());
        }
        return map;
    }

}
