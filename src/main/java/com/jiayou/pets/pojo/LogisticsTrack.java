package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("logistics_track")
public class LogisticsTrack {
    @TableId
    private Long id;
    private Long logisticsId;
    private String location;
    private String description;
    private LocalDateTime trackTime;
    private LocalDateTime createdAt;
} 