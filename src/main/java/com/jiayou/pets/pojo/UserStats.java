/*
 * @Author: jiayouuu 3010336955@qq.com
 * @Date: 2025-03-21 23:58:40
 * @LastEditors: jiayouuu 3010336955@qq.com
 * @LastEditTime: 2025-03-21 23:58:54
 * @FilePath: /pets_online_server/src/main/java/com/jiayou/pets/pojo/UserStats.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_stats")
public class UserStats {
    @TableId
    private Long id;
    private Long userId;
    private Integer loginCount;
    private Integer postCount;
    private Integer commentCount;
    private Integer applicationCount;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 