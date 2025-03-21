package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post_image")
public class PostImage {
    @TableId
    private Long id;
    private Long postId;
    private String url;
    private Integer sort;
    private LocalDateTime createdAt;
} 