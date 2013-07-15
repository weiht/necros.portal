function save() {
	if (!$('#channel_id').val()) {
		alert('必须填写ID。');
		return;
	}
	var data = $('form').serialize();
	$.post($('form').attr('action'), data, function(ret) {
		if (ret == "OK") {
			location.href = "index";
		} else {
			alert(ret);
		}
	});
}

function doRemove(id) {
	$.post('action/del-channel', {id: id}, function(ret) {
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

function generateChannel(id) {
	$.post('action/generate-channel', {
		id: id
	}, function(ret) {
		if (ret == "OK") {
			alert('生成页面成功。');
		} else {
			alert(ret);
		}
	});
}

function generateAllChannels() {
	$.post('action/generate-all-channels', {
	}, function(ret) {
		if (ret == "OK") {
			alert('生成页面成功。');
		} else {
			alert(ret);
		}
	});
}

function importChannels() {
	var d = $('#importChannelsFile').data('files');
	if (!d || !d.files || !d.files.length) {
		alert('未选中要导入的文件。');
	} else {
		d.submit();
	}
}

$(function() {
	if ($('#importChannelsFile').size() > 0) {
		$('#importChannelsFile').fileupload({
			'add': function(e, d) {
				$('#importChannelsFile').data('files', d);
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
					$('#importChannelsFile').removeData('files');
					$('#filelist').empty();
					$('#dlg-import').modal('hide');
				} else {
					alert('导入失败：' + (d.result ? d.result.error : '原因未知。'));
				}
			}
		});
	}
});