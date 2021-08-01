package com.note.vo;

/**
 * @author Dream_飞翔
 * @date 2021/7/25
 * @time 16:36
 * @email 1072876976@qq.com
 */

import lombok.Data;

/**
 * 用来接收分组信息
 *
 */
@Data
public class NoteVo {
    private String groupName; // 分组名称（发布时间）
    private long noteCount; // 日记数量

    private Integer typeId; // 类型ID
}
