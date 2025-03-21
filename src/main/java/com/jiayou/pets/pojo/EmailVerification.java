package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("email_verification")
public class EmailVerification {
    @TableId
    private Long id;
    private Long userId;
    private String token;
    private String type;
    private Integer isUsed;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
} 