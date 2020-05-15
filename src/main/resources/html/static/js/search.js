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

var userData = [{
    userId: 1,
    nickname: "用户昵称",
    avatarUrl: "/static/img/icon/avatar.png",
    simpleDescription: "一句话简介",
    countFollow: 5
}, {
    userId: 1,
    nickname: "用户昵称",
    avatarUrl: "/static/img/icon/avatar.png",
    simpleDescription: "一句话简介",
    countFollow: 5
}, {
    userId: 1,
    nickname: "用户昵称",
    avatarUrl: "/static/img/icon/avatar.png",
    simpleDescription: "一句话简介",
    countFollow: 5
}, {
    userId: 1,
    nickname: "用户昵称",
    avatarUrl: "/static/img/icon/avatar.png",
    simpleDescription: "一句话简介",
    countFollow: 5
}, {
    userId: 1,
    nickname: "用户昵称",
    avatarUrl: "/static/img/icon/avatar.png",
    simpleDescription: "一句话简介",
    countFollow: 5
}, ]


var $questionDiv = $("#search-question-div")
var $userDiv = $("#search-user-div")
var $questionTab = $("#main-tab-question")
var $userTab = $("#main-tab-user")
var keyword = ""

var isDisplay = {
    "question": true,
    "user": false
}

var loadingTag = {
    "question": false,
    "user": false
}

var nextPage = {
    "question": 1,
    "user": 1
}
var apiUrl = {
    "question": "/search/question",
    "user": "/search/user"
}

$questionTab.on("click", function () {
    if (!isDisplay["question"]) {
        $questionTab.css("border-color", "rgb(0, 111, 180)")
        $questionTab.css("color", "black")
        $userTab.css("border-color", "white")
        $userTab.css("color", "#8A8A8A")
        $questionDiv.css("display", "flex")
        $userDiv.css("display", "none")
        isDisplay["question"] = true
        isDisplay["user"] = false
        loadInfo("question")
    }
})

$userTab.on("click", function () {
    if (!isDisplay["user"]) {
        $questionTab.css("border-color", "white")
        $questionTab.css("color", "#8A8A8A")
        $userTab.css("border-color", "rgb(0, 111, 180)")
        $userTab.css("color", "black")
        $questionDiv.css("display", "none")
        $userDiv.css("display", "flex")
        isDisplay["question"] = false
        isDisplay["user"] = true
        loadInfo("user")
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
                keyword: keyword,
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
        var loadingDiv = '<div id="loading-' + from + '" class="main-content-loading"><img src = "/static/img/loading_horizontal.gif"></div>'
        $("#search-" + from + "-div").append(loadingDiv)
    } else if (type == "stop") {
        $("#loading-" + from).remove()
    }
}

function loadingFail(from) {
    var buttonDiv = '<div id="load-more-' + from + '-button" class="main-content-load-more">重新加载</div>'
    $("#search-" + from + "-div").append(buttonDiv)
    $("#load-more-" + from + "-button").on("click", function () {
        loadInfo(from)
    })
}

function processData(from, data) {
    if (data == undefined || data == null) {
        return
    }
    if (from == "question") {
        for (let i = 0; i < data.length; i++) {
            var questionId = data[i].questionId
            var title = data[i].title
            var countAnswer = data[i].countAnswer
            addQuestionItem(questionId, title, countAnswer)
        }
    } else if (from == "user") {
        for (let i = 0; i < data.length; i++) {
            var userId = data[i].userId
            var nickname = data[i].nickname
            var avatarUrl = data[i].avatarUrl
            var simpleDescription = data[i].simpleDescription
            var countFollower = data[i].countFollower
            addUserItem(userId, nickname, avatarUrl, simpleDescription, countFollower)
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
                tips = '<div class="search-result-tips">暂时没有相关结果哦</div>'
            } else {
                tips = '<div class="search-result-tips">没有更多了</div>'
            }
        } else {
            var tips = '<div class="search-result-tips">没有更多了</div>'
        }
        $("#search-" + from + "-div").append(tips)
    } else {
        // 显示加载更多
        var buttonDiv = '<div id="load-more-' + from + '-button" class="main-content-load-more">加载更多</div>'
        $("#search-" + from + "-div").append(buttonDiv)
        $("#load-more-" + from + "-button").on("click", function () {
            loadInfo(from)
        })
    }
}

function addQuestionItem(questionId, title, countAnswer) {
    var s = '<a href="/question/' + questionId + '">'
    s += '<div class="item">'
    s += '<div class="title">● ' + title + '</div>'
    s += '<span class="count">' + countAnswer + ' 人回答</span>'
    s += '</div></a>'
    $questionDiv.append(s)
}

function addUserItem(userId, nickname, avatarUrl, simpleDescription, countFollower) {
    var s = '<a href="/people/' + userId + '">'
    s += '<div class="item"><div class="user-info-div">'
    s += '<img src="' + avatarUrl + '">'
    s += '<div class="user-info">'
    s += '<span class="nickname">' + nickname + '</span>'
    s += '<span class="simple-description">' + simpleDescription + '</span>'
    s += '</div></div>'
    s += '<span class="count">' + countFollower + ' 人关注</span>'
    s += '</div></a>'
    $userDiv.append(s)
}


function init(key) {
    keyword = key
    setSearchBoxKeyword(keyword)
    loadInfo("question")
}