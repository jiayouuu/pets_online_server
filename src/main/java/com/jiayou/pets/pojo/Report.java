package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("report")
public class Report {
    @TableId
    private Long id;
    private Long userId;
    private Long targetId;
    private String targetType;
    private String reason;
    private String description;
    private String status;
    private Long adminId;
    private LocalDateTime handleTime;
    private String handleResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 