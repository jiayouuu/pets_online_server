package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("logistics")
public class Logistics {
    @TableId
    private Long id;
    private Long applicationId;
    private String trackingNumber;
    private String logisticsCompany;
    private String status;
    private String currentLocation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 