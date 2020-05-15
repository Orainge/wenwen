var userId = $.cookie("userId")
var peopleId, peopleNickname

var nextPage = {
    "question": 1,
    "answer": 1,
    "followQuestion": 1,
    "followUser": 1
}

var loadingTag = {
    "question": false,
    "answer": false,
    "followQuestion": false,
    "followUser": false
}
var contentDiv = {
    "question": $("#people-content-question"),
    "answer": $("#people-content-answer"),
    "followQuestion": $("#people-content-follow-question"),
    "followUser": $("#people-content-follow-user")
}

var firstLoadDiv = {
    "question": $("#first-load-question"),
    "answer": $("#first-load-answer"),
    "followQuestion": $("#first-load-follow-question"),
    "followUser": $("#first-load-follow-user")
}

var nullDataTips = {
    "question": "你暂时还没有提问哦",
    "answer": "你暂时还没有回答问题哦",
    "followQuestion": "你暂时还没有关注问题哦",
    "followUser": "你暂时还没有关注其它用户哦"
}

var firstLoadDivId = {
    "question": "first-load-question",
    "answer": "first-load-answer",
    "followQuestion": "first-load-follow-question",
    "followUser": "first-load-follow-user"
}

var loadMoreButtonId = {
    "question": "load-more-question",
    "answer": "load-more-answer",
    "followQuestion": "load-more-follow-question",
    "followUser": "load-more-follow-user"
}

var loadMoreLoadingId = {
    "question": "load-more-question-loading",
    "answer": "load-more-answer-loading",
    "followQuestion": "load-more-follow-question-loading",
    "followUser": "load-more-follow-user-loading"
}

var mainContentDisplay = [true, false, false]
var followIsDisplay = [true, false]

var loadingBlock = '<img src="/static/img/loading.gif">'

var $mainTab = [$("#people-main-tab-question"), $("#people-main-tab-answer"), $("#people-main-tab-follow")]
var $followTab = [$("#people-main-tab-follow-question"), $("#people-main-tab-follow-user")]
var $mainContent = [contentDiv["question"], contentDiv["answer"], $("#people-content-follow")]
var $followContent = [contentDiv["followQuestion"], contentDiv["followUser"]]

// 定义 Tab 按钮操作
$(function () {
    for (let i = 0; i < $mainTab.length; i++) {
        $mainTab[i].on("click", function () {
            if (!mainContentDisplay[i]) {
                $mainTab[i].css("border-bottom-color", "#006FB4")
                $mainTab[i].css("font-weight", "800")
                $mainContent[i].css("display", "flex")
                mainContentDisplay[i] = true
                for (let j = 0; j < $mainTab.length; j++) {
                    if (i != j) {
                        $mainTab[j].css("border-bottom-color", "white")
                        $mainTab[j].css("font-weight", "unset")
                        $mainContent[j].css("display", "none")
                        mainContentDisplay[j] = false
                    }
                }
                if (i == 0) {
                    getData("question")
                } else if (i == 1) {
                    getData("answer")
                } else if (i == 2) {
                    if (followIsDisplay[0]) {
                        getData("followQuestion")
                    } else if (followIsDisplay[1]) {
                        getData("followUser")
                    }
                }
            }
        })
    }
    for (let i = 0; i < $followTab.length; i++) {
        $followTab[i].on("click", function () {
            if (!followIsDisplay[i]) {
                $followTab[i].css("font-weight", "800")
                $followContent[i].css("display", "flex")
                followIsDisplay[i] = true
                for (let j = 0; j < $followTab.length; j++) {
                    if (i != j) {
                        $followTab[j].css("font-weight", "unset")
                        $followContent[j].css("display", "none")
                        followIsDisplay[j] = false
                    }
                }
                if (i == 0) {
                    getData("followQuestion")
                } else if (i == 1) {
                    getData("followUser")
                }
            }
        })
    }
})

// from: question, answer, followQuestion, followUser
function getData(from) {
    if (!loadingTag[from] && nextPage[from] != -1) {
        loadingTips(from, "start")
        loadingTag[from] = true
        $.ajax({
            url: "/people/get/" + from,
            type: "get",
            data: {
                peopleId: peopleId,
                nextPage: nextPage[from],
            },
            success: function (response) {
                if (response.code == 0) {
                    let end = response.data.end
                    var data = response.data.data
                    loadingTips(from, "stop") // 移除动画
                    processData(from, data) // 根据情况附加按钮
                    let isNullData = nextPage[from] == 1 && (data == undefined || data == null || data.length == 0)
                    appendEnd(from, end, isNullData)
                    if (end == 1) {
                        nextPage[from] = -1
                    } else {
                        nextPage[from] = nextPage[from] + 1
                    }
                    loadingTag[from] = false
                } else {
                    loadingFail(from)
                    loadingTag[from] = false
                }
            },
            error: function () {
                loadingFail(from)
                loadingTag[from] = false
            }
        })
    }
}

