var n_retryInterval = 3 // 获取通知失败后重试的间隔时间(秒)
var n_retryTimes = 3 // 获取通知失败后重试的次数

// -------- 接口名称 --------
var n_apiName = {
    feed: "/notification/lite/feed",
    message: "/notification/lite/message",
    like: "/notification/lite/like"
}

// -------- URL --------
var n_URL = {
    user: "/people/",
    collection: "/collection/",
    question: "/question/",
    question_commit: "/questionComment/",
    answer: "/answer/",
    answer_commit: "/answerComment/"
}

// ----------------- 获取通知 -----------------
// -------- 下一页获取的页码 --------
// 当等于 -1 时表示无下一页，不会加载
var n_nextPage = {
    feed: 1,
    message: 1,
    like: 1
}

// -------- 是否在加载 标识 --------
var n_loadingTag = {
    feed: false,
    message: false,
    like: false
}

// -------- 加载失败 标识 --------
var n_loadFailTag = {
    feed: false,
    message: false,
    like: false
}

// -------- 当前剩余重试次数 --------
var n_retryTimes_now = {
    feed: n_retryTimes,
    message: n_retryTimes,
    like: n_retryTimes
}

// from: 谁在加载；可选："feed", "message", "like"
// isScroll: 是否是下拉到底部的时候执行加载
function loadFromServer(from, isScroll) {
    n_loadingTag[from] = true
    $.ajax({
        url: n_apiName[from],
        type: "get",
        data: {
            page: n_nextPage[from],
        },
        success: function (response) {
            if (response.code == 0) {
                var data = response.data.data
                if (isScroll)
                    $("#success-loading-" + from).remove()
                if (from == "feed") {
                    pFeedData(data)
                } else if (from == "message") {
                    pMessageData(data)
                } else if (from == "like") {
                    pLikeData(data)
                }
                updateTotalHeight(from)
                if (response.data.end == 1) {
                    appendEndLoad(from, data)
                    n_nextPage[from] = -1
                } else {
                    n_nextPage[from] = n_nextPage[from] + 1
                }
                n_loadingTag[from] = false
            } else {
                lFS_fail(from, isScroll)
            }
        },
        error: function () {
            lFS_fail(from, isScroll)
        }
    })
}

// 没有更多数据了，需要附加结束提示
// from: 谁在加载；可选："feed", "message", "like"
function appendEndLoad(from, data) {
    if (from == "feed") {
        var feedTips
        if (n_nextPage.feed == 1 && (data == undefined || data.length == 0)) {
            feedTips = '你还没有收到任何消息哦'
        } else {
            feedTips = '没有更多信息了'
        }
        feedBoxS.append('<div id="success-end-loading-feed" class="end-loading">' + feedTips + '</div>')
        if (n_nextPage.feed == 1) {
            feedBoxS.scrollTop(0)
        } else {
            feedBoxS.scrollTop(feedBoxS.scrollTop() + $("#success-end-loading-feed").outerHeight(true))
        }
    } else if (from == "message") {
        var messageTips
        if (n_nextPage.message == 1 && (data == undefined || data.length == 0)) {
            messageTips = '你还没有收到任何消息哦'
        } else {
            messageTips = '没有更多信息了'
        }
        messageBoxS.append('<div id="success-end-loading-message" class="end-loading">' + messageTips + '</div>')
        if (n_nextPage.message == 1) {
            messageBoxS.scrollTop(0)
        } else {
            messageBoxS.scrollTop(messageBoxS.scrollTop() + $("#success-end-loading-message").outerHeight(true))
        }
    } else if (from == "like") {
        var likeTips
        if (n_nextPage.like == 1 && (data == undefined || data.length == 0)) {
            likeTips = '你还没有收到任何消息哦'
        } else {
            likeTips = '没有更多信息了'
        }
        likeBoxS.append('<div id="success-end-loading-like" class="end-loading">' + likeTips + '</div>')
        if (n_nextPage.like == 1) {
            likeBoxS.scrollTop(0)
        } else {
            likeBoxS.scrollTop(likeBoxS.scrollTop() + $("#success-end-loading-like").outerHeight(true))
        }
    }
}

