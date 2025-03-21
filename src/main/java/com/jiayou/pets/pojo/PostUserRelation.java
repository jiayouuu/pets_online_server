package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post_user_relation")
public class PostUserRelation {
    @TableId
    private Long id;
    private Long postId;
    private Long userId;
    private String type;
    private LocalDateTime createdAt;
} 