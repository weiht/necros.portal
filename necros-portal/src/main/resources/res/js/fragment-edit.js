function save() {
	if (!$('#fragment_id').val()) {
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
	$.post('action/del-fragment', {id: id}, function(ret) {
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

function importFragments() {
	var d = $('#importFile').data('files');
	if (!d || !d.files || !d.files.length) {
		alert('未选中要导入的文件。');
	} else {
		d.submit();
	}
}

$(function() {
	if ($('#importFile').size() > 0) {
		$('#importFile').fileupload({
			'add': function(e, d) {
				$('#importFile').data('files', d);
				var f = d.files[0];
				$('#filelist').html(f && f.name || '');
			},
			'fail': function() {
				alert('导入失败。');
			},
			'done': function(e, d) {
				if (d.result && !d.result.error) {
					alert('导入完成。');
					location.reload();
				} else {
					alert('导入失败：' + (d.result ? d.result.error : '原因未知。'));
				}
			}
		});
	}
});