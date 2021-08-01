package com.note.filter;

import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 过滤器处理中文乱码的问题，处理请求乱码
 * 乱码原因：
 * 服务器默认的解析编码为ISO-8859-1，不支持中文
 * 乱码情况：
 * Post请求：
 * Tomcat7及以下版本    乱码
 * Tomcat8以上版本      乱码
 * Get请求：
 * Tomcat7及以下版本     乱码
 * Tomcat8以上版本      不乱码
 * <p>
 * 解决方案：
 * Post请求：
 * 无论什么版本的服务器都会出现乱码，需要通过request.setCharacterEncoding("UTF-8")设置编码格式（只针对Post有效）
 * Get请求：
 * Tomcat8及以上版本，不会乱码，不需要处理
 * Tomcat7及以下版本，需要单独处理 -> new String(request.getParameter("xxx").getBytes("ISO-8859-1","UTF-8"))
 */
@WebFilter("/*") // 过滤所有资源
public class EncodingFilter implements Filter {

    // 如果不重写初始化和销毁方法则无法启动
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 基于HTTP
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 处理Post请求（只针对Post请求有效，Get请求不受影响）
        request.setCharacterEncoding("UTF-8");

        // 得到请求类型（GET | POST）
        String method = request.getMethod();

        // 如果是GET请求，判断服务器的版本
        if ("GET".equalsIgnoreCase(method)) { // 忽略大小写比较
            // 得到服务器的版本
            String serverInfo = request.getServletContext().getServerInfo();
            // 通过截取字符串得到具体的版本号，Apache Tomcat /8.5.57
            String version = serverInfo.substring(serverInfo.lastIndexOf("/") + 1, serverInfo.indexOf("."));
            // 判断服务器版本是否是Tomcat7及以下
            if (version != null && Integer.parseInt(version) < 8) {
                // Tomcat7及以下版本服务器的GET请求
                MyWapper myRequest = new MyWapper(request);
                // 放行资源
                filterChain.doFilter(myRequest,response);
                return;
            }
        }
        // 如果不是Tomcat7及以下版本服务器的GET请求就走这个处理方法
        filterChain.doFilter(request, response);
    }

    /**
     * 1. 定义内部类（类的本质是request对象）
     * 2. 继承HttpServletRequestWrapper包装类
     * 3. 重写getParameter()方法
     */
    class MyWapper extends HttpServletRequestWrapper {
        // 定义成员变量，HttpServletRequest对象（提升构造器中request的作用域）
        private HttpServletRequest request;

        /**
         * 带参构造
         * 可以得到需要处理的request对象
         *
         * @param request 需要处理的request对象
         */
        public MyWapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        /**
         * 重写getParameter()方法，处理乱码问题
         *
         * @param name
         * @return
         */
        @Override
        public String getParameter(String name) {
            // 获取参数（乱码的参数值）
            String value = request.getParameter(name);
            // 判断参数值是否为空
            if (StrUtil.isBlank(value)) {
                return value;
            }
            // 通过new String()处理乱码
            try {
                value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }
    }
}
