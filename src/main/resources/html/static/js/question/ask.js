/* 判断字符串是否为空 */
function isNull(val) {
    var str = val.replace(/(^\s*)|(\s*$)/g, ''); //去除空格;
    return (str == '' || str == undefined || str == null)
}

function search() {
    var content = $('#search-input').val()
    if (isNull(content)) {
        return
    }
    window.location.href = "/search?content=" + content
}

$('#search-button').on("click", search)
$('#search-input').keypress(function (event) {
    if (event.which === 13) {
        search();
    }
})

$('#ask-button').on("click", function () {
    $('#ask-box').
})