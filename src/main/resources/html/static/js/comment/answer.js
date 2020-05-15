var answerId, questionId
var atUserId
var likeList = {}
var baseAPIUrl = "/answerComment"
var nextPage = 1;
var layuiUtil
var $mainDiv = $("#content")
var userId = $.cookie("userId")

// 初始化弹窗
layui.use(['util', 'form'], function () {
    var form = layui.form
    layuiUtil = layui.util
    form.render()
})

function initComment(qId, aId) {
    questionId = qId
    answerId = aId
    getComment()
}

function initOne(qId, aId, cId) {
    questionId = qId
    answerId = aId
    getOne(cId)
}

var loadingTag = false

function getComment() {
    if (!loadingTag && nextPage != -1) {
        loadingTips("start")
        loadingTag = true

        $.ajax({
            url: baseAPIUrl,
            type: "get",
            data: {
                answerId: answerId,
                nextPage: nextPage
            },
            success: function (response) {
                if (response.code == 0) {
                    let end = response.data.end
                    var data = response.data.data
                    loadingTips("stop")
                    processData(data)
                    let isNullData = nextPage == 1 && (data == undefined || data == null || data.length == 0)
                    appendEnd(end, isNullData)
                    if (end == 1) {
                        nextPage = -1
                    } else {
                        nextPage += 1
                    }
                    loadingTag = false
                } else {
                    loadingTips("stop")
                    loadingFail()
                    loadingTag = false
                }
            },
            error: function () {
                loadingTips("stop")
                loadingFail()
                loadingTag = false
            }
        })
    }
}

function getOne(commentId) {
    if (!loadingTag && nextPage != -1) {
        loadingTips("start")
        loadingTag = true

        $.ajax({
            url: baseAPIUrl + "/" + commentId,
            type: "get",
            data: {
                questionId: questionId,
                answerId: answerId,
                nextPage: nextPage
            },
            success: function (response) {
                if (response.code == 0) {
                    let end = response.data.end
                    var data = response.data.data
                    loadingTips("stop")
                    addLoadAllTips()
                    processData(data)
                    let isNullData = nextPage == 1 && (data == undefined || data == null || data.length == 0)
                    appendEnd(end, isNullData)
                    if (end == 1) {
                        nextPage = -1
                    } else {
                        nextPage += 1
                    }
                    loadingTag = false
                } else {
                    loadingTips("stop")
                    loadingFail()
                    loadingTag = false
                }
            },
            error: function () {
                loadingTips("stop")
                loadingFail()
                loadingTag = false
            }
        })
    }
}

function addLoadAllTips() {
    // 点击加载所有评论 的按钮，在只显示一条评论的时候显示
    let s = '<div id="show-all-comment">显示所有评论</div>'
    $mainDiv.append(s)
    $("#show-all-comment").on("click", function () {
        reloadComment()
    })
}

var loadingTipsStr = '<div id="loading-tips"><img src="/static/img/loading_horizontal.gif"></div>'
var loadMoreBtnStr = '<div id="load-more">加载更多</div>'
var reloadBtnStr = '<div id="load-more">重新加载</div>'

