package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("adoption_application")
public class AdoptionApplication {
    @TableId
    private Long id;
    private Long petId;
    private Long userId;
    private String environment;
    private String experience;
    private String signature;
    private String status;
    private Long adminId;
    private String adminComment;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 