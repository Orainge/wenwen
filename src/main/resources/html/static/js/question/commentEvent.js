function loadSpecificQuestionComment(questionCommentId) {
    let data = ""
    data += "?questionId=" + questionId
    data += "&answerId=" + answerId

    layer.open({
        type: 2,
        title: "评论",
        move: false,
        resize: false,
        skin: 'layui-layer-lan',
        area: ['500px', '700px'],
        content: '/questionComment/show' + data,
        scrollbar: false,
        anim: 5,
    })
}

function loadSpecificAnswerComment(answerId, answerCommentId) {
    let data = ""
    data += "?questionId=" + questionId
    data += "&answerId=" + answerId
    data += "&commentId=" + answerCommentId
    console.log(data)
    
    layer.open({
        type: 2,
        title: "评论",
        move: false,
        resize: false,
        skin: 'layui-layer-lan',
        area: ['500px', '700px'],
        content: '/answerComment/show' + data,
        scrollbar: false,
        anim: 5,
    })
}