// from: question, answer, followQuestion, followUser
function appendEnd(from, end, isNullData) {
    if (end == 1) {
        // 最后一页
        if (isNullData) {
            // 第一页就空了
            contentDiv[from].append('<div class="first-load">' + nullDataTips[from] + '</div>')
        } else {
            contentDiv[from].append('<div class="people-main-content-no-more">没有更多了</div>')
        }
    } else {
        // 不是最后一页
        contentDiv[from].append('<div id="' + loadMoreButtonId[from] + '" class="people-main-content-load-more">加载更多</div>')
        $("#" + loadMoreButtonId[from]).on("click", function () {
            getData(from)
        })
    }
}

function loadingFail(from) {
    // 显示加载失败，重新显示加载更多按钮
    loadingTips(from, "stop")
    // 添加重试按钮
    contentDiv[from].append('<div id="' + loadMoreButtonId[from] + '" class="people-main-content-load-more">重试</div>')
    // 根据情况决定按钮的动作
    if (nextPage[from] == 1) {
        // 重新放入动画
        $("#" + loadMoreButtonId[from]).on("click", function () {
            getData(from)
        })
    } else {
        // 不用重新放入动画
        $("#" + loadMoreButtonId[from]).on("click", function () {
            getData(from)
        })
    }
}

// from: question, answer, followQuestion, followUser
function loadingTips(from, type) {
    if (nextPage[from] == 1) {
        if (type == "start") {
            $("#" + firstLoadDivId[from]).remove()
            $("#" + loadMoreButtonId[from]).remove()
            contentDiv[from].append('<div id="' + firstLoadDivId[from] + '" class="login-more-loading">' + loadingBlock + '</div>')
        } else if (type == "stop") {
            $("#" + firstLoadDivId[from]).remove()
        }
    } else {
        if (type == "start") {
            $("#" + loadMoreButtonId[from]).remove()
            contentDiv[from].append('<div id="' + loadMoreLoadingId[from] + '" class="login-more-loading">' + loadingBlock + '</div>')
        } else if (type == "stop") {
            $("#" + loadMoreLoadingId[from]).remove()
        }
    }
}

// from: question, answer, followQuestion, followUser
function processData(from, data) {
    if (from == "question") {
        for (let i = 0; i < data.length; i++) {
            let s = ""
            s += '<a class="item" href="/question/' + data[i].questionId + '">'
            s += '<span class="title">● ' + data[i].title + '</span>'
            s += '<span>' + data[i].countAnswer + '人回答'
            s += '</a>'
            contentDiv["question"].append(s)
        }
    } else if (from == "answer") {
        for (let i = 0; i < data.length; i++) {
            let s = ""
            s += '<a class="item" href="/question/' + data[i].questionId + '/answer/' + data[i].answerId + '">'
            s += '<span class="title">● 关于 ' + data[i].title + ' 的回答</span>'
            s += '<span>' + data[i].countLike + '人赞同'
            s += '</a>'
            contentDiv["answer"].append(s)
        }
    } else if (from == "followQuestion") {
        for (let i = 0; i < data.length; i++) {
            let s = ""
            s += '<a class="item" href="/question/' + data[i].questionId + '">'
            s += '<span class="title">● ' + data[i].title + '</span>'
            s += '<span>' + data[i].countAnswer + '人回答'
            s += '</a>'
            contentDiv["followQuestion"].append(s)
        }
    } else if (from == "followUser") {
        for (let i = 0; i < data.length; i++) {
            if (data[i].simpleDescription == undefined || data[i].simpleDescription == null || data[i].simpleDescription.length == 0) {
                data[i].simpleDescription = "Ta还没有填写个人简介"
            }
            let s = ""
            s += '<a class="item" href="/people/' + data[i].userId + '">'
            s += '<div class="information-div">'
            s += '<img src="' + data[i].avatarUrl + '">'
            s += '<div class="information">'
            s += '<span class="nickname">' + data[i].nickname + '</span>'
            s += '<span class="simple-description">' + data[i].simpleDescription + '</span>'
            s += '</div>'
            s += '</div>'
            s += '<span>' + data[i].countFollower + ' 人关注</span>'
            s += '</a>'
            contentDiv["followUser"].append(s)
        }
    }
}

$(function () {
    $("#follow-question-info").on("click", function () {
        let i = 2
        if (!mainContentDisplay[i]) {
            $mainTab[i].css("border-bottom-color", "#006FB4")
            $mainTab[i].css("font-weight", "800")
            $mainContent[i].css("display", "flex")
            mainContentDisplay[i] = true
            for (let j = 0; j < $mainTab.length; j++) {
                if (i != j) {
                    $mainTab[j].css("border-bottom-color", "white")
                    $mainTab[j].css("font-weight", "unset")
                    $mainContent[j].css("display", "none")
                    mainContentDisplay[j] = false
                }
            }
            if (followIsDisplay[0]) {
                getData("followQuestion")
            }
        }

        i = 0
        if (!followIsDisplay[i]) {
            $followTab[i].css("font-weight", "800")
            $followContent[i].css("display", "flex")
            followIsDisplay[i] = true
            for (let j = 0; j < $followTab.length; j++) {
                if (i != j) {
                    $followTab[j].css("font-weight", "unset")
                    $followContent[j].css("display", "none")
                    followIsDisplay[j] = false
                }
            }
            getData("followQuestion")
        }
    })
})

