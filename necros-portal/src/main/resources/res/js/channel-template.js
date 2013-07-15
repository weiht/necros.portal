if (!window.console) {
	window.console = {
		'log': function() {}
	};
}

window.LAYOUT_PATTERNS = {
	'row': '<div class="hc hrow"></div>',
	'col': '<div class="hc hcol"></div>',
	'sec': '<div class="hc hsec"></div>'
};

window.SECTION_PATTERNS = {
	'predefined': '<div class="sec"></div>',
	'ckeditible': '<div class="ckeditible" contenteditable="true"><p></p></div>'
};

window.layoutToolbars = {
	'main': null,
	'row': null,
	'col': null,
	'sec': null
};

// ========Common toolbar visibilities===========//

function hoverEnabled() {
	return $('.open .dropdown-menu').size() == 0;
}

function showToolbar(b) {
	var c = $(b.data('current'));
	b.offset(c.offset()).show();
}

function hideToolbar(b) {
	return function() {
		if (!b.data('visible')) return b;
	};
}

function toolbarMouseOver(tb, div) {
	return function(e) {
		if (!hoverEnabled()) return;
		var b = $(tb);
		b.data('visible', true);
		if (div) {
			b.data('current', $(div));
		}
		showToolbar(b);
		if (e) e.stopPropagation();
	};
}

function toolbarMouseLeave(tb) {
	return function(e) {
		if (!hoverEnabled()) return;
		var b = $(tb);
		b.data('visible', false);
		setTimeout(hideToolbar(b), 1);
		if (e) e.stopPropagation();
	};
}

// ============Appending objects==============//

function doAppendRow(pdiv) {
	return $(LAYOUT_PATTERNS.row).appendTo(pdiv);
}

function doAppendCol(pdiv) {
	return $(LAYOUT_PATTERNS.col).appendTo(pdiv);
}

//=============Container toolbars=============//

function containerAppendRow() {
	doAppendRow('#container').addClass('row-fluid');
}

function doClearContainer() {
	$('#container').empty();
}

function clearContainer() {
	if (confirm('清除后，一旦保存，将不可恢复。')) {
		doClearContainer();
	}
}

function getChannelTemplate() {
	var tpl = $('#container').clone();
	tpl = tpl.find('.ckeditible').each(function() {
		var attrs = this.attributes;
		var me = $(this);
		for (var i = attrs.length - 1; i >= 0; i --) {
			var a = attrs[i].nodeName;
			me.removeAttr(a);
		}
		me.attr('class', 'ckeditible');
	}).end().find('.sec').each(function() {
		var me = $(this);
		var id = me.attr('sectionid');
		if (id) {
			me.html('$sectionEvaluator.evaluate("'
					+ id
					+ '")');
		}
	}).end().html();
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
	var m = $('#main-toolbar');
	layoutToolbars.main = m;
	m.hover(
		toolbarMouseOver(m),
		toolbarMouseLeave(m)
	);
	$('#container').hover(
		toolbarMouseOver(m, '#container'),
		toolbarMouseLeave(m)
	);
}

// =========Row toolbars=========//

function deleteRow() {
	layoutToolbars.row.data('current').remove();
	hideToolbar(layoutToolbars.row.data('visible', false));
}

function colEdited() {
	var ispan = Math.floor(parseFloat($('#spancols').val()));
	var ioffset = Math.floor(parseFloat($('#offsetcols').val()));
	var span, offset;
	if (!isNaN(ispan) && ispan >= 1 && ispan <= 12) {
		span = "span" + ispan;
	} else {
		span = "span" + 12
	}
	if (!isNaN(ioffset) && ioffset > 0 && ioffset < 12) {
		offset = "offset" + ioffset;
	}
	var row = layoutToolbars.row.data('current');
	var col = doAppendCol(row)
		.addClass(span);
	if (offset) col.addClass(offset);
	$('#dlg-col').modal('hide');
}

function fullRowUp() {
	var r = layoutToolbars.row;
	var row = $(r.data('current'));
	var prev = row.prev();
	if (prev.size() > 0) {
		row.remove().insertBefore(prev);
	}
}

function fullRowDown() {
	var r = layoutToolbars.row;
	var row = $(r.data('current'));
	var next = row.next();
	if (next.size() > 0) {
		row.remove().insertAfter(next);
	}
}

