function isNotNull(val) {
    var str = val.replace(/(^\s*)|(\s*$)/g, ''); //去除空格;
    if (str == '' || str == undefined || str == null) {
        return false;
    } else {
        return true;
    }
}

// 标签切换
function Tab(id) {
    var i;
    for (i = 0; i <= 1; i++) {
        if (i == id) {
            document.getElementById("main-box-input-" + i).style.display = "block";
            document.getElementById("main-box-tab-" + i).style.borderColor = "rgb(0, 49, 79)";
        } else {
            document.getElementById("main-box-input-" + i).style.display = "none";
            document.getElementById("main-box-tab-" + i).style.borderColor = "white";
        }
    }
}

layui.use('form', function () {
    var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
    form.render();
});

$(function () {
    // 登录按钮
    $("#login-button").on("click", function () {
        var loginPrincipal = $("#login-input-principal").val(); // 用户名或邮箱
        var loginPassword = $("#login-input-password").val(); // 密码
        if (isNotNull(loginPrincipal) && isNotNull(loginPassword)) {
            var form = new FormData();
            form.append("principal", loginPrincipal);
            form.append("password", loginPassword);
            $.ajax({
                url: "/auth",
                type: "post",
                data: form,
                processData: false,
                contentType: false,
                success: function (response) {
                    if (response.code == 0) {
                        var user = response.data.user;
                        // 登录成功
                        // localStorage.setItem("userId", response.data.userInfo.userId);
                        // localStorage.setItem("username", response.data.userInfo
                        //     .username);
                        // localStorage.setItem("avatarUrl", response.data.userInfo
                        //     .avatarUrl);
                        // localStorage.setItem("simpleDesc", response.data.userInfo
                        //     .simpleDesc);
                        window.location.href = response.data.redirectUrl;
                    } else {
                        // 登陆失败
                        $("#login-error-message").text(response.message);
                    }
                },
                error: function (response) {
                    $("#login-error-message").text("服务器错误，请稍后重试");
                }
            });
        }
    });
});