// 外部接口，让主页的调用
function redirectTab(redirectTab) {
    if (redirectTab == undefined || redirectTab == null || redirectTab == "")
        redirectTab = "question"
    if (redirectTab == "question") {
        getData("question")
    } else if (redirectTab == "answer") {
        redirect(1)
        getData("answer")
    } else if (redirectTab == "followQuestion") {
        redirect(2)
        getData("followQuestion")
    } else if (redirectTab == "followUser") {
        redirect(2)
        let i = 1
        $followTab[i].css("font-weight", "800")
        $followContent[i].css("display", "flex")
        followIsDisplay[i] = true
        for (let j = 0; j < $followTab.length; j++) {
            if (i != j) {
                $followTab[j].css("font-weight", "unset")
                $followContent[j].css("display", "none")
                followIsDisplay[j] = false
            }
        }
        getData("followUser")
    }
}

function redirect(i) {
    $mainTab[i].css("border-bottom-color", "#006FB4")
    $mainTab[i].css("font-weight", "800")
    $mainContent[i].css("display", "flex")
    mainContentDisplay[i] = true
    for (let j = 0; j < $mainTab.length; j++) {
        if (i != j) {
            $mainTab[j].css("border-bottom-color", "white")
            $mainTab[j].css("font-weight", "unset")
            $mainContent[j].css("display", "none")
            mainContentDisplay[j] = false
        }
    }
}

function checkInfo(pId, pNickname) {
    peopleId = pId
    peopleNickname = pNickname
    var defaultNullString = "暂未填写"
    var tags = ["address", "industry", "career", "education", "full-description"]
    for (var i = 0; i < tags.length; i++) {
        if ($("#people-profile-" + tags[i]).text().length == 0) {
            $("#people-profile-" + tags[i]).text(defaultNullString)
        }
    }
    var $simpleDescription = $("#people-profile-simple-description")
    if ($simpleDescription.text().length == 0) {
        $simpleDescription.text("")
    }
    var $gender = $("#people-profile-gender")
    var genderCode = $gender.text()
    if (genderCode == "0") {
        $gender.text("性别保密")
    } else if (genderCode == "1") {
        $gender.text("男")
    } else if (genderCode == "2") {
        $gender.text("女")
    }
    if ($simpleDescription.text().length == 0) {
        $simpleDescription.text("")
    }
    if (pId == userId) {
        $("#people-profile-edit-button").css("display", "block")
        $("#people-profile-edit-button").on("click", function () {
            window.location.href = "/settings/profile/"
        })
    } else {
        nullDataTips = {
            "question": "Ta暂时还没有提问哦",
            "answer": "Ta暂时还没有回答问题哦",
            "followQuestion": "Ta暂时还没有关注问题哦",
            "followUser": "Ta暂时还没有关注其它用户哦"
        }
        checkIsFollow()
    }
}

var isProcessFollow = false
var isFollow = false
var $followButton = $("#people-profile-follow-button")

function checkIsFollow() {
    $.ajax({
        url: "/follow/people",
        type: "get",
        data: {
            peopleId: peopleId,
        },
        success: function (response) {
            if (response.code == 0) {
                isFollow = response.data.isFollow == 1
                if (isFollow) {
                    $followButton.html("取消关注")
                    $followButton.css("background-color", "rgb(30,159,255)")
                } else {
                    $followButton.html("关注")
                    $followButton.css("background-color", "rgb(0,150,136)")
                }
                $followButton.css("display", "block")
                $followButton.on("click", function () {
                    followUser()
                })
            }
        }
    })
}

function followUser() {
    if (!isProcessFollow) {
        var type, data
        if (isFollow) {
            type = "delete"
            data = JSON.stringify({
                peopleId: peopleId,
            })
        } else {
            type = "post"
            data = JSON.stringify({
                userNickname: localStorage.getItem("nickname"),
                peopleId: parseInt(peopleId),
                peopleNickname: peopleNickname
            })
        }
        isProcessFollow = true
        $.ajax({
            url: "/follow/people",
            type: type,
            data: data,
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (response) {
                if (response.code == 0) {
                    isFollow = !isFollow
                    if (isFollow) {
                        $followButton.html("取消关注")
                        $followButton.css("background-color", "rgb(30,159,255)")
                    } else {
                        $followButton.html("关注")
                        $followButton.css("background-color", "rgb(0,150,136)")
                    }
                    $("#count-follower").html(response.data)
                    isProcessFollow = false
                }
            },
            error: function () {
                isProcessFollow = false
            }
        })
    }
}