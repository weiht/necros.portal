var decodeMap = {
	'&': /&amp;/g,
	'>': /&gt;/g,
	'<': /&lt;/g
};

function getTemplateString() {
	var str = unescape(window.templateEditor.getData());
	str = $('<div></div>').html(str).find('*').each(function() {
		var me = $(this);
		var h = me.html();
		if (h.match(/^[\s]*[#]/)) {
			me.replaceWith('\n' + h + '\n');
		}
	}).end().html();
	var strs = str.split('\n');
	for (var i = 0; i < strs.length; i ++) {
		var s = strs[i];
		if (s.charAt(0) == '#') {
			for (var k in decodeMap) {
				s = s.replace(decodeMap[k], k);
			}
			strs[i] = s;
		}
	}
	return strs.join('\n');
}

function save() {
	if (!$('#section_id').val()) {
		alert('必须填写ID。');
		return;
	}
	window.templateEditor.save();
	var data = $('form').serialize();
	$.post($('form').attr('action'), data, function(ret) {
		if (ret == "OK") {
			alert('保存成功。');
		} else {
			alert(ret);
		}
	});
}

function doRemove(id) {
	$.post('action/del-section', {id: id}, function(ret) {
		if (ret == "OK") {
			location.reload();
		} else {
			alert(ret);
		}
	});
}

function remove(id) {
	if (confirm('一旦删除，将不可恢复。')) {
		doRemove(id);
	}
}

function importSections() {
	var d = $('#importSectionsFile').data('files');
	if (!d || !d.files || !d.files.length) {
		alert('未选中要导入的文件。');
	} else {
		d.submit();
	}
}

$(function() {
	if ($('#importSectionsFile').size() > 0) {
		$('#importSectionsFile').fileupload({
			'add': function(e, d) {
				$('#importSectionsFile').data('files', d);
				var f = d.files[0];
				$('#filelist').html(f && f.name || '');
			},
			'fail': function() {
				alert('导入失败。');
			},
			'done': function(e, d) {
				console.log(d);
				if (d.result && !d.result.error) {
					alert('导入完成。');
					$('#importSectionsFile').removeData('files');
					$('#filelist').empty();
					$('#dlg-import').modal('hide');
				} else {
					alert('导入失败：' + (d.result ? d.result.error : '原因未知。'));
				}
			}
		});
	}
});