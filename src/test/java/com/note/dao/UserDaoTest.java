package com.note.dao;

import com.note.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dream_飞翔
 * @date 2021/6/6
 * @time 10:37
 * @email 1072876976@qq.com
 */
public class UserDaoTest {

    @Test
    public void queryUserByName01() {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getPassword());
    }

    @Test
    public void testAdd(){
        String sql = "insert into tb_user (username,password,nick,head,mood) values(?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add("misszy");
        params.add("e10adc3949ba59abbe56e057f20f883e");
        params.add("zhuoya's fool");
        params.add("404.jpg");
        params.add("Hello,World!");
        int row = BaseDao.executeUpdate(sql, params);
        System.out.println("受到影响的行数：" + row);
    }
}