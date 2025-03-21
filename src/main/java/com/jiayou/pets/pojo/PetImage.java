/*
 * @Author: jiayouuu 3010336955@qq.com
 * @Date: 2025-03-21 23:55:49
 * @LastEditors: jiayouuu 3010336955@qq.com
 * @LastEditTime: 2025-03-21 23:58:29
 * @FilePath: /pets_online_server/src/main/java/com/jiayou/pets/pojo/PetImage.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("pet_image")
public class PetImage {
    @TableId
    private Long id;
    private Long petId;
    private String url;
    private Integer isCover;
    private Integer sort;
    private LocalDateTime createdAt;
} 