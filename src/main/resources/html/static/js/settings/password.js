$(document.head).append("<script type='text/javascript' src='/static/js/util/md5.js'></script>")

// 初始化弹窗
layui.use('form', function () {
    layui.form.render()
})

/* 判断字符串是否为空 */
function isNull(val) {
    var str = val.replace(/(^\s*)|(\s*$)/g, ''); //去除空格;
    return (str == '' || str == undefined || str == null)
}

$("#confirm-button").on("click", function () {
    var formData = layui.form.val('modify-password-form')
    var oldPassword = formData["password-old"]
    var newPassword = formData["password-new"]
    var newPasswordConfirm = formData["password-new-confirm"]

    if (isNull(oldPassword)) {
        setTipsMessage("请输入旧密码", "fail", 3)
        return
    }

    if (oldPassword.length < 6) {
        setTipsMessage("旧密码长度不能少于6位", "fail", 3)
        return
    }

    if (isNull(newPassword)) {
        setTipsMessage("请输入新密码", "fail", 3)
        return
    }

    if (isNull(newPasswordConfirm)) {
        setTipsMessage("请确认新密码", "fail", 3)
        return
    }

    if (newPassword != newPasswordConfirm) {
        setTipsMessage("两次输入的密码不一致", "fail", 3)
        return
    }

    if (newPassword.length < 6) {
        setTipsMessage("新密码长度不能少于6位", "fail", 3)
        return
    }

    tips("start")
    $.ajax({
        url: "/settings/password",
        type: "post",
        data: JSON.stringify({
            oldPassword: md5(oldPassword),
            newPassword: md5(newPassword)
        }),
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                tips("success",response.message)
                setTimeout(function () {
                    window.location.href = "/settings/password"
                }, 2000)
            } else {
                tips("fail", response.message)
            }
        },
        error: function () {
            tips("fail", "连接错误，请稍后再试")
        }
    })
})

var $tips = $("#modify-password-tips-text")
var $loading = $("#modify-password-loading")
var myTips

function tips(type, message) {
    if (type == "start") {
        $tips.text("")
        $loading.css("display", "block")
    } else if (type == "success") {
        $loading.css("display", "none")
        setTipsMessage(message, "success")
    } else if (type == "fail") {
        $loading.css("display", "none")
        setTipsMessage(message, "fail", 2)
    }
}

function setTipsMessage(message, type, delayTime) {
    if (myTips != null) {
        clearInterval(myTips)
    }
    if (type == null || type == undefined) {
        $tips.css("color", "red")
    } else if (type == "fail") {
        $tips.css("color", "red")
    } else if (type == "success") {
        $tips.css("color", "green")
    }
    $tips.text(message)
    if (delayTime != null && delayTime != undefined) {
        myTips = setTimeout(function () {
            $tips.text("")
            myTips = null
        }, delayTime * 1000)
    }
}