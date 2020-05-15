var questionId = null
var contentOriginalHeight = null // content 原来的高度
var contentShowMore = null // 是否在显示更多，null表示不使用 显示更多 功能
var userId = $.cookie("userId")
var questionTitle = null

// 初始化弹窗
layui.use('form', function () {
    var form = layui.form;
    form.render()
})

function setQuestion(question_id, title, content, topic_list) {
    // content = "<p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p>"
    // content = "<p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p><p>ddddd</p>"
    questionId = question_id
    title = title.substring(1, title.length - 1)
    content = content.substring(1, content.length - 1)
    $("#question-title").html(title)
    questionTitle = title
    $("#question-content").html(content)
    document.title = "问问社区 | " + title
    var topicList = $.parseJSON(topic_list.substring(1, topic_list.length - 1))
    // var topicList = [{
    //         topic_id: "话题ID1",
    //         topic_name: "话题标题1"
    //     },
    //     {
    //         topic_id: "话题ID2",
    //         topic_name: "话题标题2"
    //     }
    // ]
    var topicListUl = $('#question-topic-list')
    for (var i = 0; i < topicList.length; i++) {
        let item = topicList[i]
        let liDiv = $('<li></li>')
        liDiv.append(item.topic_name)
        topicListUl.append(liDiv)
    }
}

function setProfile(anonymous, user_id, username, avatar_url, simple_description) {
    if (anonymous == 1 && user_id == "") {
        $('#question-user-profile-simple-description').remove()
        $('#question-user-profile-username').remove()
        $('#question-user-profile-username-div').append("匿名")
    } else {
        user_id = user_id.substring(1, user_id.length - 1)
        username = username.substring(1, username.length - 1)
        avatar_url = avatar_url.substring(1, avatar_url.length - 1)
        if (simple_description != "") {
            simple_description = simple_description.substring(1, simple_description.length - 1)
        }
        $('#question-user-profile-avatar').attr("src", avatar_url)
        $('#question-user-profile-username').attr("href", "/people/" + user_id)
        $('#question-user-profile-username').html(username)
        if (simple_description == "") {
            $('#question-user-profile-simple-description').remove()
        } else {
            $('#question-user-profile-simple-description').html(simple_description)
        }
    }
}

function setInfo(create_time) {
    create_time = parseInt(create_time)
    var createDate = new Date(create_time)
    var date = createDate.getFullYear() + " 年 " + (createDate.getMonth() + 1) + " 月 " + createDate.getDate() + " 日"
    $('#question-create-time').append(date)
}

function setOwner(user_id) {
    if (user_id == userId) {
        $("#question-button-answer").css("display", "none")
        $("#question-edit").css("display", "flex")
        $("#question-button-follow").css("display", "none")
        $("#question-button-delete").on("click", function () {
            var confirmTips = layer.confirm('你确定要删除这个问题吗？', {
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
                deleteQuestion()
            })
        })
        $("#question-button-edit").on("click", function () {
            // TODO 弹窗编辑问题对话框（需要后台验证当前用户ID
            layer.open({
                type: 2,
                title: "编辑问题",
                move: false,
                resize: false,
                area: ['700px', '500px'],
                content: '/question/edit?questionId=' + questionId,
            })
        })
    } else {
        initFollowButton()
    }
}

function prepareShowMoreButton() {
    $("#question-content").resize(function () {
        if (contentShowMore == null) {
            var contentDiv = $('#question-content')
            var contentHeight = contentDiv.outerHeight(true)
            if (contentHeight > 100) {
                contentOriginalHeight = contentHeight
                contentShowMore = false
                contentDiv.css("max-height", "100px")
                contentDiv.css("height", "100px")
                // 按下 显示更多 按钮时执行的功能
                var buttonDiv = $("#question-button-more")
                buttonDiv.css("display", "flex")
                var buttonHtml = '<div id="question-button-more-tips" style="margin-right:5px;">显示更多</div><img id="question-button-more-tips-img" src="/static/img/icon/question_show_more.png" />'
                buttonDiv.append(buttonHtml)
                buttonDiv.on("click", function () {
                    if (contentShowMore == null) return
                    var contentDiv = $('#question-content')
                    var tipsDiv = $('#question-button-more-tips')
                    var tipsImg = $('#question-button-more-tips-img')
                    if (contentShowMore) {
                        // 点击显示更多
                        contentDiv.css("max-height", "100px")
                        contentDiv.css("height", "100px")
                        tipsDiv.html("显示更多")
                        tipsImg.attr("src", "/static/img/icon/question_show_more.png")
                        contentShowMore = false
                    } else {
                        // 点击收起
                        contentDiv.css("max-height", "")
                        contentDiv.css("height", "")
                        tipsDiv.html("收起")
                        tipsImg.attr("src", "/static/img/icon/question_hide_more.png")
                        contentShowMore = true
                    }
                })
            }
        }
    })
}

function deleteQuestion() {
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
        url: "/question/" + questionId,
        type: "delete",
        data: JSON.stringify({
            question_id: questionId
        }),
        dataType: "json",
        timeout: 20 * 1000,
        contentType: 'application/json;charset=utf-8',
        success: function (response) {
            if (response.code == 0) {
                layer.close(tips)
                window.location.href = "/"
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

$(function () {
    $("#answer-button").on("click", function () {
        layer.open({
            type: 2,
            title: "回答 - " + questionTitle,
            move: false,
            resize: false,
            skin: 'layui-layer-lan',
            area: ['700px', '500px'],
            content: '/answer?questionId=' + questionId,
            scrollbar: false,
            anim: 5,
        })
    })
})