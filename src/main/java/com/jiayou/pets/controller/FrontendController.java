/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 21:19:12
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-22 09:59:10
 * @Description: 前端页面资源控制器
 */
package com.jiayou.pets.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;



@Controller
public class FrontendController {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // 将所有非API请求都重定向到 index.html
    @GetMapping("/app/**")
    public String forwardToFrontend() {
        return "forward:/index.html";
    }
    // 将pets 请求重定向到 pets/app
    @GetMapping("/")
    public RedirectView redirect() {
        return new RedirectView(String.format("%s/app/", contextPath));
    }
}
