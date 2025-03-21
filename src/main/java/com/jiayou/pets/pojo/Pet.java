/*
 * @Author: jiayouuu 3010336955@qq.com
 * @Date: 2025-03-21 23:55:38
 * @LastEditors: jiayouuu 3010336955@qq.com
 * @LastEditTime: 2025-03-21 23:55:53
 * @FilePath: /pets_online_server/src/main/java/com/jiayou/pets/pojo/Pet.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.jiayou.pets.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("pet")
public class Pet {
    @TableId
    private Long id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private BigDecimal weight;
    private String color;
    private String location;
    private String healthStatus;
    private String characterDescription;
    private Integer isSterilized;
    private Integer isVaccinated;
    private String status;
    private Long createdBy;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 