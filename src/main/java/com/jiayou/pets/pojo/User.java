/*
 * @Author: 桂佳囿
 * @Date: 2025-01-18 15:47:05
 * @LastEditors: 桂佳囿
 * @LastEditTime: 2025-01-22 17:45:02
 * @Description: 用户
 */
package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
// jpa 实体
@Entity
// mybatis-plus 实体
@TableName("user")
public class User {
    // jpa主键
    @Id
    // jpa 主键策略
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // mybatis-plus 主键策略
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;
    private String email;
    private String password;
    private String nickname;
}
