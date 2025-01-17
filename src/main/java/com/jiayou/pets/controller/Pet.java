package com.jiayou.pets.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Pet {
    @GetMapping("/pet")
    public String getMethodName(@RequestParam(defaultValue = "dog") String name) {
        return String.format("Hello %s!", name);
    }

}
