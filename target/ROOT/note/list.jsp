<%--
  Created by IntelliJ IDEA.
  User: 飞翔的兰
  Date: 2021/6/8
  Time: 0:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon glyphicon-th-list"></span>&nbsp;
            日记列表
        </div>

        <%--  判读日记列表是否存在  --%>
        <c:if test="${empty page}">
            <h2>暂未查询到日记记录</h2>
        </c:if>

        <c:if test="${!empty page}">
            <%--  遍历获取日记列表  --%>
            <div class="note_datas">
                <ul>
                    <c:forEach items="${page.dataList}" var="item">
                        <li>『<fmt:formatDate value="${item.pubTime}" pattern="yyyy-MM-dd-h:mm"/>』
                            &nbsp;&nbsp;<a href="note?actionName=detail&noteId=${item.noteId}">${item.title}</a></li>
                    </c:forEach>
                </ul>
            </div>

            <%--  设置分页导航  --%>
            <nav style="text-align: center">
                <ul class="pagination   center">
                        <%--  如果当前不是第一页，则显示上一页的按钮  --%>
                    <c:if test="${page.pageNum > 1}">
                        <li>
                            <a href="index?actionName=${action}&title=${title}&date=${date}&typeId=${typeId}&pageNum=${page.prePage}">
                                <span>«</span>
                            </a>
                        </li>
                    </c:if>
                        <%--  导航页数  --%>
                    <c:forEach begin="${page.startNavPage}" end="${page.endNavPage}" var="p">
                        <li <c:if test="${page.pageNum == p}">class="active" </c:if>>
                            <a href="index?actionName=${action}&title=${title}&date=${date}&typeId=${typeId}&pageNum=${p}">${p}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${page.pageNum < page.totalPages}">
                        <li>
                            <a href="index?actionName=${action}&title=${title}&date=${date}&typeId=${typeId}&pageNum=${page.nextPage}">
                                <span>»</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </c:if>
    </div>
</div>

