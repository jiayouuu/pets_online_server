package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String category;
    private Long petId;
    private String status;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 