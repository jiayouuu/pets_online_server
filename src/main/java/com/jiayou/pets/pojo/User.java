/*
 * @Author: jiayouuu 3010336955@qq.com
 * @Date: 2025-03-13 23:48:38
 * @LastEditors: jiayouuu 3010336955@qq.com
 * @LastEditTime: 2025-03-21 23:58:43
 * @FilePath: /pets_online_server/src/main/java/com/jiayou/pets/pojo/User.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
// jpa 实体 用于自动生成表
// @Entity
// mybatis-plus 实体
@TableName("user")
public class User {
    // jpa主键
    // @Id
    // jpa 主键策略
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // mybatis-plus 主键策略
    @TableId
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String avatar;
    private String role;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
