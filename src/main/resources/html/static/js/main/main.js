var sidebarStopHeight = null // 右边侧边栏停止滚动的高度
var mainToNaviHeight = 10 // 主框架到导航栏的高度，css中设置为10

// 滚动条的操作
$(document).scroll(function (e) {
    // 以下为设置右边侧边栏停住滚动
    if (sidebarStopHeight != null) {
        var scrollTop = $(document).scrollTop();
        if (scrollTop < sidebarStopHeight + mainToNaviHeight) {
            $("#main-sidebar").css("margin-top", 0);
        } else {
            $("#main-sidebar").css("margin-top", scrollTop - sidebarStopHeight - mainToNaviHeight);
        }
    }
})

// -------------------以下为页面加载时调用的设置-------------------
// 设置右边侧边栏在滚动多高的像素后停住，如果不设置默认为全局滚动，单位px
function setSidebarStopHeight(height) {
    sidebarStopHeight = height <= 0 ? height - mainToNaviHeight : height
}