function processData(data) {
    for (let i = 0; i < data.length; i++) {
        let item = data[i]
        let s = ""
        s += '<div class="item">'
        s += '<div class="up">'
        s += '<img src="' + item.avatarUrl + '">'
        // s += '<a href="/people/' + item.userId + '">'
        s += '<span title="' + item.nickname + '的个人主页" class="nickname" id="reply-nickname-' + item.commentId + '">' + item.nickname + '</span>'
        // s += '</a>'
        if (item.atUserId) {
            s += '<div class="at">'
            s += '回复'
            // s += '<a href="/people/' + item.atUserId + '">'
            s += '<span title="' + item.atNickname + '的个人主页" class="nickname" id="reply-at-nickname-' + item.commentId + '">' + item.atNickname + '</span>'
            // s += '</a>'
            s += '</div>'
        }
        s += '</div>'
        if (item.isDelete == 1) {
            s += '<div class="content content-delete">评论已删除</div>'
        } else {
            s += '<div class="content">' + item.content + '</div>'
            s += '<div class="bottom">'
            s += '<div id="like-button-' + item.commentId + '" class="bottom-item">'
            if (item.isLike == 1) {
                s += '<img id="like-button-status-' + item.commentId + '" src="/static/img/icon/reply-like.png">'
                likeList[item.commentId] = true
            } else {
                s += '<img id="like-button-status-' + item.commentId + '" src="/static/img/icon/reply-unlike.png">'
                likeList[item.commentId] = false
            }
            s += '<span id="like-count-' + item.commentId + '">' + item.countLike + '</span>'
            s += '</div>'
            if (item.userId != userId) {
                s += '<div id="reply-button-' + item.commentId + '" class="bottom-item">'
                s += '<img src="/static/img/icon/reply-reply.png">'
                s += '<span>回复</span>'
                s += '</div>'
            }
            if (item.userId == userId) {
                s += '<div id="delete-button-' + item.commentId + '" class="bottom-item">'
                s += '<img src="/static/img/icon/reply-delete.png">'
                s += '<span>删除</span>'
                s += '</div>'
            }
            s += '</div>'
        }
        s += '</div>'
        $mainDiv.append(s)


        $("#reply-nickname-" + item.commentId).on("click", function () {
            // 父级页面跳转
            parent.location.href = "/people/" + item.userId
        })

        if (item.atUserId) {
            $("#reply-at-nickname-" + item.commentId).on("click", function () {
                // 父级页面跳转
                parent.location.href = "/people/" + item.atUserId
            })
        }

        if (item.userId == userId) {
            $("#delete-button-" + item.commentId).on("click", function () {
                let confirmTips = layer.confirm('你确定要删除这条回复吗？', {
                    title: "警告",
                    btn: ['确定', '取消'], //按钮
                    skin: 'layui-layer-lan', //样式类名
                    scrollbar: false,
                    closeBtn: 0,
                    move: false,
                    anim: 5,
                    isOutAnim: false,
                    icon: 0
                }, function () {
                    layer.close(confirmTips)
                    deleteComment(item.commentId)
                })
            })
        } else {
            $("#like-button-" + item.commentId).on("click", function () {
                if (likeList[item.commentId]) {
                    changeLike(item.commentId, "unlike")
                } else {
                    changeLike(item.commentId, "like")
                }
            })
            $("#reply-button-" + item.commentId).on("click", function () {
                atUser("at", item.userId, item.nickname)
            })
        }
    }
}

function loadingTips(type) {
    if (type == "start") {
        $("#load-more").remove()
        $mainDiv.append(loadingTipsStr)
    } else if (type == "stop") {
        $("#loading-tips").remove()
    }
}

function appendEnd(end, isNullData) {
    if (end == 1) {
        if (isNullData) {
            var s = '<div id="no-comment-tips">目前还没有人评论哦！</div>'
            $mainDiv.append(s)
        } else {
            // 不显示到底了
        }
    } else {
        $mainDiv.append(loadMoreBtnStr)
        $("#load-more").on("click", function () {
            getComment()
        })
    }
}

function loadingFail() {
    $mainDiv.append(reloadBtnStr)
    $("#load-more").on("click", function () {
        getComment()
    })
}

function deleteComment(answerCommentId) {
    let delay = 20 * 1000
    let tips = layer.msg('删除中', {
        icon: 16,
        time: delay,
        shade: [0.3, "#000"]
    })

    function error() {
        layer.close(tips)
        layer.msg('删除失败，请稍后重试', {
            icon: 2,
            time: 3000,
            shade: [0.3, "#000"]
        })
    }

    $.ajax({
        url: baseAPIUrl,
        type: "delete",
        data: JSON.stringify({
            answerCommentId: answerCommentId
        }),
        dataType: "json",
        timeout: 20 * 1000,
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                layer.close(tips)
                reloadComment()
            } else {
                error()
            }
        },
        error: function () {
            error()
        },
        complete: function () {
            if (status == 'timeout') {
                error()
            }
        }
    })
}

