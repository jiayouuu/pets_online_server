package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("consultation_image")
public class ConsultationImage {
    @TableId
    private Long id;
    private Long consultationId;
    private String url;
    private LocalDateTime createdAt;
} 