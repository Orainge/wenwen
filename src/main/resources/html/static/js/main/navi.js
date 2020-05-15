var notificationIsDisplay = false // 通知浮窗是否为显示
var userMenuIsDisplay = false // 用户浮窗是否为显示
var mainDivWidth = 1000 // 主框架宽度
var notificationAddWidth = 100 // 弹出通知后需要增加的宽度
var userMenuAddWidth = 40 // 弹出用户菜单后需要增加的宽度
var notificationInAnimate = false; // notification 是否在展示动画
var userMenuInAnimate = false; // userMenu 是否在展示动画
var animateSpeed = 200 // 弹窗淡出的时间(毫秒)

// -----------------------问题搜索--------------------------
// 点击搜索按钮时执行搜索操作
$("#navi-search-button").click(naviSearch)

// 搜索框回车时执行搜索
$("#navi-search-input").keypress(function (event) {
    if (event.which === 13) {
        naviSearch();
    }
})

// 执行搜索操作
function naviSearch() {
    var str = $("#navi-search-input").val()
    if (str == '' || str == undefined || str == null) return
    window.location.href = "/search?content=" + str
}
// -----------------------问题搜索--------------------------

// -------------------------提问----------------------------
// 初始化提问弹窗
layui.use('form', function () {
    var form = layui.form;
    form.render()
})

// 点击提问按钮
$("#navi-ask-button").click(function () {
    layer.open({
        type: 2,
        title: "提问",
        move: false,
        resize: false,
        skin: 'layui-layer-lan',
        area: ['700px', '500px'],
        content: '/ask',
        scrollbar: false,
        anim: 5,
    });
})
// -------------------------提问----------------------------

// -------------------------浮窗----------------------------
// 点击通知按钮
$("#navi-notification-button").click(function () {
    if (!notificationInAnimate && !userMenuInAnimate) {
        notificationInAnimate = true
        if (userMenuIsDisplay) {
            managePop("userMenu", "close", true)
        }
        if (notificationIsDisplay) {
            managePop("notification", "close")
        } else {
            managePop("notification", "open")
        }
    }
})

// 点击用户头像
$("#navi-user-avatar").click(function (event) {
    if (!notificationInAnimate && !userMenuInAnimate) {
        userMenuInAnimate = true
        if (notificationIsDisplay) {
            managePop("notification", "close", true)
        }
        if (userMenuIsDisplay) {
            managePop("userMenu", "close")
        } else {
            managePop("userMenu", "open")
        }
    }
})

// 点击页面空白任意处关闭两个通知浮窗（如果它们被打开）
$(document).click(function (event) {
    var target = $(event.target)
    if (target.closest("#navi-user-avatar").length != 0) return
    if (target.closest("#navi-user-menu-box-div").length != 0) return
    if (target.closest("#navi-notification-button").length != 0) return
    if (target.closest("#navi-notification-box-div").length != 0) return

    if (notificationIsDisplay) {
        managePop("notification", "close")
    }
    if (userMenuIsDisplay) {
        managePop("userMenu", "close")
    }
})

// 管理弹窗的状态
// notToOriginal: 不将 main div 调整为原来的大小
function managePop(from, status, notToOriginal) {
    if (status == "open") {
        if (from == "notification") {
            n_showNotification() // 在 notification.js 中处理
            notificationIsDisplay = true
            adjustMainDivWidth("notification")
            $("#navi-notification-box-div").fadeIn(animateSpeed, function () {
                notificationInAnimate = false;
            })
        } else if (from == "userMenu") {
            userMenuIsDisplay = true
            adjustMainDivWidth("userMenu")
            $("#navi-user-menu-box-div").fadeIn(animateSpeed, function () {
                userMenuInAnimate = false;
            })
        }
    } else if (status == "close") {
        if (from == "notification") {
            $("#navi-notification-box-div").fadeOut(animateSpeed, function () {
                if (!notToOriginal)
                    adjustMainDivWidth("original")
                notificationInAnimate = false;
            })
            notificationIsDisplay = false
        } else if (from == "userMenu") {
            $("#navi-user-menu-box-div").fadeOut(animateSpeed, function () {
                if (!notToOriginal)
                    adjustMainDivWidth("original")
                userMenuInAnimate = false;
            })
            userMenuIsDisplay = false
        }
    }
}

// (打开/关闭) 通知后 main div 的宽度调整
function adjustMainDivWidth(status) {
    if (status == "original") {
        $("#main").css("width", mainDivWidth)
        $("#main").css("margin-left", 0)
    } else {
        if (status == "notification") {
            $("#main").css("width", mainDivWidth + notificationAddWidth)
            $("#main").css("margin-left", notificationAddWidth)
        } else if (status == "userMenu") {
            $("#main").css("width", mainDivWidth + userMenuAddWidth)
            $("#main").css("margin-left", userMenuAddWidth)
        }
        // 滑动到窗口最右边
        $('html,body').animate({
            scrollLeft: $("#main").width()
        }, 0);
    }
}
// -------------------------浮窗----------------------------

// 滚动条的操作
$(document).scroll(function (e) {
    // 以下为窗口横向缩小后滚动上方导航栏
    var sl = -Math.max(document.body.scrollLeft, document.documentElement.scrollLeft);
    $("#navi-div").css("left", sl + "px")
})

$(function () {
    // 预加载图片
    $.preload([
        '/static/img/icon/notification-all_hover.png',
        '/static/img/icon/notification_hover.png',
        '/static/img/icon/search_hover.png',
    ]);
})

// ------------------ 加载用户头像 ------------------
$("#navi-user-avatar").attr('src', localStorage.getItem("avatarUrl"))
// ------------------ 加载用户头像 ------------------

// -------------------以下为页面加载时调用的设置-------------------
// 设置当前页面属于哪个Tab，不设置则不标注Tab的下划线
// nowTab取值: index: 首页;  topic: 话题
// explore: 发现 暂时取消
function naviNowTab(nowTab) {
    if (nowTab != "") {
        $("#navi-tab-" + nowTab).css("color", "#000")
        $("#navi-tab-" + nowTab).css("font-weight", "800")
        $("#navi-tab-" + nowTab).css("border-color", "rgb(0, 111, 180)")
        $("#navi-tab-" + nowTab).css("border-color", "rgb(0, 111, 180)")
    }
}

function setSearchBoxKeyword(keyword) {
    $("#navi-search-input").val(keyword)
}