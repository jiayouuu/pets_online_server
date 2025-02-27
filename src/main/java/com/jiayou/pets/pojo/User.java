package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
// jpa 实体 用于自动生成表
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
    private String avatar;
    private String phone;
    private String address;
    private String gender;
    private String birthDate;
    private String role;
}
