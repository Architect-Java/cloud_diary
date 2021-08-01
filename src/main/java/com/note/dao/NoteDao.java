package com.note.dao;

import cn.hutool.core.util.StrUtil;
import com.note.pojo.Note;
import com.note.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dream_飞翔
 * @date 2021/7/19
 * @time 8:44
 * @email 1072876976@qq.com
 */
public class NoteDao {
    /**
     * 添加或修改日记，返回受影响的行数
     *
     * @param note
     * @return
     */
    public int addOrUpdate(Note note) {
        // 定义SQL语句
        String sql = "";
        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        // 判断noteId是否为空，如果为空则为添加操作，如果不为空则为修改操作
        if (note.getNoteId() == null) {
            // 添加操作
            sql = "insert into tb_note(typeId, title, content, pubTime) values(?,?,?,now())";
        } else {
            // 修改操作
            sql = "update tb_note set typeId = ?, title = ?,content = ? where noteId = ?";
            params.add(note.getNoteId());
        }

        // 调用BaseDao的更新方法
        int row = BaseDao.executeUpdate(sql, params);

        // 返回受影响的行数
        return row;
    }

    /**
     * 查询当前登录用户的日记数据，返回总记录数
     *
     * @param userId 用户ID
     * @param title  文章标题
     * @param date   文章日期
     * @param typeId 类别ID
     * @return
     */
    public long findNoteCount(Integer userId, String title, String date, String typeId) {
        // 定义SQL语句
        String sql = "select count(1) from tb_note n " +
                "inner join tb_note_type t " +
                "on n.typeId = t.typeId " +
                "where userId = ?";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 判断条件查询的参数是否为空，如果查询的参数不为空，则拼接SQL语句并设置需要的参数
        if (!StrUtil.isBlank(title)) { // 标题查询
            // 拼接条件查询的SQL语句
            sql += " and title like concat('%',?,'%')";
            // 设置SQL语句所需要的参数
            params.add(title);
        } else if (!StrUtil.isBlank(date)) { // 日期查询
            // 拼接条件查询的SQL语句
            sql += " and DATE_FORMAT(pubTime,'%Y年%m月') = ?";
            // 设置SQL语句所需要的参数
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) { // 类别查询
            // 拼接条件查询的SQL语句
            sql += " and n.typeId = ?";
            // 设置SQL语句所需要的参数
            params.add(typeId);
        }

        // 调用BaseDao的查询方法
        long count = (long) BaseDao.findSingleValue(sql, params);

        return count;
    }

    /**
     * 分页查询日记列表
     *
     * @param userId
     * @param index
     * @param pageSize
     * @param title
     * @param date
     * @param typeId
     * @return
     */
    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize,
                                         String title, String date, String typeId) {
        // 定义SQL语句，limit语句必须在最后面
        String sql = "select noteId,title,pubTime from tb_note n " +
                "inner join tb_note_type t " +
                "on n.typeId = t.typeId " +
                "where userId = ? ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 判断条件查询的参数是否为空，如果查询的参数不为空，则拼接SQL语句并设置需要的参数
        if (!StrUtil.isBlank(title)) {
            // 拼接条件查询的SQL语句
            sql += " and title like concat('%',?,'%')";
            // 设置SQL语句所需要的参数
            params.add(title);
        } else if (!StrUtil.isBlank(date)) { // 日期查询

            // 拼接条件查询的SQL语句
            sql += " and DATE_FORMAT(pubTime,'%Y年%m月') = ?";
            // 设置SQL语句所需要的参数
            params.add(date);

        } else if (!StrUtil.isBlank(typeId)) { // 类别查询

            // 拼接条件查询的SQL语句
            sql += " and n.typeId = ?";
            // 设置SQL语句所需要的参数
            params.add(typeId);

        }

        // 拼接分页的SQL语句（因为limit语句需要写在SQL的最后面）
        sql += " order by pubTime desc limit ? , ?";
        params.add(index);
        params.add(pageSize);

        // 调用BaseDao的查询方法
        List<Note> noteList = BaseDao.queryRows(sql, params, Note.class);

        return noteList;
    }

    /**
     * 通过日期分组查询当前登录用户下的日记数量
     *
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteByDate(Integer userId) {
        // 定义SQL语句
        String sql = "select count(1) noteCount,DATE_FORMAT(pubTime,'%Y年%m月') groupName from tb_note n " +
                "inner join tb_note_type t " +
                "on t.typeId = n.typeId " +
                "where userId = ? " +
                "group by groupName " +
                "order by groupName desc;";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 3. 调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    /**
     * 通过类型查询分组当前日记的数量
     *
     * @return 日记信息对象
     */
    public List<NoteVo> findNoteCountByType(Integer userId) {
        // 定义SQL语句
        String sql = "select t.typeId,typeName groupName,count(noteId) noteCount from tb_note n " +
                "right join tb_note_type t " +
                "on n.typeId = t.typeId " +
                "where userId = ? " +
                "group by t.typeId " +
                "order by noteCount desc;";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 3. 调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    /**
     * 通过ID查询日记对象
     *
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        // 定义SQL语句
        String sql = "select noteId,title,content,pubTime,typeName,n.typeId from tb_note n " +
                "inner join tb_note_type t " +
                "on n.typeId = t.typeId " +
                "where noteId = ?";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(noteId);

        // 调用BaseDao的查询方法
        Note note = (Note) BaseDao.queryRow(sql, params, Note.class);

        // 返回Note对象
        return note;
    }

    /**
     * 通过noteId删除日记记录，返回受影响的行数
     *
     * @param noteId
     * @return
     */
    public int deleteNoteById(String noteId) {
        // 定义SQL语句
        String sql = "delete from tb_note where noteId = ?";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(noteId);

        // 调用BaseDao，返回受影响的行数
        int row = BaseDao.executeUpdate(sql, params);

        return row;
    }

    /**
     * 通过用户ID查询日记列表
     *
     * @param userId
     * @return
     */
    public List<Note> queryNoteList(Integer userId) {
        // 定义SQL语句
        String sql = "select lon,lat from tb_note n " +
                "inner join tb_note_type t " +
                "on n.typeId = t.typeId " +
                "where userId = ?";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 调用BaseDao
        List<Note> noteList = BaseDao.queryRows(sql, params, Note.class);

        return noteList;
    }
}