// 加载失败
function lFS_fail(from, isScroll) {
    n_retryTimes_now[from] = n_retryTimes_now[from] - 1
    if (n_retryTimes_now[from] == 0) {
        // 结束重试，宣布获取失败
        if (n_nextPage[from] == 1) {
            $("#navi-notification-box-loading-" + from).css("display", "none")
            $("#navi-notification-box-loading-fail-" + from).css("display", "flex")
        } else {
            $("#success-loading-" + from).remove()
            updateTotalHeight(from)
        }
        n_loadFailTag[from] = true
        if (isScroll)
            $("#success-loading-" + from).remove()
    } else {
        // 短时间内重试
        setTimeout(function () {
            loadFromServer(from)
        }, n_retryInterval * 1000)
    }
}

// 处理 feed 数据
function pFeedData(data) {
    $.each(data, function (i, item) {
        var t = item.type
        var p = item.param
        var s = ""
        if (t == 4) {
            s += '<div>'
            s += '你关注的问题有了新回答 '
            s += '<span><a href="' + n_URL.question + p[0] + n_URL.answer + p[2] + '">' + p[1] + '</a></span>'
            s += '</div>'
        } else if (t == 3) {
            // 点赞某回答
            s += '<div>'
            s += '<span><a href="' + n_URL.user + p[0] + '">' + p[1] + '</a></span>'
            s += ' 赞同了回答 '
            s += '<span><a href="' + n_URL.question + p[2] + n_URL.answer + p[4] + '">' + p[3] + '</a></span>'
            s += '</div>'
        } else {
            s += '<div>'
            s += '<span><a href="' + n_URL.user + p[0] + '">' + p[1] + '</a></span>'
            if (t == 0) {
                s += " 提出"
            } else if (t == 1) {
                s += " 回答"
            } else if (t == 2) {
                s += " 关注"
            }
            s += '了问题 '
            s += '<span><a href="' + n_URL.question + p[2]
            if (t == 1)
                s += n_URL.answer + p[4]
            s += '">' + p[3] + '</a></span>'
            s += '</div>'
        }
        feedBoxS.append(s)
    })
    if (n_nextPage.feed == 1) {
        $("#navi-notification-box-loading-feed").css("display", "none")
        feedBoxS.css("display", "flex")
    }
}

// 处理 message 数据
function pMessageData(data) {
    $.each(data, function (i, item) {
        var t = item.type
        var st = item.subType
        var p = item.param
        var s = ""
        if (t != 2) {
            s += '<div>'
            s += '<span><a href="' + n_URL.user + p[0] + '">' + p[1] + '</a></span>'
            if (t == 0) {
                if (st == 0) {
                    s += ' 关注了你'
                } else if (st == 2) {
                    s += ' 关注了你的问题 '
                    s += '<span><a href="' + n_URL.question + p[2] + '">' + p[3] + '</a></span>'
                }
            } else if (t == 1) {
                if (st == 0) {
                    s += ' 评论了你的问题 '
                    s += '<span><a href="' + n_URL.question + p[2] + n_URL.question_commit + p[4] + '">' + p[3] + '</a></span>'
                } else if (st == 1) {
                    s += ' 评论了 '
                    s += '<span><a href="' + n_URL.question + p[2] + n_URL.answer_commit + p[4] + '">' + p[3] + '</a></span>'
                    s += ' 中你的回答'
                } else if (st == 2) {
                    s += ' 回复了 '
                    s += '<span><a href="' + n_URL.question + p[2] + n_URL.question_commit + p[4] + '">' + p[3] + '</a></span>'
                    s += ' 中你的评论'
                } else if (st == 3) {
                    s += ' 回复了 '
                    s += '<span><a href="' + n_URL.question + p[2] + n_URL.answer_commit + p[4] + '">' + p[3] + '</a></span>'
                    s += ' 中你的回答的评论'
                }
            }
        } else if (t == 2) {
            if (st == 0) {
                s += '<div><span>'
                if (p[0] == '-1' || p[0] == -1) {
                    s += '<a href="#">' + p[1] + '</a>'
                } else {
                    s += '<a href="' + n_URL.user + p[0] + '">' + p[1] + '</a>'
                }
                s += '</span>'
                s += ' 回答了你的问题 '
                s += '<span><a href="' + n_URL.question + p[2] + n_URL.answer + p[4] + '">' + p[3] + '</a></span>'
            }
        }
        s += '</div>'
        messageBoxS.append(s)
    })
    if (n_nextPage.message == 1) {
        $("#navi-notification-box-loading-message").css("display", "none")
        messageBoxS.css("display", "flex")
    }
}

