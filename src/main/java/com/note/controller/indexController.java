package com.note.controller;

import com.note.pojo.Note;
import com.note.pojo.User;
import com.note.service.NoteService;
import com.note.util.PageUtil;
import com.note.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Dream_飞翔
 * @date 2021/6/9
 * @time 23:56
 * @email 1072876976@qq.com
 */
@WebServlet("/index")
public class indexController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置导航栏高亮显示
        request.setAttribute("menu_page", "index");

        // 得到用户行为（判读是什么条件查询：标题查询、日期查询、类型查询）
        String actionName = request.getParameter("actionName");

        // 将用户行为设置到request作用域中（分页导航中需要获取）
        request.setAttribute("action", actionName);

        // 判断用户行为
        if ("searchTitle".equals(actionName)) {
            // 得到查询条件：标题
            String title = request.getParameter("title");

            // 将查询条件设置到request请求域中（查询条件的回显）
            request.setAttribute("title", title);

            // 标题搜索
            noteList(request, response, title, null, null);
        } else if ("searchDate".equals(actionName)) { // 日期查询
            // 得到查询条件：日期
            String date = request.getParameter("date");

            noteList(request, response, null, date, null);

            // 将查询条件设置到request请求域中（查询条件的回显）
            request.setAttribute("date", date);
            // 日期搜索
        } else if ("searchType".equals(actionName)) { // 类别查询
            // 得到查询条件：类别
            String typeId = request.getParameter("typeId");

            noteList(request, response, null, null, typeId);

            // 将查询条件设置到request请求域中（查询条件的回显）
            request.setAttribute("typeId", typeId);
            // 日期搜索
        } else {
            // 不做条件查询，查询所有
            noteList(request, response, null, null, null);
        }


        // 设置首页动态包含的页面
        request.setAttribute("changePage", "note/list.jsp");
        // 请求转发到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * 分页查询日记列表
     *
     * @param request
     * @param response
     * @param title    标题
     */
    private void noteList(HttpServletRequest request, HttpServletResponse response, String title, String date, String typeId) {
        // 1. 接收参数（当前页，每页显示的数量）
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        // 2. 获取Session作用域中的user对象
        User user = (User) request.getSession().getAttribute("user");

        // 3. 调用Service层中的查询方法，返回Page对象
        PageUtil<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title, date, typeId);

        // 4. 将Page对象设置到request作用域中
        request.setAttribute("page", page);

        // 通过日期分组查询当前登录用户下的日记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());

        // 设置集合存放在request作用域中
        request.getSession().setAttribute("dateInfo", dateInfo);

        // 通过类型分组查询当前登录用户下的日记数量
        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());

        // 设置集合存放在request作用域中
        request.getSession().setAttribute("typeInfo", typeInfo);
    }
}