function commitComment() {
    // 提交回复
    var content = layuiUtil.escape($("#reply-content").val())
    if (content.length == 0) {
        layer.msg('请输入内容', {
            icon: 2,
            time: 1500,
            shade: [0.3, "#000"]
        })
        return
    }

    let delay = 20 * 1000
    let tips = layer.msg('提交中', {
        icon: 16,
        time: delay,
        shade: [0.3, "#000"]
    })

    function commitError() {
        layer.close(tips)
        layer.msg('提交失败，请稍后重试', {
            icon: 2,
            time: 3000,
            shade: [0.3, "#000"]
        })
    }

    var data = {
        answerId: parseInt(answerId),
        content: content,
        questionId: parseInt(questionId)
    }
    if (answerId != undefined || answerId != null) {
        data.atUserId = parseInt(atUserId)
    }

    $.ajax({
        url: baseAPIUrl,
        type: "post",
        data: JSON.stringify(data),
        dataType: "json",
        timeout: 20 * 1000,
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                layer.close(tips)
                $("#reply-content").val("")
                atUser("reset")
                reloadComment()
            } else {
                commitError()
            }
        },
        error: function () {
            commitError()
        },
        complete: function () {
            if (status == 'timeout') {
                commitError()
            }
        }
    })
}

function reloadComment() {
    $mainDiv.empty()
    nextPage = 1
    getComment()
}

function changeLike(answerCommentId, type) {
    let method, likeStatus
    if (type == "like") {
        method = "post"
        likeStatus = true
    } else if (type == "unlike") {
        method = "delete"
        likeStatus = false
    } else {
        return
    }
    $.ajax({
        url: baseAPIUrl + "/like",
        type: method,
        data: JSON.stringify({
            answerCommentId: answerCommentId
        }),
        dataType: "json",
        timeout: 20 * 1000,
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                $("#like-count-" + answerCommentId).html(response.data)
                likeList[answerCommentId] = likeStatus
                changeLikeButtonStatus(answerCommentId, type)
            }
        }
    })
}

function changeLikeButtonStatus(answerCommentId, type) {
    if (type == "like") {
        $("#like-button-status-" + answerCommentId).attr("src", "/static/img/icon/reply-like.png")
        $("#like-count-" + answerCommentId).css("color", "#1296DB")
    } else if (type == "unlike") {
        $("#like-button-status-" + answerCommentId).attr("src", "/static/img/icon/reply-unlike.png")
        $("#like-count-" + answerCommentId).css("color", "#000000")
    }
}

function atUser(type, atId, atNickname) {
    if (type == "at") {
        atUserId = atId
        $("#reply-tips").html("回复 " + atNickname)
        $("#reply-reset-comment").css("display", "block")
        $("#reply-reset-comment").on("click", function () {
            atUser("reset")
        })
    } else if (type == "reset") {
        atUserId = undefined
        $("#reply-tips").html("评论")
        $("#reply-reset-comment").css("display", "none")
    }
}

$("#reply-commit-comment").on("click", function () {
    commitComment()
})

// ----------------- 调试用 -----------------
$("#like-button-xxx").on("click", function () {
    if (likeList.xxx) {
        changeLike("xxx", "unlike")
    } else {
        changeLike("xxx", "like")
    }
})

$("#reply-button-xxx").on("click", function () {
    atUser("at", "atUserId", "我是大帅哥")
})

$("#delete-button-xxx").on("click", function () {
    var confirmTips = layer.confirm('你确定要删除这条回复吗？', {
        title: "警告",
        btn: ['确定', '取消'], //按钮
        skin: 'layui-layer-lan', //样式类名
        scrollbar: false,
        closeBtn: 0,
        move: false,
        anim: 5,
        isOutAnim: false,
        icon: 0
    }, function () {
        layer.close(confirmTips)
        deleteComment("xxx")
    })
})