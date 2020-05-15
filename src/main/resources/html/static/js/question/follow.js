var isFollow = false
var $followButton = $("#question-button-follow")
var $followButtonTips = $("#question-button-follow-tips")

function initFollowButton() {
    $.ajax({
        url: "/follow/question/",
        type: "get",
        data: {
            questionId: questionId,
        },
        success: function (response) {
            if (response.code == 0) {
                isFollow = response.data.isFollow == 1
                changeFollowButtonStatus(isFollow)
                $followButton.on("click", function () {
                    pressFollow()
                })
            }
        }
    })
}

var isProcessing = false

function pressFollow() {
    if (!isProcessing) {
        isProcessing = true

        var data = {
            questionId: parseInt(questionId),
        }
        var type
        if (isFollow) {
            type = "delete"
        } else {
            type = "post"
        }
        $.ajax({
            url: "/follow/question",
            type: type,
            data: JSON.stringify(data),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (response) {
                if (response.code == 0) {
                    if (isFollow) {
                        $("#question-count-follow").html(response.data)
                    } else {
                        $("#question-count-follow").html(response.data)
                    }
                    changeFollowButtonStatus(!isFollow)
                    isProcessing = false
                }
            },
            error: function () {
                isProcessing = false
            }
        })
    }
}

function changeFollowButtonStatus(nowIsFollow) {
    isFollow = nowIsFollow
    if (isFollow) {
        $followButtonTips.html("取消关注")
        $followButton.css("background-color", "rgb(246,246,246)")
    } else {
        $followButtonTips.html("关注")
        $followButton.css("background-color", "rgba(0, 0, 0, 0)")
    }
}