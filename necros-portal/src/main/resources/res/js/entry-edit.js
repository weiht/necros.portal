var decodeMap = {
	'&': /&amp;/g,
	'>': /&gt;/g,
	'<': /&lt;/g
};

function save() {
	if (!$('#param_id').val()) {
		alert('必须填写键。');
		return;
	}
	var data = $('form').serialize();
	$.post($('form').attr('action') + '?action=' + $('#action').val()
		+ '&cid=' + $('#cid').val(), data, function(ret) {
		if (ret == "OK") {
			alert('保存成功。');
			location.href = "entries?cid=" + $('#cid').val();
		} else {
			alert(ret);
		}
	});
}

function doRemove(id) {
	$.post('action/del-entry', {id: id, cid: $('#cid').val()}, function(ret) {
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
	var d = $('#importSysParamsFile').data('files');
	if (!d || !d.files || !d.files.length) {
		alert('未选中要导入的文件。');
	} else {
		d.submit();
	}
}

$(function() {
	if ($('#importSysParamsFile').size() > 0) {
		$('#importSysParamsFile').fileupload({
			'add': function(e, d) {
				$('#importSysParamsFile').data('files', d);
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
					$('#importSysParamsFile').removeData('files');
					$('#filelist').empty();
					$('#dlg-import').modal('hide');
				} else {
					alert('导入失败：' + (d.result ? d.result.error : '原因未知。'));
				}
			}
		});
	}
});