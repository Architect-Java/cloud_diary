package com.note.controller;

import com.note.pojo.User;
import com.note.service.UserService;
import com.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Dream_飞翔
 * @date 2021/6/6
 * @time 10:09
 * @email 1072876976@qq.com
 */
@WebServlet("/user")
@MultipartConfig
public class UserController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 接收用户行为
        String actionName = request.getParameter("actionName");
        // 判断用户行为，调用对应的方法
        if ("login".equals(actionName)) {
            // 用户登录
            userLogin(request, response);
        } else if ("logout".equals(actionName)) {
            // 用户退出
            userLogout(request, response);
        } else if ("userCenter".equals(actionName)) {
            // 进入个人中心
            userCenter(request, response);
        } else if ("userHead".equals(actionName)) {
            // 加载头像
            userHead(request, response);
        } else if ("checkNick".equals(actionName)) {
            // 验证名称的唯一性
            checkNick(request, response);
        } else if ("updateUser".equals(actionName)) {
            // 修改用户信息
            updateUser(request, response);
        }
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @param response
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 调用Service层的方法，传递request对象作为参数，返回resultInfo对象
        ResultInfo<User> resultInfo = userService.updateUser(request, response);
        // 2. 将resultInfo对象存到request作用域中
        request.setAttribute("resultInfo", resultInfo);
        // 3. 请求转发跳转到个人中心页面（user?actionName=userCenter）
        request.getRequestDispatcher("user?actionName=userCenter").forward(request,response);
    }

    /**
     * 验证名称的唯一性
     *
     * @param request
     * @param response
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 获取参数（昵称）
        String nick = request.getParameter("nick");
        // 2. 从session作用域中获取用户对象得到用户ID
        User user = (User) request.getSession().getAttribute("user");
        // 3. 调用Service层中的方法，得到返回的结果
        Integer code = userService.checkNick(nick, user.getUserId());
        // 4. 通过字符输出流，将结果响应给前台的Ajax的回调函数
        response.getWriter().write(code + "");
        // 5. 关闭资源
        response.getWriter().close();
    }

    /**
     * 加载头像
     *
     * @param request
     * @param response
     */
    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 获取参数（图片名称）
        String head = request.getParameter("imageName");
        // 2. 得到图片的存放路径（得到项目的路径request.getServletContext().getRealPath("/")）
        String realPath = request.getServletContext().getRealPath("/WEB-INF/upload/");
        // 3. 通过图片的完整路径，得到file对象
        File file = new File(realPath + "/" + head);
        // 4. 通过截取，得到图片的后缀
        String pic = head.substring(head.lastIndexOf(".") + 1);
        // 5. 通过不同的图片后缀，设置不同的响应类型
        if ("PNG".equalsIgnoreCase(pic)) {
            response.setContentType("image/png");
        } else if ("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)) {
            response.setContentType("image/jpeg");
        } else if ("GIF".equalsIgnoreCase(pic)) {
            response.setContentType("image/gif");
        }
        // 6. 利用fileUtils的copyFile()方法，将图片拷贝给浏览器
        FileUtils.copyFile(file, response.getOutputStream());
    }

    /**
     * 进入个人中心
     *
     * @param request
     * @param response
     */
    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // 设置导航栏高亮
        request.setAttribute("menu_page","user");

        // 1. 设置首页动态包含的页面值
        request.setAttribute("changePage", "user/info.jsp");
        // 2. 请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * 用户退出
     *
     * @param request
     * @param response
     */
    private void userLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 销毁Session对象
        request.getSession().invalidate();
        // 2. 删除Cookie对象，没有提供删除Cookie对象的方法，因此设置Cookie的过期时间为0即可
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0); // 设置为0，表示删除cookie
        response.addCookie(cookie);
        // 3. 重定向跳转到登录页面
        response.sendRedirect("login.jsp");
    }

    /**
     * 用户登录
     *
     * @param request  请求信息
     * @param response 响应信息
     */
    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取参数：姓名、密码
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        // 2. 调用Service层方法，返回ResultInfo对象
        ResultInfo<User> resultInfo = userService.userLogin(userName, password);

        // 3. 判断是否登录成功，通过code来进行判断
        if (resultInfo.getCode() == 1) { // 如果成功
            // 将用户信息存储到session作用域中
            request.getSession().setAttribute("user", resultInfo.getResult());
            // 判断用户是否选择记住密码(rem的值为1)
            String rem = request.getParameter("rem");
            // 如果是，表示记住密码
            if ("1".equals(rem)) {
                // 将用户的姓名与密码存到cookie中，设置失效时间并响应给客户端
                // 1. 得到cookie对象
                Cookie cookie = new Cookie("user", userName + "-" + password);
                // 2. 设置失效时间
                cookie.setMaxAge(3 * 24 * 60 * 60);
                // 3. 响应给客户端
                response.addCookie(cookie);
            } else {
                // 如果否，清空原有的cookie对象
                Cookie cookie = new Cookie("user", null);
                // 删除cookie，设置maxage为0
                cookie.setMaxAge(0);
                // 响应给客户端
                response.addCookie(cookie);
            }
            // 重定向跳转到index控制器
            response.sendRedirect("index");
        } else { // 如果失败
            // 将ResultInfo对象设置到Request作用域中
            request.setAttribute("resultInfo", resultInfo);
            // 请求转发跳转到登录页面
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
