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
    layui.form.on('checkbox(is-short)', function (data) {
        if (data.elem.checked) {
            // 是短回答
            $("#answer-editor-full-div").css("display", "none")
            $("#answer-editor-short-div").css("display", "block")
        } else {
            //不是短回答
            $("#answer-editor-full-div").css("display", "block")
            $("#answer-editor-short-div").css("display", "none")
        }
    })
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

// ---------- 点击 发布回答/存草稿 按钮 -------------
$(function () {
    let savingDraftTips, submittingTips
    let content, anonymous, isShort

    function checkValid() {
        isShort = $("#answer-submit-checkbox-is-short")[0].checked ? 1 : 0
        if (isShort == 1) {
            content = layuiUtil.escape($("#answer-editor-short").val())
        } else {
            content = editor.txt.text()
        }
        if (content == "") {
            tips('请输入文字', 1)
            return false
        }
        if (isShort == 0) {
            content = editor.txt.html()
        } else {
            content = layuiUtil.escape($("#answer-editor-short").val())
        }

        anonymous = $("#answer-submit-checkbox-anonymous")[0].checked ? 1 : 0
        return true
    }

    $('#answer-submit-button-submit').click(function () {
        if (!isSubmitting) {
            if (!checkValid()) return

            isSubmitting = true
            submittingTips = layer.msg("发布中", {
                shade: [0.1, '#fff'],
                icon: 16,
                time: 20 * 1000
            })

            $.ajax({
                url: "/answer/",
                type: "post",
                data: JSON.stringify({
                    questionId: parent.questionId,
                    content: content,
                    anonymous: anonymous,
                    isShort: isShort,
                }),
                dataType: "json",
                timeout: 20 * 1000,
                contentType: 'application/json;charset=utf-8',
                success: function (response) {
                    if (response.code == 0) {
                        var data = response.data
                        parent.location.href = "/question/" + data.questionId + "/answer/" + data.answerId
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

    $('#answer-submit-button-draft').click(function () {
        if (!isSavingDraft) {
            if (!checkValid()) return

            isSavingDraft = true
            savingDraftTips = layer.msg("保存中", {
                shade: [0.1, '#fff'],
                icon: 16,
                time: 20 * 1000
            })
            $.ajax({
                url: "/answer/draft/",
                type: "post",
                data: JSON.stringify({
                    questionId: parent.questionId,
                    content: content,
                    anonymous: anonymous,
                    isShort: isShort,
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

// ------------ 初始化 ------------
function init(content, isShort, anonymous) {
    // ------------ 初始化编辑器 ------------
    let uploadingTips

    function uploadError() {
        layer.close(uploadingTips)
        tips('上传失败，请稍后再试', 1)
    }

    var e = window.wangEditor
    editor = new e('#answer-editor-full')
    editor.customConfig.menus = [
        'head', // 标题
        'bold', // 粗体
        'fontSize', // 字号
        'fontName', // 字体
        'italic', // 斜体
        'underline', // 下划线
        'strikeThrough', // 删除线
        'link', // 插入链接
        'emoticon', // 表情
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
        data.append("type", "answer");

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
    e.fullscreen.init('#answer-editor-full', "放大输入框", "缩小输入框")

    if (isShort == undefined || isShort == "")
        isShort = "0"
    if (anonymous == undefined || anonymous == "")
        anonymous = "0"
    $('#answer-submit-checkbox-anonymous').attr("checked", anonymous == "1")
    $('#answer-submit-checkbox-is-short').attr("checked", isShort == "1")
    if (isShort == "1") {
        // 是短回答
        $("#answer-editor-full-div").css("display", "none")
        $("#answer-editor-short-div").css("display", "block")
        $("#answer-editor-short").val(content)
    } else if (isShort == "0") {
        //不是短回答
        $("#answer-editor-full-div").css("display", "block")
        $("#answer-editor-short-div").css("display", "none")
        editor.txt.html(content)
    }
}