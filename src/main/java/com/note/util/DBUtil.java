package com.note.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Dream_飞翔
 * @date 2021/6/5
 * @time 15:26
 * @email 1072876976@qq.com
 */
public class DBUtil {
    // 接收db.properties文件中的键值对，得到配置对象
    private static Properties properties = new Properties();

    static {
        try {
            // 加载配置文件（输入流）
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            // 通过load()方法将输入流的内容加载到配置文件对象中
            properties.load(in);
            // 通过配置文件对象的getProperty()方法获取驱动名并加载驱动
            Class.forName(properties.getProperty("jdbcName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 得到数据库连接的相关信息
            String dbUrl = properties.getProperty("dbUrl");
            String userName = properties.getProperty("userName");
            String Password = properties.getProperty("Password");
            // 得到数据库连接
            connection = DriverManager.getConnection(dbUrl, userName, Password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭数据库连接资源
     *
     * @param resultSet         数据库结果集对象
     * @param preparedStatement 数据库预编译对象
     * @param connection        数据库连接对象
     */
    public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {

        try {
            // 如果资源对象不为空则关闭
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
