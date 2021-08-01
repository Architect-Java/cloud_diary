package com.note.controller;

import cn.hutool.core.util.StrUtil;
import com.note.pojo.Note;
import com.note.pojo.NoteType;
import com.note.pojo.User;
import com.note.service.NoteService;
import com.note.service.NoteTypeService;
import com.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Dream_飞翔
 * @date 2021/7/19
 * @time 8:45
 * @email 1072876976@qq.com
 */
@WebServlet("/note")
public class NoteController extends HttpServlet {
    private NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置高亮显示
        request.setAttribute("menu_page", "note");

        // 得到用户行为
        String actionName = request.getParameter("actionName");

        // 判读用户行为
        if ("view".equals(actionName)) {
            // 进入发布日记页面
            noteView(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            // 添加或修改日记
            addOrUpdate(request, response);
        } else if ("detail".equals(actionName)) {
            // 查询日记详情
            noteDetail(request, response);
        } else if ("delete".equals(actionName)) {
            // 删除日记详情
            noteDelete(request, response);
        }
    }

    /**
     * 删除日记操作
     *
     * @param request
     * @param response
     */
    private void noteDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 接收参数（noteId）
        String noteId = request.getParameter("noteId");

        // 2. 调用Service层的删除方法，返回状态码（1为成功，0为失败）
        Integer code = noteService.deleteNote(noteId);

        // 3. 通过流将结果响应给Ajax的回调函数（输出字符串）
        response.getWriter().write(code + "");

        // 4. 关闭流
        response.getWriter().close();
    }

    /**
     * 查询日记详情
     *
     * @param request
     * @param response
     */
    private void noteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 接收参数（noteId）
        String noteId = request.getParameter("noteId");

        // 2. 调用Service层的查询方法，返回Note对象
        Note note = noteService.findNoteById(noteId);

        // 3. 将Note对象设置到request请求域中
        request.setAttribute("note", note);

        // 4. 设置首页动态包含的页面值
        request.setAttribute("changePage", "note/detail.jsp");

        // 5. 请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * 添加或修改日记
     *
     * @param request
     * @param response
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. 接收参数（类型ID、标题、内容）
        String typeId = request.getParameter("typeId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 如果是修改操作，需要接收noteId
        String noteId = request.getParameter("noteId");

        // 2. 调用Service层，返回resultInfo对象
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId, title, content, noteId);

        // 3. 判断resultInfo的code值
        if (resultInfo.getCode() == 1) { // 如果code=1，表示成功
            // 重定向跳转到首页 index
            response.sendRedirect("index");
        } else { // 如果code=0，表示失败
            // 将resultInfo对象设置到request作用域
            request.setAttribute("resultInfo", resultInfo);
            // 请求转发跳转到note?actionName=view
            String url = "note?actionName=view";
            // 如果是修改操作，需要传递noteId
            if (!StrUtil.isBlank(noteId)) {
                url += "&noteId=" + noteId;
            }
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    /**
     * 进入发布日记界面
     *
     * @param request
     * @param response
     */
    private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 修改操作
        // 得到要修改的日记ID
        String noteId = request.getParameter("noteId");
        // 通过noteId查询日记对象
        Note note = noteService.findNoteById(noteId);
        // 将note对象设置到请求域中
        request.setAttribute("noteInfo", note);

        // 1. 从Session对象中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        // 2. 通过用户ID查询对应的类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        // 3. 将类型列表设置到request请求域中
        request.setAttribute("typeList", typeList);
        // 4. 设置首页动态包含的页面值
        request.setAttribute("changePage", "note/view.jsp");
        // 5. 请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
