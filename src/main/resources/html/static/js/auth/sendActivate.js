/* 判断字符串是否为空 */
function isNull(val) {
    var str = val.replace(/(^\s*)|(\s*$)/g, ''); //去除空格;
    return (str == '' || str == undefined || str == null)
}

/* 判断字符串为邮箱 */
function isEmail(str) {
    var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
    if (!reg.test(str)) {
        return false
    }
    return true
}

var timer = null
var mainTips = null

function alertMessage(message, isSuccess) {
    mainTips.text(message)
    if (isSuccess) {
        mainTips.css("color", "rgb(71, 146, 21)")
        if (timer != null) {
            clearInterval(timer)
        }
        $("#main-loading").css("display", "none")
        $("#main-email").attr('readonly', 'readonly')
    } else {
        mainTips.css("color", "rgb(238, 13, 13)")
        if (timer != null) {
            clearInterval(timer)
        }
        timer = setTimeout(function () {
            mainTips.text("")
            timer = null
        }, 3000)
    }
}

function loading() {
    $("#main-loading").css("display", "block")
    $("#main-button").css("display", "none")
}

function unload() {
    $("#main-loading").css("display", "none")
    $("#main-button").css("display", "block")
}

$(function () {
    mainTips = $("#main-tips")

    $("#main-button").on("click", function () {
        var email = $("#main-email").val()
        // 验证邮箱输入是否正确
        if (isNull(email)) {
            alertMessage("请输入邮箱")
            return
        } else if (!isEmail(email)) {
            alertMessage("请输入正确的邮箱")
            return
        }

        /* 从服务器获取信息 */
        loading()
        $.ajax({
            url: "/auth/sendActivate",
            type: "post",
            data: JSON.stringify({
                email: email
            }),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (response) {
                if (response.code == 0) {
                    alertMessage(response.message, true)
                } else {
                    unload()
                    alertMessage(response.message)
                }
            },
            error: function () {
                unload()
                alertMessage("服务器错误，请稍后重试")
            }
        });
    });
});