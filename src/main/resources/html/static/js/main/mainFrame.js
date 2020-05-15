// 如果不指定额外添加的 script 和 css，则只加载默认框架
// 否则需要显式定义 window.onload = function(){sourceLoad(callback_func, ex_script, ex_css)}
// callback_func: 加载完成后需要执行的函数, ex_script: 数组, 额外添加的js, ex_css: 数组, 额外添加的css
window.onload = function () {
    sourceLoad() //只能这样写，否则会引起错误
}

function sourceLoad(callback_func, ex_script, ex_css) {
    var s = new SourceLoad();
    s.load(callback_func, ex_script, ex_css)
}

function SourceLoad() {
    var callback = null
    var head = document.getElementsByTagName("head")[0]

    var meta = [
        ["http-equiv", "X-UA-Compatible"],
        ["content", "IE=edge"],
        ["name", "viewport"],
        ["content", "width=device-width, initial-scale=1"]
    ]

    var css = [
        "/static/layui/css/layui.css",
        "/static/css/main/navi.css",
        "/static/css/main/notification.css",
        "/static/css/main/main.css"
    ]

    var script = [
        "/static/js/util/jquery.min.js",
        "/static/js/util/jquery.cookie.min.js",
        "/static/layui/layui.js",
        "/static/js/util/preload.js"
    ]

    var index = {
        meta: 0,
        css: 0,
        script: 0

    }

    this.load = function (callback_func, ex_script, ex_css) {
        document.body.style.display = "none"
        if (ex_script) {
            script = script.concat(ex_script)
        }
        if (ex_css) {
            css = css.concat(ex_css)
        }
        if (callback_func === undefined || callback_func === null) {
            callback = function () {}
        } else {
            callback = callback_func
        }
        loadMeta()
        loadCSS()
    }

    function loadMeta() {
        var metaItem = document.createElement("meta");
        for (var i = 0; i < meta.length; i++) {
            metaItem.setAttribute(meta[i][0], meta[i][1]);
        }
        head.appendChild(metaItem);
    }

    function loadCSS() {
        var temp = document.createElement("link")
        temp.setAttribute("rel", "stylesheet");
        temp.setAttribute("media", "all");
        temp.href = css[index.css]
        temp.onload = function () {
            index.css += 1
            if (index.css === css.length) {
                loadScript()
            } else {
                loadCSS()
            }
        }
        head.appendChild(temp);
    }

    function loadScript() {
        var temp = document.createElement("script")
        temp.setAttribute("type", "text/javascript")
        temp.setAttribute("charset", "utf-8")
        temp.src = script[index.script]
        temp.onload = function () {
            index.script += 1
            if (index.script === script.length) {
                loadBody()
            } else {
                loadScript()
            }
        }
        head.appendChild(temp);
    }

    function loadBody() {
        var navi_body = '<div id="navi-div"><div class="navi"><div class="navi-title"><a href="/">问问社区</a></div><div name="navi-separator" style="width: 20px;"></div><div class="navi-tab"><ul><a href="/"><li id="navi-tab-index">首页</li></a></ul></div><div name="navi-separator" style="width: 60px;"></div><div class="navi-search-box"><input id="navi-search-input" placeholder="搜索你的疑问..."><div id="navi-search-button"></div></div><div name="navi-separator" style="width: 10px;"></div><button id="navi-ask-button">提问</button><div name="navi-separator" style="width: 170px;"></div><div name="navi-separator" style="width: 10px;"></div><div id="navi-notification-button"></div><div class="navi-separator" style="width: 20px;"></div><img id="navi-user-avatar" style="width: 33px; height: 33px;" src="/static/img/icon/avatar.png"></div><div id="navi-notification-pop"><div id="navi-notification-box-div"><div id="navi-notification-box"><div id="navi-notification-box-triangle1"></div><div id="navi-notification-box-triangle2"></div><div class="navi-notification-box-tab"><div id="navi-notification-box-tab-feed" class="navi-notification-box-tab-icon"></div><div class="navi-notification-box-tab-separator"></div><div id="navi-notification-box-tab-message" class="navi-notification-box-tab-icon"></div><div class="navi-notification-box-tab-separator"></div><div id="navi-notification-box-tab-like" class="navi-notification-box-tab-icon"></div></div><div id="navi-notification-box-main"><div id="navi-notification-box-main-feed" class="navi-notification-box-main-list"><div id="navi-notification-box-loading-feed" class="navi-notification-box-loading"><img src="/static/img/loading.gif"><p>加载通知中</p></div><div id="navi-notification-box-loading-fail-feed" class="navi-notification-box-loading-fail"><img src="/static/img/error.png"><p>加载失败，请稍后再试</p></div><div id="navi-notification-box-success-feed" class="navi-notification-box-success"></div></div><div id="navi-notification-box-main-message" class="navi-notification-box-main-list"><div id="navi-notification-box-loading-message" class="navi-notification-box-loading"><img src="/static/img/loading.gif"><p>加载通知中</p></div><div id="navi-notification-box-loading-fail-message" class="navi-notification-box-loading-fail"><img src="/static/img/error.png"><p>加载失败，请稍后再试</p></div><div id="navi-notification-box-success-message" class="navi-notification-box-success"></div></div><div id="navi-notification-box-main-like" class="navi-notification-box-main-list"><div id="navi-notification-box-loading-like" class="navi-notification-box-loading"><img src="/static/img/loading.gif"><p>加载通知中</p></div><div id="navi-notification-box-loading-fail-like" class="navi-notification-box-loading-fail"><img src="/static/img/error.png"><p>加载失败，请稍后再试</p></div><div id="navi-notification-box-success-like" class="navi-notification-box-success"></div></div></div><div id="navi-notification-box-footer"></div></div></div></div><div id="navi-user-menu-pop"><div id="navi-user-menu-box-div"><div id="navi-user-menu-box"><div id="navi-user-menu-box-triangle1"></div><div id="navi-user-menu-box-triangle2"></div><div class="navi-user-menu-box-item-div"><ul><a href="/people"><li><div id="navi-user-menu-box-item-profile" class="navi-user-menu-box-item-icon"></div>我的主页</li></a><a href="/settings"><li><div id="navi-user-menu-box-item-settings" class="navi-user-menu-box-item-icon"></div>设置</li></a><a href="/logout"><li><div id="navi-user-menu-box-item-exit" class="navi-user-menu-box-item-icon"></div>退出</li></a></ul></div></div></div></div></div><div id="navi-bottom-line"></div><script type="text/javascript" src="/static/js/main/navi.js" charset="utf-8"></script><script type="text/javascript" src="/static/js/main/notification.js" charset="utf-8"></script><script type="text/javascript" src="/static/js/main/main.js" charset="utf-8"></script>'
        $(document.body).prepend(navi_body)
        loadFinish()
    }

    function loadFinish() {
        callback()
        document.body.style.display = "flex"
    }
}