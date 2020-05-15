/**
 * 
 */
window.wangEditor.fullscreen = {
	// editor create之后调用
	zoonInTips: "全屏",
	zoonOutTips: "退出全屏",
	init: function (editorSelector, zoonInTips, zoonOutTips) {
		if (zoonInTips) {
			this.zoonInTips = zoonInTips
		}
		if (zoonOutTips) {
			this.zoonOutTips = zoonOutTips
		}
		$(editorSelector + " .w-e-toolbar").append('<div class="w-e-menu"><a class="_wangEditor_btn_fullscreen" href="###" onclick="window.wangEditor.fullscreen.toggleFullscreen(\'' + editorSelector + '\')">' + this.zoonInTips + '</a></div>');
	},
	toggleFullscreen: function (editorSelector) {
		$(editorSelector).toggleClass('fullscreen-editor');
		if ($(editorSelector + ' ._wangEditor_btn_fullscreen').text() == this.zoonInTips) {
			$(editorSelector + ' ._wangEditor_btn_fullscreen').text(this.zoonOutTips);
		} else {
			$(editorSelector + ' ._wangEditor_btn_fullscreen').text(this.zoonInTips);
		}
	}
};