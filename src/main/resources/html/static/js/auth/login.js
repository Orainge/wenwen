$(document.head).append("<script type='text/javascript' src='/static/js/util/md5.js'></script>")

/* 用于开关的显示 */
layui.use('form', function () {
    layui.form.render()
})

/* 弹窗 - 找回密码 */
$('#login-reset_password').click(function () {
    layer.open({
        type: 2,
        title: "找回密码",
        move: false,
        resize: false,
        area: ['330px', '300px'],
        content: '/auth/sendReset',
    });
})

/* 弹窗 - 重新发送激活链接 */
$('#login-resend_email').click(function () {
    layer.open({
        type: 2,
        title: "发送激活链接",
        move: false,
        resize: false,
        area: ['330px', '300px'],
        content: '/auth/sendActivate',
    });
})

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

var delayTime = 3000 //ms
var timer1 = null
var timer2 = null
var loginButton = $("#login-button")
var registerButton = $("#register-button")
var loginLoading = $("#login-loading")
var registerLoading = $("#register-loading")
var registerSuccessMessage = $("#register-success-message")

/* 登录错误的提示 */
function alertLoginMessage(message) {
    $("#login-message").text(message)
    if (timer1 != null) {
        clearInterval(timer1)
    }
    timer1 = setTimeout(function () {
        clearMessage('login')
        timer1 = null
    }, delayTime)
}

/* 注册错误的提示 */
function alertRegisterMessage(message) {
    $("#register-message").text(message)
    if (timer2 != null) {
        clearInterval(timer2)
    }
    timer2 = setTimeout(function () {
        clearMessage('register')
        timer2 = null
    }, delayTime)
}

/* 注册成功的提示 */
function alertRegisterSuccess() {
    registerLoading.css("display", "none")
    registerSuccessMessage.css("display", "block")
    /* 禁止用户再次输入注册信息 */
    $("#register-input-email").attr('readonly', 'readonly')
    $("#register-input-username").attr('readonly', 'readonly')
    $("#register-input-password").attr('readonly', 'readonly')
}

function clearMessage(from) {
    $("#" + from + "-message").text("")
}

/* 标签切换 */
function tabSwitch(fromId) {
    var i;
    for (i = 0; i <= 1; i++) {
        if (i == fromId) {
            $("#main-box-input-" + i).css("display", "block")
            $("#main-box-tab-" + i).css("border-color", "rgb(0, 49, 79)")
        } else {
            $("#main-box-input-" + i).css("display", "none")
            $("#main-box-tab-" + i).css("border-color", "white")
        }
    }
}

function loading(from) {
    if (from == "login") {
        clearMessage('login')
        loginButton.css("display", "none")
        loginLoading.css("display", "block")
    } else if (from == "register") {
        clearMessage('register')
        registerButton.css("display", "none")
        registerLoading.css("display", "block")
    }
}

function unload(from) {
    if (from == "login") {
        loginButton.css("display", "block")
        loginLoading.css("display", "none")
    } else if (from == "register") {
        registerButton.css("display", "block")
        registerLoading.css("display", "none")
    }
}

/* 密码框回车直接登录 */
$("#login-input-password").keypress(function (event) {
    if (event.which === 13) {
        login()
    }
})

$("#login-button").click(login)

$("#register-button").click(function () {
    /* 邮箱 */
    var registerEmail = $("#register-input-email").val()
    if (isNull(registerEmail)) {
        alertRegisterMessage("请输入邮箱")
        return
    }
    /* 是否输入了正确的邮箱 */
    if (!isEmail(registerEmail)) {
        alertRegisterMessage("请输入正确的邮箱")
        return
    }
    /* 用户名 */
    var registerUsername = $("#register-input-username").val()
    if (isNull(registerUsername)) {
        alertRegisterMessage("请输入用户名")
        return
    }
    /* 密码 */
    var registerPassword = $("#register-input-password").val()
    if (isNull(registerPassword)) {
        alertRegisterMessage("请输入密码")
        return
    }

    var registerRePassword = $("#register-input-re_password").val()
    if (isNull(registerRePassword)) {
        alertRegisterMessage("请确认你输入的密码")
        return
    }

    if (registerPassword.length < 6) {
        alertRegisterMessage("密码长度不能少于6位")
        return
    }

    if (registerPassword != registerRePassword) {
        alertRegisterMessage("两次输入的密码不一致")
        return
    }

    loading('register')
    $.ajax({
        url: "/auth/register",
        type: "post",
        data: JSON.stringify({
            email: registerEmail,
            username: registerUsername,
            password: md5(registerPassword)
        }),
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                alertRegisterSuccess()
            } else {
                unload('register')
                alertRegisterMessage(response.message)
            }
        },
        error: function () {
            unload('register')
            alertRegisterMessage("服务器错误，请稍后重试")
        }
    })
})

/* 执行登录操作 */
function login() {
    /* 用户名或邮箱 */
    var loginPrincipal = $("#login-input-principal").val()
    if (isNull(loginPrincipal)) {
        alertLoginMessage("请输入用户名")
        return
    }
    /* 密码 */
    var loginPassword = $("#login-input-password").val()
    if (isNull(loginPassword)) {
        alertLoginMessage("请输入密码")
        return
    }
    /*是否记住我 */
    var rememberMe = $("#login-input-remember_me")[0].checked ? 1 : 0;

    /* 从服务器获取信息 */
    loading('login')
    $.ajax({
        url: "/login",
        type: "post",
        data: JSON.stringify({
            principal: loginPrincipal,
            password: md5(loginPassword),
            rememberMe: rememberMe
        }),
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                var user = response.data.userInfo
                // 保存用户信息
                $.cookie('userId', user.userId, {
                    expires: 7,
                    path: '/'
                })
                localStorage.setItem("nickname", user.nickname)
                localStorage.setItem("avatarUrl", user.avatarUrl)
                window.location.href = response.data.redirectUrl
            } else {
                unload('login')
                alertLoginMessage(response.message)
            }
        },
        error: function () {
            unload('login')
            alertLoginMessage("服务器错误，请稍后重试")
        }
    })
}

$(function () {
    localStorage.removeItem('nickname')
    localStorage.removeItem('avatarUrl')
    $.cookie('userId', null)
})