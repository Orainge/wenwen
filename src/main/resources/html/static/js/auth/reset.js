$(document.head).append("<script type='text/javascript' src='/static/js/util/md5.js'></script>")
var token = null
var timer = null

function tokenIsValid(code, message, email, token) {
    $("#main-box-message-button").on("click", function () {
        window.location.href = "/"
    })
    // code = 0
    if (code == 0) {
        // 链接有效
        this.token = token;
        // var email = data.email;
        $("#main-box-reset-button").on("click", resetPassword)
        $("#main-box-loading").css("display", "none")
        $("#main-box-reset").css("display", "block")
        $("#main-box").css("display", "block")
        $("#main-box-reset-email").text(email)
    } else {
        // 链接无效
        alertBoxMessage("重置失败", message)
        $("#main-box-loading").css("display", "none")
        $("#main-box-message").css("display", "block")
        $("#main-box").css("display", "block")
    }
}

/* 判断字符串是否为空 */
function isNull(val) {
    var str = val.replace(/(^\s*)|(\s*$)/g, ''); //去除空格;
    return (str == '' || str == undefined || str == null)
}

function loading() {
    $("#main-box-reset-loading").css("display", "block")
    $("#main-box-reset-button").css("display", "none")
}

function unload() {
    $("#main-box-reset-loading").css("display", "none")
    $("#main-box-reset-button").css("display", "block")
}

function alertResetErrorMessage(message) {
    $("#main-box-reset-tips").text(message)
    if (timer != null) {
        clearInterval(timer)
    }
    timer = setTimeout(function () {
        $("#main-box-reset-tips").text("")
        timer = null
    }, 3000)
}

function alertBoxMessage(title, message) {
    $("#main-box-message-title").text(title)
    $("#main-box-message-message").text(message)
}

/* 重置密码请求 */
function resetPassword() {
    /*检查输入的密码是否相等 */
    var newPassword = $("#main-box-reset-input-password").val()

    if (newPassword.length < 6) {
        alertResetErrorMessage("密码长度不能少于6位")
        return
    }

    if (isNull(newPassword)) {
        alertResetErrorMessage("请输入密码")
        return
    }

    var rePassword = $("#main-box-reset-input-re_password").val()
    if (isNull(rePassword)) {
        alertResetErrorMessage("请确认输入的密码")
        return
    }

    if (newPassword != rePassword) {
        // 两次输入的密码不一致
        alertResetErrorMessage("两次输入的密码不一致")
        return
    }

    loading()
    $.ajax({
        url: "/auth/resetPassword",
        type: "post",
        data: JSON.stringify({
            token: token,
            password: md5(newPassword)
        }),
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                alertBoxMessage("重置成功", response.message)
                $("#main-box-reset").css("display", "none")
                $("#main-box-message").css("display", "block")
            } else {
                unload()
                alertResetErrorMessage(response.message)
            }
        },
        error: function () {
            unload()
            alertResetErrorMessage("服务器错误，请稍后重试")
        }
    })
}