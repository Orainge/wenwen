var questionData = [{
    "questionId": 1,
    "title": "Python和Java二选一该学啥？",
    "countAnswer": 7,
}, {
    "questionId": 2,
    "title": "如何理解应用 Java 多线程与并发编程？",
    "countAnswer": 1,
}, {
    "questionId": 3,
    "title": "现在学java能找到工作吗？",
    "countAnswer": 3,
}]

var $viewDiv = $("#index-view-div")
var $answerDiv = $("#index-answer-div")
var $viewTab = $("#index-tab-view")
var $answerTab = $("#index-tab-answer")

var isDisplay = {
    "view": true,
    "answer": false
}

var loadingTag = {
    "view": false,
    "answer": false
}

var nextPage = {
    "view": 1,
    "answer": 1
}
var apiUrl = {
    "view": "/index/topView",
    "answer": "/index/topAnswer"
}

$viewTab.on("click", function () {
    if (!isDisplay["view"]) {
        $viewTab.css("border-color", "rgb(0, 111, 180)")
        $viewTab.css("color", "black")
        $answerTab.css("border-color", "white")
        $answerTab.css("color", "#8A8A8A")
        $viewDiv.css("display", "flex")
        $answerDiv.css("display", "none")
        isDisplay["view"] = true
        isDisplay["answer"] = false
        loadInfo("view")
    }
})

$answerTab.on("click", function () {
    if (!isDisplay["answer"]) {
        $viewTab.css("border-color", "white")
        $viewTab.css("color", "#8A8A8A")
        $answerTab.css("border-color", "rgb(0, 111, 180)")
        $answerTab.css("color", "black")
        $viewDiv.css("display", "none")
        $answerDiv.css("display", "flex")
        isDisplay["view"] = false
        isDisplay["answer"] = true
        loadInfo("answer")
    }
})

function loadInfo(from) {
    if (!loadingTag[from] && nextPage[from] != -1) {
        loadingTag[from] = true
        loadingTips(from, "start")
        $.ajax({
            url: apiUrl[from],
            type: "get",
            data: {
                nextPage: nextPage[from],
            },
            success: function (response) {
                if (response.code == 0) {
                    let end = response.data.end
                    var data = response.data.data
                    loadingTips(from, "stop")
                    processData(from, data)
                    let isNullData = nextPage[from] == 1 && (data == undefined || data == null || data.length == 0)
                    appendEnd(from, end, isNullData)
                    if (end == 1) {
                        nextPage[from] = -1
                    } else {
                        nextPage[from] = nextPage[from] + 1
                    }
                    loadingTag[from] = false
                } else {
                    loadingTips(from, "stop")
                    loadingFail(from)
                    loadingTag[from] = false
                }
            },
            error: function () {
                loadingTips(from, "stop")
                loadingFail(from)
                loadingTag[from] = false
            }
        })
    }
}

function loadingTips(from, type) {
    if (type == "start") {
        $("#load-more-" + from + "-button").remove()
        var loadingDiv = '<div id="loading-' + from + '" class="index-content-loading"><img src = "/static/img/loading_horizontal.gif"></div>'
        $("#index-" + from + "-div").append(loadingDiv)
    } else if (type == "stop") {
        $("#loading-" + from).remove()
    }
}

function loadingFail(from) {
    var buttonDiv = '<div id="load-more-' + from + '-button" class="index-content-load-more">重新加载</div>'
    $("#index-" + from + "-div").append(buttonDiv)
    $("#load-more-" + from + "-button").on("click", function () {
        loadInfo(from)
    })
}

function processData(from, data) {
    if (data == undefined || data == null) {
        return
    }
    if (from == "view") {
        for (let i = 0; i < data.length; i++) {
            var questionId = data[i].questionId
            var title = data[i].title
            var countBrowse = data[i].countBrowse
            addTopView(questionId, title, countBrowse)
        }
    } else if (from == "answer") {
        for (let i = 0; i < data.length; i++) {
            var questionId = data[i].questionId
            var title = data[i].title
            var countAnswer = data[i].countAnswer
            addTopAnswer(questionId, title, countAnswer)
        }
    }
}

function appendEnd(from, end, isNullData) {
    if (end == 1) {
        // 最后一页了
        var tips
        if (nextPage[from] == 1) {
            // 没有数据
            if (isNullData) {
                tips = '<div class="index-result-tips">暂时没有相关结果哦</div>'
            } else {
                tips = '<div class="index-result-tips">没有更多了</div>'
            }
        } else {
            var tips = '<div class="index-result-tips">没有更多了</div>'
        }
        $("#index-" + from + "-div").append(tips)
    } else {
        // 显示加载更多
        var buttonDiv = '<div id="load-more-' + from + '-button" class="index-content-load-more">加载更多</div>'
        $("#index-" + from + "-div").append(buttonDiv)
        $("#load-more-" + from + "-button").on("click", function () {
            loadInfo(from)
        })
    }
}

function addTopView(questionId, title, countBrowse) {
    var s = ' <a href="/question/' + questionId + '"><div class="item">'
    s += '<div class="title">● ' + title + '</div>'
    s += '<span class="count">' + countBrowse + ' 次浏览</span>'
    s += '</div></a>'
    $viewDiv.append(s)
}

function addTopAnswer(questionId, title, countAnswer) {
    var s = ' <a href="/question/' + questionId + '"><div class="item">'
    s += '<div class="title">● ' + title + '</div>'
    s += '<span class="count">' + countAnswer + ' 人回答</span>'
    s += '</div></a>'
    $answerDiv.append(s)
}

// 打开首页调用
$(function () {
    loadInfo("view")
})