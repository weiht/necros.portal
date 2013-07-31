if (!window.console) {
	window.console = {
		'log': function() {}
	};
}

function getChannelTemplate() {
	window.templateEditor.save();
	var tpl = unescape($('#template').val());
	return tpl;
}

function doSaveChannel() {
	var tpl = getChannelTemplate();
	$.post('action/channel-template-save', {
		id: $('#container').attr('channelid'),
		template: tpl
		}, function(ret) {
			if (ret == "OK") {
				alert('保存成功。');
			} else {
				alert(ret);
			}
		});
}

function doGenerateChannel() {
	$.post('action/generate-channel', {
		id: $('#container').attr('channelid')
	}, function(ret) {
		if (ret == "OK") {
			alert('生成页面成功。');
		} else {
			alert(ret);
		}
	});
}

function saveChannel() {
	if (confirm('一旦保存，将不可恢复。')) {
		doSaveChannel();
	}
}

function previewChannel() {
	var tpl = getChannelTemplate();
	$('#previewTemplate').val(tpl);
	$('#previewForm').submit();
}

function generateChannel() {
	if (confirm("将页面生成为文件，未保存的部分不生效。")) {
		doGenerateChannel();
	}
}

function initMainToolbar() {
}

function sectionSelected() {
	var sec = $('#selsection').val();
	var dsec = $(LAYOUT_PATTERNS.sec).appendTo($(layoutToolbars.col.data('current')));
	if (sec) {
		$(SECTION_PATTERNS.predefined)
			.attr('sectionid', sec)
			.append($('#' + sec).find('.template').text())
			.appendTo(dsec);
	} else {
		dsec.html(SECTION_PATTERNS.ckeditible);
	}
	dsec.find('.ckeditible').each(function() {
		CKEDITOR.inline(this);
	});
	$('#dlg-select-section').modal('hide');
}

function initSectionSelector() {
	var sel = $('#selsection');
	$('#section-list>div').each(function() {
		var me = $(this);
		sel.append('<option value="' + me.attr('id') + '">'
				+ me.find('.title').html()
				+ '</option>');
	});
}

function initPage() {
	initMainToolbar();
	initSectionSelector();
}

$(initPage);