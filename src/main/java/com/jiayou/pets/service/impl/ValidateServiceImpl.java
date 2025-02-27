package com.jiayou.pets.service.impl;

import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jiayou.pets.response.ResponseEntity;
import com.jiayou.pets.service.ValidateService;
import com.jiayou.pets.utils.EmailUtil;
import com.jiayou.pets.utils.JwtUtil;
import com.jiayou.pets.utils.RedisUtil;
import java.util.concurrent.TimeUnit;
@Service
public class ValidateServiceImpl implements ValidateService {
    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    @Autowired
    private DefaultKaptcha kaptcha;

    public ValidateServiceImpl(EmailUtil emailUtil, RedisUtil redisUtil, JwtUtil jwtUtil) {
        this.emailUtil = emailUtil;
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
    }

    private String generateNumberCode() {
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
    public ResponseEntity<HashMap<String, Object>> sendImgCode() {
        // 生成验证码
        String code = generateStringCode(5);
        // 创建验证码图片
        BufferedImage image = kaptcha.createImage(code);
        // 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 生成一个 UUID
        String tempId = UUID.randomUUID().toString();
        HashMap<String, Object> map = new HashMap<>();
        try {
            ImageIO.write(image, "png", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            String imgData = "data:image/png;base64," + base64Image;
            redisUtil.setWithExpire(tempId, code, 1, java.util.concurrent.TimeUnit.HOURS);
            map.put("id", tempId);
            map.put("img", imgData);
            return ResponseEntity.success(map);
        } catch (Exception e) {
            return ResponseEntity.error(400, e.getMessage());
        }
    }
    

    @Override
    public ResponseEntity<HashMap<String, Object>> validateImgCode(String id, String code) {
        HashMap<String, Object> map = new HashMap<>();
        String preCode = (String) redisUtil.get(id);
        boolean flag = preCode != null && preCode.toLowerCase().equals(code.toLowerCase());
        if (flag) {
            redisUtil.delete(id);
        } else {
            return ResponseEntity.error(400, "验证码错误");
        }
        return ResponseEntity.success(map);
    }

    @Override
    public ResponseEntity<HashMap<String, Object>> sendEmailCode(String email) {
        HashMap<String, Object> map = new HashMap<>();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.error(400, "邮箱不能为空");
        }
        String preCode = (String) redisUtil.get(email);
        if (preCode != null) {
            return ResponseEntity.error(400, "验证码仍处于有效期，请勿重复请求");
        }
        String code = generateNumberCode();
        redisUtil.setWithExpire(email, code, 1, java.util.concurrent.TimeUnit.MINUTES);
        // todo 发送验证码
        // boolean success = emailUtil.sendSimpleEmail(email, "验证码", String.format("您的验证码为：%s，有效期1分钟，请勿泄露。", code));
        boolean success = true;
        System.out.println("codeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee---|"+code);
        String token = jwtUtil.generateToken(new HashMap<String, Object>() {{
            put("email", email);
        }},1,TimeUnit.MINUTES);
        if (success) {
            map.put("token", token);
            return ResponseEntity.success(map);
        } else {
            return ResponseEntity.error(400, "验证码发送失败");
        }
    }
    @Override
    public ResponseEntity<HashMap<String, Object>> validateEmailCode(String email, String code) {
        HashMap<String, Object> map = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenEmail = authentication.getName();
        if(tokenEmail==null){
            return ResponseEntity.error(400, "验证码已过期");
        }
        if(!tokenEmail.equals(email)){
            return ResponseEntity.error(400, "邮箱不匹配,请重试");
        }
        String preCode = (String) redisUtil.get(tokenEmail);
        boolean flag = preCode != null && preCode.toLowerCase().equals(code.toLowerCase());
        if (flag) {
            redisUtil.delete(tokenEmail);
        } else {
            return ResponseEntity.error(400, "验证码错误");
        }
        String token = jwtUtil.generateToken(new HashMap<String, Object>() {{
            put("email", email);
        }},1,TimeUnit.MINUTES);
        map.put("token", token);
        return ResponseEntity.success(map);
    }

   

}
