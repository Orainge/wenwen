var editor
var isSubmitting = false
var isSavingDraft = false
var layuiUtil

function escape2Html(str) {
    var arrEntities = {
        'lt': '<',
        'gt': '>',
        'nbsp': ' ',
        'amp': '&',
        'quot': '"'
    };
    return str.replace(/&(lt|gt|nbsp|amp|quot);/ig, function (all, t) {
        return arrEntities[t];
    });
}

layui.use(['util', 'layer', 'form'], function () {
    layui.form.render()
    layuiUtil = layui.util
})

function tips(message, type, delay) {
    let icon = 6
    if (!delay) {
        delay = 2000
    } else {
        delay = delay * 1000
    }
    if (type == 1) {
        icon = 2
    }
    let m = layer.msg(message, {
        shade: [0.1, '#fff'],
        icon: icon,
        time: delay,
    })
}

// ---------- 点击 发布问题/存草稿 按钮 -------------
$(function () {
    let savingDraftTips, submittingTips
    let topicList
    let title, userId, content, anonymous

    function checkValid() {
        if ($('#ask-question-title').val() == "") {
            tips('请输入问题', 1)
            return false
        }

        topicList = []
        for (let i = 0; i <= 4; i++) {
            let val = $('#ask-question-topic-' + i).val()
            if (val != "")
                topicList.push(layuiUtil.escape(val))
        }

        if (topicList.length == 0) {
            tips('请添加至少一个话题', 1)
            return false
        }
       
        for (let j = 0; j < topicList.length; j++) {
            for (let k = j + 1; k < topicList.length; k++) {
                if (topicList[j] == topicList[k]) {
                    tips('请不要重复添加话题', 1)
                    return false
                }
            }
        }

        title = layuiUtil.escape($('#ask-question-title').val())
        userId = $.cookie("userId")
        content = editor.txt.html()
        anonymous = $("#ask-question-submit-checkbox-anonymous")[0].checked ? 1 : 0

        return true
    }

    $('#ask-question-submit-button-submit').click(function () {
        if (!isSubmitting) {
            if (!checkValid()) return

            isSubmitting = true
            submittingTips = layer.msg("发布中", {
                shade: [0.1, '#fff'],
                icon: 16,
                time: 20 * 1000
            })

            $.ajax({
                url: "/ask/",
                type: "post",
                data: JSON.stringify({
                    userId: userId,
                    title: title,
                    content: content,
                    anonymous: anonymous,
                    topicList: topicList
                }),
                dataType: "json",
                timeout: 20 * 1000,
                contentType: 'application/json;charset=utf-8',
                success: function (response) {
                    if (response.code == 0) {
                        parent.location.href = "/question/" + response.data
                    } else {
                        layer.close(submittingTips)
                        tips("连接错误，请稍后再试", 1)
                        isSubmitting = false
                    }
                },
                error: function () {
                    layer.close(submittingTips)
                    tips("连接错误，请稍后再试", 1)
                    isSubmitting = false
                },
                complete: function () {
                    if (status == 'timeout') {
                        layer.close(submittingTips)
                        tips("连接超时，请稍后再试", 1)
                        isSavingDraft = false
                    }
                }
            })
        }
    })

    $('#ask-question-submit-button-draft').click(function () {
        if (!isSavingDraft) {
            if (!checkValid()) return

            isSavingDraft = true
            savingDraftTips = layer.msg("保存中", {
                shade: [0.1, '#fff'],
                icon: 16,
                time: 20 * 1000
            })

            $.ajax({
                url: "/ask/draft/",
                type: "post",
                data: JSON.stringify({
                    userId: userId,
                    title: title,
                    content: content,
                    anonymous: anonymous,
                    topicList: topicList
                }),
                dataType: "json",
                timeout: 20 * 1000,
                contentType: 'application/json;charset=utf-8',
                success: function (response) {
                    if (response.code == 0) {
                        tips("保存成功")
                        isSavingDraft = false
                    } else {
                        layer.close(savingDraftTips)
                        tips("保存失败，请稍后再试", 1)
                        isSavingDraft = false
                    }
                },
                error: function () {
                    layer.close(savingDraftTips)
                    tips("保存失败，请稍后再试", 1)
                    isSavingDraft = false
                },
                complete: function () {
                    if (status == 'timeout') {
                        layer.close(savingDraftTips)
                        tips("连接超时，请稍后再试", 1)
                        isSavingDraft = false
                    }
                }
            })
        }
    })
})

var topicIsVisible = [false, false, false, false, false]
// ---------------- 动态更改话题输入框 ----------------
$(function () {
    for (let i = 0; i < 4; i++) {
        let t = $("#ask-question-topic-" + i)
        t.bind("input propertychange change", function () {
            if (t.val() == "") {
                // 输入框为空
                for (let j = i + 1; j <= 4; j++) {
                    let tj = $("#ask-question-topic-" + j)
                    if (topicIsVisible[j]) {
                        tj.css("visibility", "hidden")
                        topicIsVisible[j] = false
                        setTimeout(function () {
                            tj.val("")
                        }, 330)
                    }
                }
            } else {
                if (!topicIsVisible[i + 1]) {
                    $("#ask-question-topic-" + (i + 1)).css("visibility", "visible")
                    topicIsVisible[i + 1] = true
                }
            }
        })
    }
})

// ------------ 初始化 ------------
function init(title, content, anonymous, topicList) {
    // ------------ 初始化编辑器 ------------
    let uploadingTips

    function uploadError() {
        layer.close(uploadingTips)
        tips('上传失败，请稍后再试', 1)
    }

    var e = window.wangEditor
    editor = new e('#ask-question-editor')
    editor.customConfig.menus = [
        'bold', // 粗体
        'italic', // 斜体
        'image', // 插入图片
        'undo', // 撤销
        'redo' // 重复
    ]
    editor.customConfig.showLinkImg = false // 隐藏网络图片TAB 
    editor.customConfig.customUploadImg = function (files, insert) {
        let data = new FormData();
        for (let i = 0; i < files.length; i++) {
            data.append("files", files[i]);
        }
        data.append("type", "question");

        uploadingTips = layer.msg('上传中', {
            shade: [0.1, '#fff'],
            icon: 16,
            time: 20 * 1000
        })
        $.ajax({
            url: "/imgUpload",
            type: "POST",
            data: data,
            timeout: 20 * 1000,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                if (response.code == 0) {
                    for (let j = 0; j < response.data.length; j++) {
                        insert(response.data[j]);
                    }
                    layer.close(uploadingTips)
                } else {
                    uploadError()
                }
            },
            error: function () {
                uploadError()
            },
            complete: function () {
                if (status == 'timeout') {
                    uploadError()
                }
            }
        })
    }
    editor.create()
    e.fullscreen.init('#ask-question-editor', "放大输入框", "缩小输入框")

    if (title) {
        if (anonymous == 1) {
            $('#ask-question-submit-checkbox-anonymous').attr("checked", true)
        }
        $('#ask-question-title').val(escape2Html(title))
        editor.txt.html(content)
        topicList = $.parseJSON(topicList.substring(1, topicList.length - 1))
        let i = 0;
        for (; i < topicList.length; i++) {
            $('#ask-question-topic-' + i).val(escape2Html(topicList[i]))
            topicIsVisible[i] = true
            $('#ask-question-topic-' + i).css("visibility", "visible")
        }
        if (i != 5) {
            topicIsVisible[i] = true
            $('#ask-question-topic-' + i).css("visibility", "visible")
        }
    }
}