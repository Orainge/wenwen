var util
layui.use(['util'], function () {
    util = layui.util
})

function check(data) {
    var delayTime = 2
    if (data.nickname.length == 0) {
        setTipsMessage("请输入昵称", "fail", delayTime)
        return false
    }
    data.nickname = util.escape(data.nickname)
    data.simpleDescription = util.escape(data.simpleDescription)
    data.address = util.escape(data.address)
    data.industry = util.escape(data.industry)
    data.career = util.escape(data.career)
    data.education = util.escape(data.education)
    data.fullDescription = util.escape(data.fullDescription)
    data.userId = $.cookie("userId")
    return data
}

$("#confirm-button").on("click", function () {
    var formData = check(layui.form.val('modify-profile-form'))
    if (!formData) {
        return
    }
    tips("start")
    $.ajax({
        url: "/settings/profile",
        type: "post",
        data: JSON.stringify(formData),
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                tips("success", response.message)
                localStorage.setItem("nickname", formData.nickname)
                setTimeout(function () {
                    window.location.href = "/settings/profile"
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

var $tips = $("#modify-profile-tips-text")
var $loading = $("#modify-profile-loading")
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