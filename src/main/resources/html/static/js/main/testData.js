var test_time = {
    "feed": 0,
    "message": 0,
    "like": 0,
    max: 3,
}

function testData(from) {
    if (from == "feed") {
        var data = {
            data: [{
                type: 0,
                parm: [1, "用户1", 1, "问题1"],
            }, {
                type: 1,
                parm: [2, "用户2", 2, "问题2222222222222222222222222222222", 4546],
            }, {
                type: 2,
                parm: [3, "用户3", 3, "问题3"],
            }, {
                type: 3,
                parm: [4, "用户4", 4, "问题4"],
            }, {
                type: 1,
                parm: [4, "用户4", 5, "问题5"],
            }, {
                type: 2,
                parm: [4, "用户4", 6, "问题6"],
            }, ],
            end: testGetTime("feed")
        }
        return data
    } else if (from == "message") {
        var data = {
            data: [{
                type: 0,
                s_type: 0,
                parm: [1, "用户1"],
            }, {
                type: 0,
                s_type: 1,
                parm: [1, "用户1", 1, "收藏夹1"],
            }, {
                type: 0,
                s_type: 2,
                parm: [1, "用户1", 1, "问题1"],
            }, {
                type: 1,
                s_type: 0,
                parm: [1, "用户1", 1, "问题1", 1],
            }, {
                type: 1,
                s_type: 1,
                parm: [1, "用户1", 1, "问题1", 1],
            }, {
                type: 1,
                s_type: 2,
                parm: [1, "用户1", 1, "问题1", 1],
            }, {
                type: 1,
                s_type: 3,
                parm: [1, "用户1", 1, "问题1", 2],
            }, {
                type: 2,
                s_type: 0,
                parm: [1, "用户1", 1, "问题1", 1],
            }],
            end: testGetTime("message")
        }
        return data
    } else if (from == "like") {
        var data = {
            data: [{
                type: 0,
                parm: [1, "用户1", 1, "问题1"],
            }, {
                type: 1,
                parm: [1, "用户1", 1, "问题1", 1],
            }, {
                type: 2,
                parm: [1, "用户1", 1, "问题1", 2],
            }, {
                type: 3,
                parm: [1, "用户1", 1, "问题1", 3],
            }, ],
            end: testGetTime("like")
        }
        return data
    }
    return null
}

function testGetTime(from) {
    ++test_time[from]
    if (test_time[from] != test_time.max) {
        return 0
    } else
        return 1
}

// testLoading(from, isScroll)
function testLoading(from, isScroll) {
    var times = 1
    n_loadingTag[from] = true
    setTimeout(function () {
        var response_data = testData(from)
        var data = response_data.data
        if (isScroll)
            $("#success-loading-" + from).remove()
        if (from == "feed") {
            pFeedData(data)
        } else if (from == "message") {
            pMessageData(data)
        } else if (from == "like") {
            pLikeData(data)
        }
        updateTotalHeight(from)
        if (response_data.end == 1) {
            n_nextPage[from] = -1
        } else {
            n_nextPage[from] = n_nextPage[from] + 1
        }
        n_loadingTag[from] = false

    }, times * 1000)
}