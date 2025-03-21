package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sensitive_word")
public class SensitiveWord {
    @TableId
    private Long id;
    private String word;
    private String category;
    private Integer level;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 