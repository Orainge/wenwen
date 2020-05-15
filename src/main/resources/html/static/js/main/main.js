var sidebarStopHeightSet = null // 设置的右边侧边栏停止滚动的高度
var sidebarStopHeight = null // 实际的右边侧边栏停止滚动的高度
var mainDownToNaviHeight = 13 // 主框架到导航栏的高度，css中设置为10

// 滚动条的操作
$(document).scroll(function (e) {
    // 以下为设置右边侧边栏停住滚动
    changeSidebarStopPlace()
})

// 页面渲染完成后的操作
$(function () {
    addCopyrightInSidebar()
})

// 在加入设置 main-up 框架后设置侧边栏停留的高度
function setMainUpStopHeight() {
    if (sidebarStopHeightSet != null) {
        var mainUpHeight = $('#main-up').outerHeight(true)
        if (mainUpHeight != undefined || mainUpHeight != null) {
            sidebarStopHeight = sidebarStopHeightSet + mainUpHeight
        }
    }
}

// 添加网页版权信息
function addCopyrightInSidebar() {
    var mainSidebar = $("#main-sidebar")
    if (mainSidebar != undefined) {
        var copyright = '&copy; All Rights Reserved. <a href="/">问问社区</a> 2020'
        var copyrightDiv = $('<div></div>')
        copyrightDiv.attr('id', 'main-sidebar-copyright')
        copyrightDiv.html(copyright)
        mainSidebar.append(copyrightDiv)
    }
}

(function ($, h, c) {
    var a = $([]),
        e = $.resize = $.extend($.resize, {}),
        i,
        k = "setTimeout",
        j = "resize",
        d = j + "-special-event",
        b = "delay",
        f = "throttleWindow";
    e[b] = 250;
    e[f] = true;
    $.event.special[j] = {
        setup: function () {
            if (!e[f] && this[k]) {
                return false;
            }
            var l = $(this);
            a = a.add(l);
            $.data(this, d, {
                w: l.width(),
                h: l.height()
            });
            if (a.length === 1) {
                g();
            }
        },
        teardown: function () {
            if (!e[f] && this[k]) {
                return false;
            }
            var l = $(this);
            a = a.not(l);
            l.removeData(d);
            if (!a.length) {
                clearTimeout(i);
            }
        },
        add: function (l) {
            if (!e[f] && this[k]) {
                return false;
            }
            var n;

            function m(s, o, p) {
                var q = $(this),
                    r = $.data(this, d);
                r.w = o !== c ? o : q.width();
                r.h = p !== c ? p : q.height();
                n.apply(this, arguments);
            }
            if ($.isFunction(l)) {
                n = l;
                return m;
            } else {
                n = l.handler;
                l.handler = m;
            }
        }
    };

    function g() {
        i = h[k](function () {
                a.each(function () {
                    var n = $(this),
                        m = n.width(),
                        l = n.height(),
                        o = $.data(this, d);
                    if (m !== o.w || l !== o.h) {
                        n.trigger(j, [o.w = m, o.h = l]);
                    }
                });
                g();
            },
            e[b]);
    }
})(jQuery, this);

// 当 main-up 框架改变高度的时候调整高度
// 需要配合上面的插件使用
$("#main-up").resize(function () {
    setMainUpStopHeight()
    changeSidebarStopPlace()
})

// 刷新右边侧栏停留的位置
function changeSidebarStopPlace() {
    if (sidebarStopHeight != null) {
        var scrollTop = $(document).scrollTop();
        if (scrollTop < sidebarStopHeight + mainDownToNaviHeight) {
            $("#main-sidebar").css("margin-top", 0);
        } else {
            $("#main-sidebar").css("margin-top", scrollTop - sidebarStopHeight - mainDownToNaviHeight);
        }
    }
}

// -------------------以下为页面加载时调用的设置-------------------
// 设置右边侧边栏在滚动多高的像素后停住，如果不设置默认为全局滚动，单位px
function setSidebarStopHeight(height) {
    sidebarStopHeightSet = height <= 0 ? height - mainDownToNaviHeight : height
}