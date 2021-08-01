package com.note.pojo;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Dream_飞翔
 * @date 2021/6/6
 * @time 9:40
 * @email 1072876976@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    private Integer userId; // 用户ID
    private String userName; // 用户名称
    private String password; // 用户密码
    private String nick; // 用户昵称
    private String head; // 用户头像
    private String mood; // 用户心情
}
