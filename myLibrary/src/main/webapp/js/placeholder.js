/**
 * @Author Shiming
 * @time 2012-12-21 10:45:49
 */
(function(){
var EventUtil = {
	addHandler: function(element, type, handler){
		if (element.addEventListener){
			element.addEventListener(type, handler, false);
		} else if (element.attachEvent){
			element.attachEvent("on" + type, handler);
		} else {
			element["on" + type] = handler;
		}
	},
	getCharCode: function(event){
		if (typeof event.charCode === "number"){
			return event.charCode;
		} else {
			return event.keyCode;
		}
	},
	getEvent: function(event){
		return event ? event : window.event;
	},
	getTarget: function(event){
		return event.target || event.srcElement;
	}
};
EventUtil.addHandler(document, 'readystatechange', function(){
	if (!('placeholder' in document.createElement('input'))) {
		var inputdoc = document.getElementsByTagName('input');
		var hasPlaceholder = new Array();
		for (var i in inputdoc) {
			if (typeof(inputdoc[i]) === 'object' && inputdoc[i].getAttribute('placeholder') != null) {
				hasPlaceholder.push(inputdoc[i]);
			}
		}
		if (hasPlaceholder.length > 0) {
			var holders = new Array;
			for (var i = 0; i < hasPlaceholder.length; i++) {
				var span = document.createElement('span');
				// span.className = 'ui-placeholder';
				var input = hasPlaceholder[i];
				span.height = input.height;
				span.width = input.width;
				span.innerHTML = input.getAttribute('placeholder');
				if (input === input.parentNode.lastChild) {
					input.parentNode.appendChild(span);
				} else {
					input.parentNode.insertBefore(span, input.nextSibling);
				}
				span.style.color = "#999";
				span.style.textIndent = '0.5em';
				span.style.position = "absolute";
				span.style.top = input.offsetTop;
				span.style.left = input.offsetLeft;
				holders[i] = span;
			}
			var togglePlaceholder = function(event) {
				event = EventUtil.getEvent(event);
				var target = EventUtil.getTarget(event);
				// alert(target.value)
				for( var i in hasPlaceholder) {
					if (target === hasPlaceholder[i]) {
						if (hasPlaceholder[i].value != '') {
							holders[i].style.display = 'none';
						} else if ( /[a-zA-Z0-9`~!@#\$%\^&\*\(\)_+-=\[\]\{\};:'"\|\\,.\/\?<>]/.test(String.fromCharCode(EventUtil.getCharCode(event))) ) {
							
							holders[i].style.display = 'none';
						} else {
							holders[i].style.display = 'block';
						}
					}
				}
			}
			
			EventUtil.addHandler(document, 'keydown', togglePlaceholder)

			EventUtil.addHandler(document, 'keyup', function(event) {
				event = EventUtil.getEvent(event);
				var target = EventUtil.getTarget(event);
				// alert(target.value)
				for( var i in hasPlaceholder) {
					if (target === hasPlaceholder[i]) {
						if (hasPlaceholder[i].value != '') {
							holders[i].style.display = 'none';
						} else {
							holders[i].style.display = 'block';
						}
					}
				}
			})
		}
	}
})
}).call();



//for placeholder in ie;
if ($.browser.msie) {
//the text input that has placeholder attr
var inputText = $(":text[placeholder]");

inputText.each(function(i) {
var placeholder = $(this).attr("placeholder");

//if value is blank
if($(this).val(placeholder) == ''){
//then set value to placeholder
$(this).val(placeholder);
}
//when focus in
$(this).focus(function() {
//if value equals placeholder
if ($(this).val() == placeholder) {
//then set value to blank
$(this).val("");
}
});

//when focus out
$(this).focusout(function() {
//if value is blank
if ('' == $(this).val()) {
//then set value to placeholder;
$(this).val(placeholder)
}
});
});

//before submit;
$("form").live("submit", function() {
var inputText = $(this).find("input[type=text]");

inputText.each(function(i) {
//if value equals placeholder
if ($(this).val() == $(this).attr("placeholder")) {
//then set value to blank
$(this).val("");
}
});
});
}