// 处理 like 数据
function pLikeData(data) {
    console.log(data)
    $.each(data, function (i, item) {
        var t = item.type
        var p = item.param
        var s = ""
        s += '<div>'
        s += '<span><a href="' + n_URL.user + p[0] + '">' + p[1] + '</a></span>'
        if (t == 0) {
            s += ' 赞同了你的问题 '
            s += '<span><a href="' + n_URL.question + p[2] + '">' + p[3] + '</a></span>'
        } else if (t == 1) {
            s += ' 赞同了你的回答 '
            s += '<span><a href="' + n_URL.question + p[2] + n_URL.answer + p[4] + '">' + p[3] + '</a></span>'
        } else if (t == 2) {
            s += ' 赞同了你在 '
            s += '<span><a href="' + n_URL.question + p[2] + n_URL.question_commit + p[4] + '">' + p[3] + '</a></span>'
            s += ' 中的评论'
        } else if (t == 3) {
            s += ' 赞同了你在 '
            s += '<span><a href="' + n_URL.question + p[2] + n_URL.answer_commit + p[4] + '">' + p[3] + '</a></span>'
            s += ' 中的回答的评论'
        }
        s += '</div>'
        likeBoxS.append(s)
    })
    if (n_nextPage.like == 1) {
        $("#navi-notification-box-loading-like").css("display", "none")
        likeBoxS.css("display", "flex")
    }
}

// ----------------- 获取通知 -----------------

// ---------------通知里面按钮的操作---------------
var feedTab = $("#navi-notification-box-tab-feed")
var messageTab = $("#navi-notification-box-tab-message")
var likeTab = $("#navi-notification-box-tab-like")
var feedBox = $("#navi-notification-box-main-feed")
var messageBox = $("#navi-notification-box-main-message")
var likeBox = $("#navi-notification-box-main-like")
var feedBoxS = $("#navi-notification-box-success-feed")
var messageBoxS = $("#navi-notification-box-success-message")
var likeBoxS = $("#navi-notification-box-success-like")

// 加载feed
function loadFeed(isScroll) {
    if (!n_loadFailTag.feed && !n_loadingTag.feed && n_nextPage.feed != -1) {
        if (isScroll || n_nextPage.feed != 1) {
            feedBoxS.append('<div id="success-loading-feed" class="loading"></div>')
            feedBoxS.scrollTop(feedBoxS.scrollTop() + $("#success-loading-feed").outerHeight(true))
            loadFromServer("feed", true)
        } else {
            loadFromServer("feed")
        }
    }
}

// 加载个人消息
function loadMessage(isScroll) {
    if (!n_loadFailTag.message && !n_loadingTag.message && n_nextPage.message != -1) {
        if (isScroll || n_nextPage.message != 1) {
            messageBoxS.append('<div id="success-loading-message" class="loading"></div>')
            messageBoxS.scrollTop(messageBoxS.scrollTop() + $("#success-loading-message").outerHeight(true))
            loadFromServer("message", true)
        } else {
            loadFromServer("message")
        }
    }
}

// 加载点赞消息
function loadLike(isScroll) {
    if (!n_loadFailTag.like && !n_loadingTag.like && n_nextPage.like != -1) {
        if (isScroll || n_nextPage.like != 1) {
            likeBoxS.append('<div id="success-loading-like" class="loading"></div>')
            likeBoxS.scrollTop(likeBoxS.scrollTop() + $("#success-loading-like").outerHeight(true))
            loadFromServer("like", true)
        } else {
            loadFromServer("like")
        }
    }
}

feedTab.click(function () {
    clickNotificationTab("feed")
})

messageTab.click(function () {
    clickNotificationTab("message")
})

likeTab.click(function () {
    clickNotificationTab("like")
})

function clickNotificationTab(from) {
    if (from == "feed") {
        if (feedBox.css("display") == "flex") return
        cNT_helper(1)
        cNT_helper(4)
        cNT_helper(6)
        feedBox.css("display", "flex")
        messageBox.css("display", "none")
        likeBox.css("display", "none")
        loadFeed()
    } else if (from == "message") {
        if (messageBox.css("display") == "flex") return
        cNT_helper(2)
        cNT_helper(3)
        cNT_helper(6)
        feedBox.css("display", "none")
        messageBox.css("display", "flex")
        likeBox.css("display", "none")
        loadMessage()
    } else if (from == "like") {
        if (likeBox.css("display") == "flex") return
        cNT_helper(2)
        cNT_helper(4)
        cNT_helper(5)
        feedBox.css("display", "none")
        messageBox.css("display", "none")
        likeBox.css("display", "flex")
        loadLike()
    }
}

