/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 18:07:47
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-18 20:38:28
 * @Description: 响应体
 */

package com.jiayou.pets.response;

import lombok.Data;

@Data
public class ResponseEntity<T> {
    private int code;
    private String message;
    private T data;

    // 无参构造函数
    public ResponseEntity() {
    }

    // 带参数的构造函数
    public ResponseEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 静态方法用于快速创建成功响应
    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(200, "Success", data);
    }

    // 静态方法用于快速创建失败响应
    public static <T> ResponseEntity<T> error(int code, String message) {
        return new ResponseEntity<>(code, message, null);
    }
}