package com.jiayou.pets.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "5");
        // 字符范围
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        // 图片宽度和高度
        properties.setProperty("kaptcha.image.width", "120");
        properties.setProperty("kaptcha.image.height", "40");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 背景颜色
        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");
        // 添加噪点
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        // 添加边框
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "gray");

        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}