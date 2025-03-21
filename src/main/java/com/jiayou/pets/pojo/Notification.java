package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {
    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String type;
    private Long targetId;
    private Integer isRead;
    private LocalDateTime createdAt;
} 