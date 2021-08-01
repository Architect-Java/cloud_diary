package com.note.service;

import cn.hutool.core.util.StrUtil;
import com.note.dao.NoteDao;
import com.note.pojo.Note;
import com.note.util.PageUtil;
import com.note.vo.NoteVo;
import com.note.vo.ResultInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dream_飞翔
 * @date 2021/7/19
 * @time 8:44
 * @email 1072876976@qq.com
 */
public class NoteService {

    private NoteDao noteDao = new NoteDao();

    /**
     * 添加或修改日记
     *
     * @param typeId
     * @param title
     * @param content
     * @param noteId
     * @return
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();

        // 1. 参数的非空判断
        if (StrUtil.isBlank(typeId)) {
            // 如果为空，code=0，msg="xxx"，result=note对象，返回resultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择日记类型！");
            return resultInfo;
        } else if (StrUtil.isBlank(title)) {
            // 如果为空，code=0，msg="xxx"，result=note对象，返回resultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("请输入日记的标题！");
            return resultInfo;
        } else if (StrUtil.isBlank(content)) {
            // 如果为空，code=0，msg="xxx"，result=note对象，返回resultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("日记内容不能为空哦！");
            return resultInfo;
        }
        // 2. 设置回显对象 Note对象
        Note note = new Note();
        note.setTitle(title).setContent(content);
        note.setTypeId(Integer.parseInt(typeId));

        // 判断日记ID是否为空
        if (!StrUtil.isBlank(noteId)) {
            note.setNoteId(Integer.parseInt(noteId));
        }

        resultInfo.setResult(note);

        // 3. 调用Dao层，添加日记记录，返回受影响的行数
        int row = noteDao.addOrUpdate(note);

        // 4. 判断受影响的行数
        if (row > 0) {
            // 如果大于0，code=1
            resultInfo.setCode(1);
        } else {
            // 如果不大于0，code=0，msg="xxx"，result=note对象
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
            resultInfo.setResult(note);
        }

        // 5. 返回resultInfo对象
        return resultInfo;
    }

    /**
     * 分页查询日记列表
     *
     * @param pageNumStr  页面数
     * @param pageSizeStr 页面大小
     * @param userId      用户ID
     * @param title       标题
     * @param date        日期
     * @param typeId      类型ID
     * @return
     */
    public PageUtil<Note> findNoteListByPage(String pageNumStr, String pageSizeStr,
                                             Integer userId, String title, String date, String typeId) {
        // 设置分页参数的默认值
        Integer pageNum = 1; // 默认当前页是第一页
        Integer pageSize = 10; // 默认每页显示10条

        // 1. 判断参数的非空校验
        if (!StrUtil.isBlank(pageNumStr)) {
            // 设置当前页
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (!StrUtil.isBlank(pageSizeStr)) {
            // 设置每页显示的数量
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 如果分页参数为空，则设置默认值
        // 2. 查询当前登录用户的日记数量，返回总记录数（long类型）
        long count = noteDao.findNoteCount(userId, title, date, typeId);

        // 3. 判断总记录数是否大于0
        if (count < 1) {
            return null;
        }

        // 4. 如果总记录数大于0，调用Page类中的带参构造，得到其它的分页参数的值，返回Page对象
        PageUtil<Note> page = new PageUtil<>(pageNum, pageSize, count);

        // 得到数据库中分页查询的开始下标，开始索引为（当前页 - 1） * 每一页显示的数量
        Integer index = (pageNum - 1) * pageSize;

        // 5. 查询当前登录用户下当前页的数据列表，返回note集合
        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title, date, typeId);

        // 6. 将note集合设置到Page对象中
        page.setDataList(noteList);

        // 7. 返回Page对象
        return page;
    }

    /**
     * 通过日期分组查询当前登录用户下的日记数量
     *
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteByDate(userId);
    }

    /**
     * 通过类型分组查询当前登录用户下的日记数量
     *
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    /**
     * 查询日记详情
     *
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        // 1. 参数的非空判断
        if (StrUtil.isBlank(noteId)) {
            return null;
        }
        // 2. 调用Dao层的查询方法，通过noteId查询note对象
        Note note = noteDao.findNoteById(noteId);
        // 3. 返回note对象
        return note;
    }

    /**
     * 删除日记详情
     *
     * @param noteId
     * @return
     */
    public Integer deleteNote(String noteId) {
        // 1. 判断参数
        if (StrUtil.isBlank(noteId)) {
            return 0;
        }

        // 2. 调用Dao层的更新方法，返回受影响的行数
        int row = noteDao.deleteNoteById(noteId);

        // 3. 判断受影响的行数是否大于0
        if (row > 0) {
            // 如果大于0，返回1；否则返回0
            return 1;
        }
        return 0;
    }

    /**
     * 通过月份查询对应日记的数量
     *
     * @param userId
     * @return
     */
    public ResultInfo<Map<String, Object>> queryNoteCountByMonth(Integer userId) {
        ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();

        // 通过月份分类查询日记数量
        List<NoteVo> noteVos = noteDao.findNoteByDate(userId);

        // 判断集合是否存在
        if (noteVos != null && noteVos.size() > 0) {
            // 得到月份
            List<String> monthList = new ArrayList<>();
            // 得到日记集合
            List<Integer> noteCountList = new ArrayList<>();

            // 遍历月份分组集合
            for (NoteVo noteVo : noteVos) {
                monthList.add(noteVo.getGroupName());
                noteCountList.add((int) noteVo.getNoteCount());
            }

            // 准备Map对象封装月份与对应的日记数量
            Map<String, Object> map = new HashMap<>();
            map.put("monthArry", monthList);
            map.put("dataArry", noteCountList);

            // 将Map对象设置到ResultInfo对象中
            resultInfo.setCode(1);
            resultInfo.setResult(map);
        }

        return resultInfo;
    }

    /**
     * 查询用户发布日记时的坐标
     *
     * @param userId
     * @return
     */
    public ResultInfo<List<Note>> queryNoteLonAndLat(Integer userId) {
        ResultInfo<List<Note>> resultInfo = new ResultInfo<>();

        // 通过用户ID查询日记记录
        List<Note> noteList = noteDao.queryNoteList(userId);

        // 判断是否为空
        if (noteList != null && noteList.size() > 0) {
            resultInfo.setCode(1);
            resultInfo.setResult(noteList);
        }

        return resultInfo;
    }
}
