/**
 * 使用方法：
 *      1.在input标签中加上class="assit_int_input"
 *      2.UIAssit.initAssit($("#表单id"));
 *
 * 作用：当用户在class="assit_int_input"的input标签中输入的内容不是数字时，
 * 会自动清空输入的字符，直到输入的是整数或小数
 */

/*********Begin UIAssit***********/
String.prototype.isBlank = function() {
    return this.trim() == "";
};
var UIAssit = function($form) {
    this.form = $form;
};
//只能输入整数
UIAssit.prototype.intInputAssit = function() {
    var cssSelector = ".assit_int_input";
    var $form = this.form;

    $form.on('focus',cssSelector,function () {
        var $this = $(this);
        $this.data("oldValue", $this.val());
    });

    $form.on('keyup',cssSelector,function () {
        var $this = $(this);
        var newValue = $this.val();
        if (!newValue.isBlank()) {
            var intValue = parseInt(newValue);
            if (isNaN(intValue)) {
                $this.val($this.data("oldValue"));
            } else {
                $this.val(intValue);
            }
        }
    });

    // $form.find(cssSelector).focus(function() {
    // 	var $this = $(this);
    // 	$this.data("oldValue", $this.val());
    // }).keyup(function() {
    // 	var $this = $(this);
    // 	var newValue = $this.val();
    // 	if (!newValue.isBlank()) {
    // 		var intValue = parseInt(newValue);
    // 		if (isNaN(intValue)) {
    // 			$this.val($this.data("oldValue"));
    // 		} else {
    // 			$this.val(intValue);
    // 		}
    // 	}
    // });
};
/**
 *可以输入小数及整数
 *eg:data-inputAssit='{"format",".99"}'表示小数点后两位，整数位无限制
 *
 * }";
 */
UIAssit.prototype.floatInputAssit = function() {
    var cssSelector = ".assit_float_input";
    var dataName = "assitoption";
    var keyFormat = "format";
    var $form = this.form;
    var assitTargets = $form.find(cssSelector);

    function getFormatReg($inputObj) {
        var data = $inputObj.data(dataName);
        var formatReg = "";
        if (data) {
            var format = $inputObj.data(dataName)[keyFormat];
            var pointIndex = format.indexOf('.');
            //整数位个数
            var intCnt = format.substring(0, pointIndex).length;
            //小数点后位数
            var precision = format.substring(pointIndex + 1, format.length).length;
            formatReg += "^";
            if (intCnt == 0) {
                formatReg += "\\d+";
            } else if (intCnt > 0) {
                formatReg += "\\d{1," + intCnt + "}";
            }
            if (precision > 0) {
                formatReg += "(\\.\\d{1," + precision + "})?";
            }
            formatReg += "$";
        }
        return formatReg;
    };

    $form.find(cssSelector).focus(function() {
        var $this = $(this);
        $this.data("oldValue", $this.val());
    }).keyup(function(event) {
        var $this = $(this);
        var newValue = $this.val();
        //backspace
        if (event.keyCode == 8) {
            $this.data("oldValue", $this.val());
            return;
        }

        if (!newValue.isBlank()) {
            if (newValue.endsWith("..")) {
                //禁止输入连续的两个点
                $this.val($this.data("oldValue"));
            } else if (/^\d+$/.test(newValue)) {
                //整数
                $this.data("oldValue", $this.val());
                return;
            } else {
                if (event.keyCode != 190) {
                    //不是点号'.'
                    var formatReg = getFormatReg($this);
                    if (!formatReg.isBlank()) {
                        var reg = new RegExp(formatReg);
                        if (!reg.test(newValue)) {
                            $this.val($this.data("oldValue"));
                        } else {
                            $this.data("oldValue", $this.val());
                        }
                    }
                } else {
                    $this.data("oldValue", $this.val());
                }
            }
        }
    }).blur(function() {
        var $this = $(this);
        var newValue = $this.val();
        if (!newValue.isBlank()) {
            var formatReg = getFormatReg($this);
            if (!formatReg.isBlank()) {
                var reg = new RegExp(formatReg);
                if (!reg.test(newValue)) {
                    // this.focus();
                    setTimeout(function() {
                        $this.get(0).focus();
                    }, 0);
                }
            }
        }
    });
};

UIAssit.initAssit = function($form) {
    var assit = new UIAssit($form);
    assit.intInputAssit();
    assit.floatInputAssit();
};

/**
 *
 *check必须输入项
 * @param:$form:待check的form,JQuery对象
 * @return:true->check OK,false->check NG
 */
UIAssit.prototype.validate = function($form) {
    var isOK = true;
    var $form = this.form;
    isOK = UIAssit.validateRequire($form);
    if (!isOK) {
        return isOK;
    }
    isOK = UIAssit.validateLength($form);
    return isOK;
};

UIAssit.validateRequire = function($form) {
    var targetCss = "assit_require";
    var errorCss = "red_border";
    var errorDivCss = "div_error";
    var isOk = true;
    $form.find("." + targetCss).each(function(index) {
        var $this = $(this);
        var inputType = $this.attr("type");
        var validatedCkArray = [];
        switch(inputType) {
            case "checkbox":
                var ckName = $this.attr("name");
                if (validatedCkArray.indexOf(ckName) == -1) {
                    //同组的checkbox之前未检验过
                    validatedCkArray.push(ckName);
                    if ($form.find("input[name=]" + ckName + "]:checked").length == 0) {
                        //同组的checkbox没有一个选 中,找到最近的那个div
                        $this.closest("div").addClass(errorDivCss);
                        isOk = false;
                    } else {
                        $this.closest("div").removeClass(errorDivCss);
                    }
                }
                break;
            case "textarea":
            case "text":
                if ($this.val().isBlank()) {
                    isOk = false;
                    $this.addClass(errorCss);
                } else {
                    $this.removeClass(errorCss);
                }
                break;
            default:
            //Log
        }

    });

    return isOk;
};
UIAssit.validateLength = function($form) {
    var errorCss = "red_border";
    var cssSelector = ".assit_length";
    var dataName = "assitoption";
    var keyFormat = "format";
    var assitTargets = $form.find(cssSelector);
    var isOK = true;
    assitTargets.each(function(index) {
        var $this = $(this);
        var valLength = $this.val().trim().length;
        var assitOption = $this.data(dataName);
        var maxLength = assitOption.maxLength;
        var minLength = assitOption.minLength;
        if (minLength && valLength < minLength) {
            isOK = false;
            $this.addClass(errorCss);
        } else if (maxLength && valLength > maxLength) {
            isOK = false;
            $this.addClass(errorCss);
        } else {
            $this.removeClass(errorCss);
        }
    });
    return isOK;
};
/*********End UIAssit***********/