package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("consultation")
public class Consultation {
    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Long doctorId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 