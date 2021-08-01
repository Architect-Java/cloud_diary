<%--
  Created by IntelliJ IDEA.
  User: 飞翔的兰
  Date: 2021/6/5
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <title>世纪末的架构师</title>
    <link href="static/css/default.css" rel="stylesheet" type="text/css"/>
    <!--必要样式-->
    <link href="static/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="static/css/demo.css" rel="stylesheet" type="text/css"/>
    <link href="static/css/loaders.css" rel="stylesheet" type="text/css"/>
    <script src="static/js/util.js" type="text/javascript"></script>
    <script src="static/js/config.js" type=text/javascript></script>
    <link href="static/layui/css/layui.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="static/js/jquery.min.js"></script>
    <script type="text/javascript" src="static/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src='static/js/stopExecutionOnTimeout.js?t=1'></script>
    <script type="text/javascript" src="static/layui/layui.js"></script>
    <script type="text/javascript" src="static/js/Particleground.js"></script>
    <script type="text/javascript" src="static/js/Treatment.js"></script>
    <script type="text/javascript" src="static/js/jquery.mockjax.js"></script>
    <script type="text/javascript" src="static/images/ThreeWebGL.js"></script>
    <script type="text/javascript" src="static/images/ThreeExtras.js"></script>
    <script type="text/javascript" src="static/images/Detector.js"></script>
    <script type="text/javascript" src="static/images/RequestAnimationFrame.js"></script>
</head>

<body>
<div class='login'>
    <div class='login_title'>
        <span>云日记登录</span>
    </div>
    <div class='login_fields'>
        <form action="user" method="post" id="loginForm">
            <div class='login_fields__user'>
                <div class='icon'>
                    <img alt="" src='static/images/user_icon_copy.png'>
                </div>
                <input type="hidden" name="actionName" value="login"/>
                <input type='text' id="userName" name="userName" placeholder='用户名' autocomplete="off" value="${resultInfo.result.userName}"/>
            </div>
            <div class='login_fields__password'>
                <div class='icon'>
                    <img alt="" src='static/images/lock_icon_copy.png'>
                </div>
                <input type="password" name="password" id="password" placeholder='密码' type='text' autocomplete="off" value="${resultInfo.result.password}">
                <div class='validation'>
                    <img alt="" src='static/images/tick.png'>
                </div>
            </div>

            <div class='login_fields__password'>
                <div class='icon'>
                    <input name="rem" type="checkbox" value="1" class="inputcheckbox"/> <label>记住我</label>
                    <span id="msg" style="color: whitesmoke;font-size: 14px; padding-left: 30px">${resultInfo.msg}</span>
                </div>
            </div>
            <div class='login_fields__submit'>
                <input type='button' value='登录' onclick="checkLogin()"/>
            </div>
        </form>
    </div>
    <div class='disclaimer'>
        <a href="https://blog.csdn.net/qq_48455576" />
        <p>世纪末的架构师</p>
    </div>
</div>

<script id="vs" type="x-shader/x-vertex">
			varying vec2 vUv; void main() { vUv = uv; gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 ); }


</script>
<script id="fs" type="x-shader/x-fragment">
			uniform sampler2D map; uniform vec3 fogColor; uniform float fogNear; uniform float fogFar; varying vec2 vUv; void main() { float depth = gl_FragCoord.z / gl_FragCoord.w; float fogFactor = smoothstep( fogNear, fogFar, depth ); gl_FragColor = texture2D( map, vUv ); gl_FragColor.w *= pow( gl_FragCoord.z, 20.0 ); gl_FragColor = mix( gl_FragColor, vec4( fogColor, gl_FragColor.w ), fogFactor ); }


</script>
<script type="text/javascript" src="static/js/stage.js"></script>
</body>
</html>
