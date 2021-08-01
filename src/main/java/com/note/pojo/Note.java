package com.note.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Dream_飞翔
 * @date 2021/7/19
 * @time 8:42
 * @email 1072876976@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Note {

    private Integer noteId; // 日记ID
    private String title; // 日记标题
    private String content; // 日记内容
    private Integer typeId; // 日记类型ID
    private Float lon; // 日记的经度
    private Float lat; // 日记的纬度
    private Date pubTime; // 日记发布时间

    private String typeName; //类型名称
}
