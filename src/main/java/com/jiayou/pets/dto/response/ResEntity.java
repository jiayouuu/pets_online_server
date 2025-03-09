package com.jiayou.pets.dto.response;

import lombok.Data;

@Data
public class ResEntity<T> {
    private int code;
    private String message;
    private T data;

    // 无参构造函数
    public ResEntity() {
    }

    // 带参数的构造函数
    public ResEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态方法用于快速创建成功响应
    public static <T> ResEntity<T> success(T data) {
        return new ResEntity<>(200, "success", data);
    }
    // 静态方法用于快速创建成功响应
    public static <T> ResEntity<T> success(T data, String message) {
        return new ResEntity<>(200, message, data);
    }

    // 静态方法用于快速创建失败响应
    public static <T> ResEntity<T> error(int code, String message) {
        return new ResEntity<>(code, message, null);
    }
}