function cNT_helper(code) {
    switch (code) {
        case 1:
            feedTab.css("background-image", 'url("/static/img/icon/feed_selected.png")')
            feedTab.hover(function () {
                feedTab.css("background-image", 'url("/static/img/icon/feed_selected.png")')
            }, function () {
                feedTab.css("background-image", 'url("/static/img/icon/feed_selected.png")')
            })
            break;
        case 2:
            if (messageBox.css("display") == "flex") return
            feedTab.css("background-image", 'url("/static/img/icon/feed_unselect.png")')
            feedTab.hover(function () {
                feedTab.css("background-image", 'url("/static/img/icon/feed_hover.png")')
            }, function () {
                feedTab.css("background-image", 'url("/static/img/icon/feed_unselect.png")')
            })
            break;
        case 3:
            messageTab.css("background-image", 'url("/static/img/icon/message_selected.png")')
            messageTab.hover(function () {
                messageTab.css("background-image", 'url("/static/img/icon/message_selected.png")')
            }, function () {
                messageTab.css("background-image", 'url("/static/img/icon/message_selected.png")')
            })
            break;
        case 4:
            messageTab.css("background-image", 'url("/static/img/icon/message_unselect.png")')
            messageTab.hover(function () {
                messageTab.css("background-image", 'url("/static/img/icon/message_hover.png")')
            }, function () {
                messageTab.css("background-image", 'url("/static/img/icon/message_unselect.png")')
            })
            break;
        case 5:
            likeTab.css("background-image", 'url("/static/img/icon/like_selected.png")')
            likeTab.hover(function () {
                likeTab.css("background-image", 'url("/static/img/icon/like_selected.png")')
            }, function () {
                likeTab.css("background-image", 'url("/static/img/icon/like_selected.png")')
            })
            break;
        case 6:
            likeTab.css("background-image", 'url("/static/img/icon/like_unselect.png")')
            likeTab.hover(function () {
                likeTab.css("background-image", 'url("/static/img/icon/like_hover.png")')
            }, function () {
                likeTab.css("background-image", 'url("/static/img/icon/like_unselect.png")')
            })
            break;
    }
}
// ---------------通知里面按钮的操作---------------

// --------------- 通知里面滚动到底部自动加载 ---------------
// 通知内的总高度
var boxHeight = {
    "default": 300, // css设置默认高度为300px
    "feed": 0,
    "message": 0,
    "like": 0
}

function updateTotalHeight(from) {
    boxHeight[from] = 0
    $("#navi-notification-box-success-" + from + " div").each(function () {
        boxHeight[from] += $(this).outerHeight(true)
    })
}

// 通知里面滚动到底部自动加载
$(function () {
    feedBoxS.scroll(function () {
        if (feedBoxS.scrollTop() == boxHeight.feed - boxHeight.default) {
            loadFeed(true)
        }
    })

    messageBoxS.scroll(function () {
        if (messageBoxS.scrollTop() == boxHeight.message - boxHeight.default) {
            loadMessage(true)
        }
    })

    likeBoxS.scroll(function () {
        if (likeBoxS.scrollTop() == boxHeight.like - boxHeight.default) {
            loadMessage(true)
        }
    })
})
// --------------- 通知里面滚动到底部自动加载 ---------------

// 预加载图片
$(function () {
    $.preload([
        '/static/img/icon/feed_unselect.png',
        '/static/img/icon/feed_hover.png',
        '/static/img/icon/message_hover.png',
        '/static/img/icon/message_selected.png',
        '/static/img/icon/like_hover.png',
        '/static/img/icon/like_selected.png'
    ]);
})

// ------------- 在 navi.js 中执行 -------------
// 当打开通知的时候执行的操作
function n_showNotification() {
    if (feedBox.css("display") == "flex") {
        loadFeed()
    } else if (messageBox.css("display") == "flex") {
        loadMessage()
    } else if (likeBox.css("display") == "flex") {
        loadLike()
    }
}
// ------------- 在 navi.js 中执行 -------------