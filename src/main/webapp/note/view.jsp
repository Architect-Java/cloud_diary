<%--
  Created by IntelliJ IDEA.
  User: 飞翔的兰
  Date: 2021/7/19
  Time: 8:54
  To change this template use File | Settings | File Templates
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-md-9">

    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;
            <c:if test="${empty noteInfo}">
                发表日记
            </c:if>
            <c:if test="${!empty noteInfo}">
                修改日记
            </c:if>
        </div>
        <div class="container-fluid">
            <div class="container-fluid">
                <div class="row" style="padding-top: 20px;">
                    <div class="col-md-12">
                        <%--  判断类型列表是否为空，如果为空，提示用户先添加类型  --%>
                        <c:if test="${empty typeList}">
                            <h2>暂未查询到日记类型！</h2>
                            <h4><a href="type?actionName=list">添加类型</a></h4>
                        </c:if>
                        <c:if test="${!empty typeList}">
                            <form class="form-horizontal" method="post" action="note">
                                    <%--  设置隐藏域：用来存放用户行为actionName  --%>
                                <input type="hidden" name="actionName" value="addOrUpdate"/>
                                    <%--  设置隐藏域，存放noteId  --%>
                                <input type="hidden" name="noteId" value="${noteInfo.noteId}">
                                    <%--  设置隐藏域, 存放用户发布日记时所在地区的经纬度  --%>
                                <input type="hidden" name="lon" id="lon"/>
                                <input type="hidden" name="lat" id="lat"/>

                                <div class="form-group">
                                    <label for="typeId" class="col-sm-2 control-label">类别:</label>
                                    <div class="col-sm-8">
                                        <select id="typeId" class="form-control" name="typeId">
                                            <option value="">请选择日记类别...</option>
                                            <c:forEach var="item" items="${typeList}">
                                                <c:choose>
                                                    <c:when test="${!empty resultInfo}">
                                                        <option
                                                                <c:if test="${resultInfo.result.typeId == item.typeId}">selected</c:if>
                                                                value="${item.typeId}">${item.typeName}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option
                                                                <c:if test="${noteInfo.typeId == item.typeId}">selected</c:if>
                                                                value="${item.typeId}">${item.typeName}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="hidden" name="noteId" value="28">
                                    <input type="hidden" name="act" value="save">
                                    <label for="title" class="col-sm-2 control-label">标题:</label>
                                    <div class="col-sm-8">
                                        <c:choose>
                                            <c:when test="${!empty resultInfo}">
                                                <input class="form-control" name="title" id="title" placeholder="日记标题"
                                                       value="${resultInfo.result.title}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input class="form-control" name="title" id="title" placeholder="日记标题"
                                                       value="${noteInfo.title}"/>
                                            </c:otherwise>
                                        </c:choose>

                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="title" class="col-sm-2 control-label">内容:</label>
                                    <div class="col-sm-8">
                                        <c:choose>
                                            <c:when test="${!empty resultInfo}">
                                                <%--  准备容器，加载富文本编辑器  --%>
                                                <textarea id="content"
                                                          name="content">${resultInfo.result.content}</textarea>
                                            </c:when>
                                            <c:otherwise>
                                                <%--  准备容器，加载富文本编辑器  --%>
                                                <textarea id="content" name="content">${noteInfo.content}</textarea>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-5 col-sm-4">
                                        <input type="submit" class="btn btn-primary" onclick="return checkForm();"
                                               value="保存">
                                        &nbsp;<span id="msg" style="font-size: 12px;color: red">${resultInfo.msg}</span>
                                    </div>
                                </div>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        var ue;
        $(function () {
            // 加载富文本编辑器，UE.getEditor("容器ID");
            ue = UE.getEditor('content');
        });

        /**
         * 表单校验
         *
         * @returns {boolean}
         */
        function checkForm() {
            // 1. 获取表单元素的值
            // 获取下拉框中的选项   .val()
            var typeId = $("#typeId").val();
            // 获取文本框的值      .val()
            var title = $("#title").val();

            // 获取富文本编辑器的内容
            // ue.getContent() 获取富文本编辑器的内容（包含html标签）
            // ue.getContentTxt() 获取富文本编辑器的纯文本内容（不包含html标签）
            var content = ue.getContent();

            // 2. 参数的非空判断
            // 如果为空，提示用户，并return false
            if (isEmpty(typeId)) {
                $("#msg").html("请选择日记类型！");
                return false;
            }
            if (isEmpty(title)) {
                $("#msg").html("请输入日记的标题！");
                return false;
            }
            if (isEmpty(content)) {
                $("#msg").html("日记内容不能为空哦！");
                return false;
            }
            // 3. 如果参数不为空，则return true，提交表单
            return true;
        }
    </script>

</div>

<%--  引入百度地图API  --%>
<script type="text/javascript" src="https://api.map.baidu.com/api?v=1.0&&type=webgl&ak=3YjSmtWz0tjughPWQYhojW0vTncpY24U"></script>
<script type="text/javascript">
    // 百度地图获取当前所在位置的经纬度
    var geolocation = new BMapGL.Geolocation();

    geolocation.getCurrentPosition(function(r) {
        // 判断是否获取到
        if (this.getStatus() == BMAP_STATUS_SUCCESS) {
            alert("您的位置: " + r.point.lng + ", " + r.point.lat)
            console.log("您的位置: " + r.point.lng + ", " + r.point.lat)
        } else {
            console.log("filed: " + this.getStatus())
        }
    });
</script>