function rowUp() {
	console.log('rowUp');
}

function rowDown() {
	console.log('rowDown');
}

function rowLeft() {
	console.log('rowLeft');
}

function rowRight() {
	console.log('rowRight');
}

function initRowToolbar() {
	var r = $('#row-toolbar');
	layoutToolbars.row = r;
	r.hover(
		toolbarMouseOver(r),
		toolbarMouseLeave(r)
	);
	$('#container').delegate('div.hrow', 'mouseenter', function() {
			toolbarMouseOver(r, $(this))();
	}).delegate('div.hrow', 'mouseleave',
			toolbarMouseLeave(r)
	);
}

// ==========Column toolbars=========//

function insertRow() {
	doAppendRow(layoutToolbars.col.data('current'));
}

function deleteCol() {
	$(layoutToolbars.col.data('current')).remove();
	hideToolbar(layoutToolbars.col.data('visible', false));
}

function currentCol() {
	return layoutToolbars.col.data('current');
}

function findSpanClass(clzz) {
	var m = clzz.match(/(^|[\s])(span[\d]{1,2})($|[\s])/);
	return m ? m[2] : null;
}

function parseSpan(cls) {
	return Math.floor(parseFloat(cls.substr(4)));
}

function colPlus() {
	var col = currentCol();
	var cls = findSpanClass(col.attr('class'));
	if (!cls) return;
	var span = parseSpan(cls);
	if (!isNaN(span) && span < 12) {
		span += 1;
		col.removeClass(cls).addClass('span' + span);
	}
}

function colMinus() {
	var col = currentCol();
	var cls = findSpanClass(col.attr('class'));
	if (!cls) return;
	var span = parseSpan(cls);
	if (!isNaN(span) && span > 1) {
		span -= 1;
		col.removeClass(cls).addClass('span' + span);
	}
}

function colLeft(){
	var col = currentCol();
	var prev = col.prev();
	if (prev.size() > 0) {
		col.remove().insertBefore(prev);
	}	
}

function colRight() {
	var col = currentCol();
	var next = col.next();
	if (next.size() > 0) {
		col.remove().insertAfter(next);
	}	
}

function colUp() {
	var col = currentCol();
	var row = $(col.parent('.row'));
	var prev = row.prev();
	if (prev.size() > 0) {
		col.remove().appendTo(prev);
	}
}

function colDown() {
	var col = currentCol();
	var row = $(col.parent('.row'));
	var next = row.next();
	if (next.size() > 0) {
		col.remove().appendTo(next);
	}
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

function initColToolbar() {
	var c = $('#col-toolbar');
	layoutToolbars.col = c;
	c.hover(
		toolbarMouseOver(c),
		toolbarMouseLeave(c)
	);
	$('#container').delegate('div.hcol', 'mouseenter', function() {
			toolbarMouseOver(c, $(this))();
	}).delegate('div.hcol', 'mouseleave',
			toolbarMouseLeave(c)
	);
}

// =========Section toolbars=========//

function currentSec() {
	return layoutToolbars.sec.data('current');
}

function secUp() {
	var sec = currentSec();
	var prev = sec.prev();
	if (prev.size() > 0) {
		sec.remove().insertBefore(prev);
	}
}

function secDown() {
	var sec = currentSec();
	var next = sec.next();
	if (next.size() > 0) {
		sec.remove().insertAfter(next);
	}
}

function initSecToolbar() {
	var s = $('#sec-toolbar');
	layoutToolbars.sec = s;
	s.hover(
			toolbarMouseOver(s), 
			toolbarMouseLeave(s) 
	);
	$('#container').delegate('div.hsec', 'mouseenter', function() {
			toolbarMouseOver(s, this)();
	}).delegate('.hsec', 'mouseleave',
			toolbarMouseLeave(s) 
	);
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

function initInlineEditor() {
	CKEDITOR.disableAutoInline = true;
	$('.ckeditible').each(function() {
		var me = $(this);
		if (me.attr('contenteditable') != 'true') {
			me.attr('contenteditable', true);
		}
		if (!me.html()) {
			me.html('<p></p>');
		}
		CKEDITOR.inline(this);
	});
}

function initPage() {
	initMainToolbar();
	initRowToolbar();
	initColToolbar();
	initSecToolbar();
	initSectionSelector();
	initInlineEditor();
}

$(initPage);