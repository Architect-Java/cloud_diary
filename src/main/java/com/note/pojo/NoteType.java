package com.note.pojo;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Dream_飞翔
 * @date 2021/6/20
 * @time 16:31
 * @email 1072876976@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NoteType {

    private Integer typeId; // 类型ID
    private String typeName; // 类型名称
    private Integer userId; // 用户ID
}
