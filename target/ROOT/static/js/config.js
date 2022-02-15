/**
 * 用户登录表单校验
 * 1. 获取用户名称与密码
 * 2. 判断用户名或密码是否为空
 *      如果为空，提示用户
 * 3. 如果不为空，提交表单
 *
 * 表单元素需要设置name属性值
 */
function checkLogin() {
    // 1. 获取用户名称与密码，通过使用id选择器
    var userName = $("#userName").val(); // 用户名
    var userPwd = $("#password").val(); // 用户密码

    // 2. 判断用户名或密码是否为空
    if (isEmpty(userName)) { // 判断用户名是否为空
        // 如果为空，提示用户
        $("#msg").html("用户名称不能为空！");
        return;
    } else if (isEmpty(userPwd)) {
        $("#msg").html("用户密码不能为空！");
        return;
    }

    // 3. 如果不为空，提交表单
    $("#loginForm").submit();
}