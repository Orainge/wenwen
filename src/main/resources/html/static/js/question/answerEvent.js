var nextPage = 1
var loadingTag = false
var isLikeList = {}

function loadAnswerFromServer() {
    if (!loadingTag && nextPage != -1) {
        loadingTag = true
        loadingTips("start")
        $.ajax({
            url: "/answer/get/",
            type: "get",
            data: {
                questionId: questionId,
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

function loadSpecificAnswer(answerId) {
    if (!loadingTag && nextPage != -1) {
        loadingTag = true
        loadingTips("start")
        $.ajax({
            url: "/question/" + questionId + "/answer/" + answerId + "/get",
            type: "get",
            success: function (response) {
                if (response.code == 0) {
                    let end = response.data.end
                    var data = response.data.data
                    loadingTips("stop")
                    appendShowAllAnswer()
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

var $mainDiv = $("#question-main-div")
var loadingTipsStr = '<div id="loading-tips"><img src="/static/img/loading_horizontal.gif"></div>'
var loadMoreBtnStr = '<div id="load-more-answer" class="answer-main-content-load-more">加载更多</div>'
var reloadBtnStr = '<div id="load-more-answer" class="answer-main-content-load-more">重新加载</div>'

function loadingTips(type) {
    if (type == "start") {
        $("#load-more-answer").remove()
        $mainDiv.append(loadingTipsStr)
    } else if (type == "stop") {
        $("#loading-tips").remove()
    }
}

function appendEnd(end, isNullData) {
    if (end == 1) {
        if (isNullData) {
            var s = '<div id="no-answer-tips">目前还没有人回答该问题，快去写第一个答案吧！</div>'
            $mainDiv.append(s)
        } else {
            // 不显示到底了
        }
    } else {
        $mainDiv.append(loadMoreBtnStr)
        $("#load-more-answer").on("click", function () {
            loadAnswerFromServer()
        })
    }
}

function loadingFail() {
    $mainDiv.append(reloadBtnStr)
    $("#load-more-answer").on("click", function () {
        loadAnswerFromServer()
    })
}

function appendShowAllAnswer() {
    let s = '<div id="show-all-answer" class="main-div show-all">显示所有答案</div>'
    $mainDiv.append(s)
    $("#show-all-answer").on("click", function () {
        window.location.href = "/question/" + questionId
    })
}

function processData(data) {
    if (data == undefined || data == null || data.length == 0) return

    for (let i = 0; i < data.length; i++) {
        let item = data[i]
        let nowIndex = item.answerId
        let createDate = new Date(item.createTime)
        let time = createDate.getFullYear() + " 年 " + (createDate.getMonth() + 1) + " 月 " + createDate.getDate() + " 日"

        if ((item.userId != undefined || item.userId != null) && (item.simpleDescription == undefined || item.simpleDescription == null || item.simpleDescription == "")) {
            item.simpleDescription = "暂无简介"
        }

        let s = '<div id="item-' + nowIndex + '" class="item main-div"><div class="top">'

        if (item.userId == undefined || item.userId == null) {
            s += '<div class="anonymous">'
            s += '<img src="/static/img/icon/avatar.png">'
            s += '<div class="info">'
            s += '<span class="username">匿名</span>'
            s += '<span class="simple-description"></span>'
            s += '<span class="recently-edit">编辑于 ' + time + '</span>'
            s += '</div>'
            s += '</div>'
        } else {
            s += ' <a href="/people/' + item.userId + '">'
            s += '<img src="' + item.avatarUrl + '">'
            s += '<div class="info">'
            s += '<span class="username">' + item.nickname + '</span>'
            s += '<span class="simple-description">' + item.simpleDescription + '</span>'
            s += '<span class="recently-edit">编辑于 ' + time + '</span>'
            s += '</div>'
            s += '</a>'
        }
        if (item.isShort == 1) {
            s += '<div class="short">短回答</div>'
        }
        s += '</div>'
        s += '<div class="content">'
        s += item.content
        s += '</div>'
        s += '<div class="bottom">'
        s += '<div class="left">'
        if (item.userId == userId) {
            s += '<div id="like-' + nowIndex + '" class="button button-forbidden">'
        } else {
            s += '<div id="like-' + nowIndex + '" class="button">'
        }
        s += '<img src="/static/img/icon/question-answer-like.png">'
        s += '<div id="like-count-' + nowIndex + '" class="count">' + item.countLike + '</div>'
        s += '</div>'
        s += '<div id="comment-' + nowIndex + '" class="button">'
        s += '<img src="/static/img/icon/question-answer-comment.png">'
        s += '<div id="comment-count-' + nowIndex + '" class="count">' + item.countCommit + '</div>'
        s += '</div>'
        s += '</div>'
        if (item.userId == userId) {
            s += '<div id="del-' + nowIndex + '" class="right">'
            s += '<div class="button">'
            s += '<img src="/static/img/icon/question-answer-delete.png">'
            s += '</div></div>'
        }
        s += '</div></div>'
        $mainDiv.append(s)

        if (item.isLike == 1) {
            isLikeList[item.answerId] = true
            changeLikeButtonStatus(item.answerId, "like")
        }
        if (item.userId != userId) {
            $("#like-" + nowIndex).on("click", function () {
                like(item.answerId)
            })
        }

        $("#comment-" + nowIndex).on("click", function () {
            comment(item.answerId)
        })
        if (item.userId == userId) {
            $("#del-" + nowIndex).on("click", function () {
                let confirmTips = layer.confirm('你确定要删除这个回答吗？', {
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
                    deleteAnswer(item.answerId)
                })
            })
        }
    }
}

var tempLikeTag = {}

function like(answerId) {
    if (tempLikeTag[answerId] != true) {
        tempLikeTag[answerId] = true
        let type = isLikeList[answerId] ? "delete" : "post"

        $.ajax({
            url: "/answer/like",
            type: type,
            data: JSON.stringify({
                answerId: answerId,
            }),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (response) {
                if (response.code == 0) {
                    $("#like-count-" + answerId).html(response.data)
                    if (type == "post") {
                        changeLikeButtonStatus(answerId, "like")
                        isLikeList[answerId] = true
                    } else if (type == "delete") {
                        changeLikeButtonStatus(answerId, "unlike")
                        isLikeList[answerId] = false
                    }
                }
                tempLikeTag[answerId] = false
            },
            error: function () {
                tempLikeTag[answerId] = false
            }
        })
    }
}

function deleteAnswer(answerId) {
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
        url: "/answer/",
        type: "delete",
        data: JSON.stringify({
            answerId: answerId
        }),
        dataType: "json",
        timeout: 20 * 1000,
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                layer.close(tips)
                // 删除对应的元素
                $("#item-" + answerId).remove()
                if ($("#question-main-div").children().length == 0) {
                    var s = '<div id="no-answer-tips">目前还没有人回答该问题，快去写第一个答案吧！</div>'
                    $mainDiv.append(s)
                }
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

function comment(answerId) {
    // 弹窗
    console.log("answerId: " + answerId)
    layer.open({
        type: 2,
        title: "评论",
        move: false,
        resize: false,
        skin: 'layui-layer-lan',
        area: ['500px', '700px'],
        content: '/answerComment/show?questionId=' + questionId + "&answerId=" + answerId,
        scrollbar: false,
        anim: 5,
    })
}

function changeLikeButtonStatus(answerId, type) {
    if (type == "like") {
        $("#like-" + answerId).css("background-color", "rgba(0, 111, 180, 0.2)")
    } else if (type == "unlike") {
        $("#like-" + answerId).css("background-color", "rgba(0,0,0,0)")
    }
}