package com.note.dao;

import com.note.util.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dream_飞翔
 * @date 2021/6/6
 * @time 16:49
 * @email 1072876976@qq.com
 * <p>
 * 基础的JDBC操作类
 * 更新操作（添加、修改、删除）
 * 查询操作
 * 1. 查询一个字段 （只会返回一条记录并且只有一个字段）
 * 2. 查询集合
 * 3. 查询某个对象
 */
public class BaseDao {
    /**
     * 更新操作
     * 添加、修改、删除
     * 1. 得到数据库连接
     * 2. 定义sql语句（添加语句、修改语句、删除语句）
     * 3. 预编译
     * 4. 如果有参数则设置参数，下标从1开始
     * 5. 执行更新，返回受到影响的行数
     * 6. 关闭资源
     *
     * @param sql    sql语句
     * @param params 所需参数的集合
     * @return 受到影响的行数
     */
    public static int executeUpdate(String sql, List<Object> params) {
        int row = 0; // 受到影响的行数
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 得到数据库连接
            connection = DBUtil.getConnection();
            // 2. 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 3. 如果有参数则设置参数，下标从1开始
            if (params != null && params.size() > 0) {
                // 循环设置参数，设置参数类型为Object
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i + 1, params.get(i));
                }
            }
            // 执行更新，返回受影响的行数
            row = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, preparedStatement, connection);
        }
        return row;
    }

    /**
     * 查询一个字段
     * <p>
     * 1. 得到数据库连接
     * 2. 定义sql语句（添加语句、修改语句、删除语句）
     * 3. 预编译
     * 4. 如果有参数则设置参数，下标从1开始
     * 5. 执行查询，返回结果集
     * 6. 判断并分析结果集
     * 7. 关闭资源
     *
     * @param sql    SQL语句
     * @param params 所需要的参数集合
     * @return 返回一条记录并且只有一个字段
     */
    public static Object findSingleValue(String sql, List<Object> params) {
        Object object = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 获取数据库连接
            connection = DBUtil.getConnection();
            // 2. 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 3. 如果有参数则设置参数，下标从1开始
            if (params != null && params.size() > 0) {
                // 循环设置参数，设置参数类型为Object
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i + 1, params.get(i));
                }
            }
            // 5. 执行查询，返回结果集
            resultSet = preparedStatement.executeQuery();
            // 6. 判断并分析结果集
            if (resultSet.next()) {
                object = resultSet.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            DBUtil.close(resultSet, preparedStatement, connection);
        }

        return object;
    }

    /**
     * 查询集合
     * 1. 得到数据库连接
     * 2. 定义sql语句（添加语句、修改语句、删除语句）
     * 3. 预编译
     * 4. 如果有参数则设置参数，下标从1开始
     * 5. 执行查询，返回结果集
     * 6. 得到结果集中的元数据对象（查询到的字段数量以及查询了哪些字段）
     * 7. 判断并分析结果集
     * 8. 实例化对象
     * 9. 遍历查询到的字段数量，得到数据库中查询到的每一个列名
     * 10. 通过反射，通过列名得到对应的field对象
     * 11. 拼接set方法，得到字符串
     * 12. 通过反射，将set方法的字符串反射成类中指定的set方法
     * 13. 通过invoke调用set方法
     * 14. 将对应的JavaBean设置到集合中
     * 15. 关闭资源
     *
     * @param sql    SQL语句
     * @param params 所需要的参数集合
     * @param cls    类对象
     * @return JavaBean集合
     */
    public static List queryRows(String sql, List<Object> params, Class cls) {
        List list = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 获取数据库连接
            connection = DBUtil.getConnection();
            // 2. 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 3. 如果有参数则设置参数，下标从1开始
            if (params != null && params.size() > 0) {
                // 循环设置参数，设置参数类型为Object
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i + 1, params.get(i));
                }
            }
            // 5. 执行查询，返回结果集
            resultSet = preparedStatement.executeQuery();
            // 6. 得到结果集的元数据对象（查询到的字段数量以及查询了哪些字段）
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 得到查询的字段数量（列的数量）
            int fieldNum = resultSetMetaData.getColumnCount();
            // 7. 判断并分析结果集的数量
            while (resultSet.next()) {
                // 实例化对象
                Object object = cls.newInstance();
                // 遍历字段数量，得到数据库中查询的每一个列名
                for (int i = 1; i <= fieldNum; i++) {
                    // 得到每一个列名
                    // getColumnLabel()：获取列名或别名
                    // getColumnName()：获取列名
                    String columnName = resultSetMetaData.getColumnLabel(i);
                    // 通过反射，使用列名得到对应的field对象
                    Field field = cls.getDeclaredField(columnName);
                    // 拼接set方法，得到设置字段属性值方法名的字符串：setName()
                    // 拼接完set字符串以后，将数据库中获取的字段名称的首字母截取并转为大写
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    // 通过反射，将set方法字符串反射成类中对应的set方法
                    // 参数1：方法名称        参数2：方法类型
                    Method method = cls.getDeclaredMethod(setMethod, field.getType());
                    // 得到查询的每一个字段对应的值
                    Object value = resultSet.getObject(columnName);
                    // 通过invoke()方法调用set方法
                    method.invoke(object, value);
                }
                // 将JavaBean设置到集合中
                list.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, preparedStatement, connection);
        }
        return list;
    }

    /**
     * 查询对象
     *
     * @param sql
     * @param params
     * @param cls
     * @return
     */
    public static Object queryRow(String sql, List<Object> params, Class cls) {
        List list = queryRows(sql, params, cls);
        Object object = null;
        // 如果集合不为空，则获取查询的第一条数据
        if (list != null && list.size() > 0) {
            object = list.get(0);
        }
        return object;
    }
}
