var URL = window.URL || window.webkitURL
var $modifyAvatarDiv = $("#modify-avatar-div")
var $modifyAvatarImage = $('#modify-avatar-image')
var $preview = $('#avatar-preview')
var $selectImageButton = $('#upload-image-button')
var $selectImageButtonText = $('#upload-image-button-text')
var $confirmButton = $('#confirm-button')

// 初始化弹窗
layui.use('form', function () {
  var form = layui.form;
  form.render()
})

$modifyAvatarImage.cropper() // 初始化剪裁插件
var options = {
  aspectRatio: 1 / 1,
  preview: '#avatar-preview',
  viewMode: 3,
}

var uploadedImageURL
// 定义 选择图片 按钮操作
$selectImageButton.change(function () {
  if (!$modifyAvatarImage.data('cropper')) return

  var files = this.files
  if (files && files.length) {
    var file = files[0]
    if (uploadedImageURL) {
      URL.revokeObjectURL(uploadedImageURL)
    }
    uploadedImageURL = URL.createObjectURL(file)
    $modifyAvatarImage.cropper('destroy').attr('src', uploadedImageURL).cropper(options)
    $selectImageButton.val('');
    $preview.css("border-color", "#8A8A8A")
    $modifyAvatarDiv.css("display", "flex")
    $confirmButton.css("display", "block")
    $selectImageButtonText.text("重新选择")
  }
})

$(function () {
  // 定义按钮操作
  $("#modify-avatar-zoom-in").on("click", function () {
    $modifyAvatarImage.cropper("zoom", 0.1)
  })
  $("#modify-avatar-zoom-out").on("click", function () {
    $modifyAvatarImage.cropper("zoom", -0.1)
  })
  $("#modify-avatar-move-left").on("click", function () {
    $modifyAvatarImage.cropper("move", -10, 0)
  })
  $("#modify-avatar-move-right").on("click", function () {
    $modifyAvatarImage.cropper("move", 10, 0)
  })
  $("#modify-avatar-move-up").on("click", function () {
    $modifyAvatarImage.cropper("move", 0, -10)
  })
  $("#modify-avatar-move-down").on("click", function () {
    $modifyAvatarImage.cropper("move", 0, 10)
  })
  $("#modify-avatar-rotate-left").on("click", function () {
    $modifyAvatarImage.cropper("rotate", "-90")
  })
  $("#modify-avatar-rotate-right").on("click", function () {
    $modifyAvatarImage.cropper("rotate", "90")
  })
  $("#modify-avatar-reset").on("click", function () {
    $modifyAvatarImage.cropper("reset")
  })
})

// Keyboard
$(document.body).on('keydown', function (e) {
  if (!$modifyAvatarImage.data('cropper')) return
  switch (e.which) {
    case 37:
      e.preventDefault()
      $modifyAvatarImage.cropper('move', -10, 0)
      break

    case 38:
      e.preventDefault()
      $modifyAvatarImage.cropper('move', 0, -10)
      break

    case 39:
      e.preventDefault()
      $modifyAvatarImage.cropper('move', 10, 0);
      break

    case 40:
      e.preventDefault()
      $modifyAvatarImage.cropper('move', 0, 10)
      break
  }
})

// 定义 确认 按钮操作
$confirmButton.on("click", function () {
  // 上传剪裁后的图片
  var imgBase64 = $modifyAvatarImage.cropper("getCroppedCanvas", {
    width: 150,
    height: 150,
  }).toDataURL('image/jpeg')

  tips("start")
  $.ajax({
    url: "/settings/avatar",
    type: "post",
    data: JSON.stringify({
      base64Data: imgBase64
    }),
    dataType: "json",
    contentType: 'application/json;charset=utf-8',
    success: function (response) {
      if (response.code == 0) {
        localStorage.setItem("avatarUrl", response.data.avatarUrl)
        tips("success")
        setTimeout(function () {
          window.location.href = "/settings/avatar"
        }, 2000)
      } else {
        tips("fail", response.message)
      }
    },
    error: function () {
      tips("fail")
    }
  })

})

var tip
function tips(type, message) {
  if (type == "start") {
    tip = layer.msg('保存中', {
      icon: 16,
      time:  20 * 1000,
      shade: [0.3, "#000"]
    })
  } else if (type == "success") {
    layer.close(tip)
    tip = layer.msg('保存成功', {
      icon: 1,
      time: 20000,
      shade: [0.3, "#000"]
    })
  } else if (type == "fail") {
    layer.close(tip)
    if (message == null || message == undefined) {
      message = "保存失败，请稍后再试"
    }
    tip = layer.msg(message, {
      icon: 2,
      time: 2000,
      shade: [0.3, "#000"]
    })
  }
}

// 设置原始头像
$("#avatar-original").attr("src", localStorage.getItem("avatarUrl"))