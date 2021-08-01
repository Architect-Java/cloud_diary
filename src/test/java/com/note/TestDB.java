package com.note;

import com.note.util.DBUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dream_飞翔
 * @date 2021/6/5
 * @time 15:42
 * @email 1072876976@qq.com
 */
public class TestDB {

    // 使用日志工厂类，记录日志
    private Logger logger = LoggerFactory.getLogger(TestDB.class);

    /**
     * 单元测试方法
     * 1. 方法的返回值，建议使用void
     * 2. 参数列表，建议空参
     * 3. 每一个方法都能单独运行
     */
    @Test
    public void testDB() {
        System.out.println(DBUtil.getConnection());
        // 使用日志
        logger.info("获取数据库连接：" + DBUtil.getConnection());
        logger.debug("获取数据库{}的连接：", DBUtil.getConnection());
    }
}
