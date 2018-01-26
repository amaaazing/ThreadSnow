/*
        js由毫秒数得到年月日
        使用： (new Date(data[i].creationTime)).Format("yyyy-MM-dd hh:mm:ss.S")
        或者 (new Date(data[i].creationTime)).Format("yyyy-MM-dd hh:mm")
*/
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


String.prototype.getCharLen = function() {
    var l = 0;
    var a = this.split("");
    for ( var i = 0; i < a.length; i++) {
        if (a[i].charCodeAt(0) < 299) {
            l++;
        } else {
            l += 2;
        }
    }
    return l;
};

String.prototype.getDBUTF8Len = function() {
    var l = 0;
    var a = this.split("");
    for ( var i = 0; i < a.length; i++) {
        if (a[i].charCodeAt(0) < 299) {
            l++;
        } else {
            l += 3;
        }
    }
    return l;
};

String.prototype.getSubstr = function(len) {
    var str = this;
    if (!str || !len) {
        return '';
    }
    // 预期计数：中文2字节，英文1字节
    var a = 0;
    // 循环计数
    var i = 0;

    // 临时字串
    var temp = '';
    for (i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 255) {
            // 按照预期计数增加2
            a += 2;
        } else {
            a++;
        }
        // 如果增加计数后长度大于限定长度，就直接返回临时字符串
        if (a > len) {
            return temp;
        }
        // 将当前内容加到临时字符串
        temp += str.charAt(i);
    }
    // 如果全部是单字节字符，就直接返回源字符串
    return str;
};

String.prototype.getDBUTF8Substr = function(len) {
    var str = this;
    if (!str || !len) {
        return '';
    }
    // 预期计数：中文3字节，英文1字节
    var a = 0;
    // 循环计数
    var i = 0;

    // 临时字串
    var temp = '';
    for (i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 255) {
            // 按照预期计数增加2
            a += 3;
        } else {
            a++;
        }
        // 如果增加计数后长度大于限定长度，就直接返回临时字符串
        if (a > len) {
            return temp;
        }
        // 将当前内容加到临时字符串
        temp += str.charAt(i);
    }
    // 如果全部是单字节字符，就直接返回源字符串
    return str;
};

// 输入框的长度限制方法
function checkMaxLength(obj, len) {
    var strValue = obj.value;
    if (strValue.getDBUTF8Len() >= len) {
        obj.value = strValue.getDBUTF8Substr(len);
    }
}

// 验证是否是小数或者整数或者负数
function isDecimal(obj, whole, dec, isNeg) {
    var strValue = obj.value;
    var negFlag = "";
    if (isNeg) {
        if (strValue.indexOf("-") != -1 && strValue.indexOf("-") == 0) {
            negFlag = "-";
            strValue = strValue.substring(1);
        }
    }
    if (!/^(\d)*(\.(\d)*)?$/.exec(strValue)) {
        obj.value = "";
    }
    if (strValue.length <= whole + dec + 1) {
        if (strValue.indexOf(".") > -1) {
            if (strValue.indexOf(".") > parseInt(whole)) {
                obj.value = negFlag + strValue.getDBUTF8Substr(whole);
            } else {
                if (strValue.length - strValue.indexOf(".") - 1 > dec) {
                    obj.value = negFlag
                        + strValue.getDBUTF8Substr(strValue.indexOf(".")
                            + 1 + dec);
                }
            }
        } else {
            if (strValue.length > parseInt(whole)) {
                obj.value = negFlag + strValue.getDBUTF8Substr(whole);
            }
        }
    } else {
        if (strValue.indexOf(".") > -1) {
            if (strValue.indexOf(".") > parseInt(whole)) {
                obj.value = negFlag + strValue.getDBUTF8Substr(whole);
            } else {
                if (strValue.length - strValue.indexOf(".") - 1 > dec) {
                    obj.value = negFlag
                        + strValue.getDBUTF8Substr(strValue.indexOf(".")
                            + 1 + dec);
                }
            }
        } else {
            obj.value = negFlag + strValue.getDBUTF8Substr(whole + dec + 1);
        }
    }
}

// 验证是否为数字 可以含小数点
// 否就清空
function checkNumber(obj) {
    if (isNaN(obj.value)) {
        obj.value = "";
    }
}

// 验证是否为数字 不含小数点 0可以是第一位
// 把不是数字的去掉
function checkData(obj) {
    obj.value = obj.value.replace(/\D/g, '');
}

// 验证一个数字是否为整数（正、负、正负）
// 整数的长度
function checkIntegerUtil(value, posOrNeg, startWhole, endWhole) {
    var re;
    if (parseInt(startWhole) > 0) {
        startWhole = parseInt(startWhole) - 1;
    } else {
        startWhole = 1;
    }
    if (parseInt(endWhole) > 0) {
        endWhole = parseInt(endWhole) - 1;
    } else {
        endWhole = 1;
    }
    if (posOrNeg == 1 || posOrNeg == '1') {
        re = new RegExp("^[+]?[0-9]\\d{" + startWhole + "," + endWhole + "}$");
    } else if (posOrNeg == 2 || posOrNeg == '2') {
        re = new RegExp("^[-]?[0-9]\\d{" + startWhole + "," + endWhole + "}$");
    } else {
        re = new RegExp("^[+-]?[0-9]\\d{" + startWhole + "," + endWhole + "}$");
    }
    dd = (re.test(value));
    return dd;

}

// 验证是否为正整数
// obj：数字 posOrNeg：1正整数 2：负整数 0：不要求
function checkInteger(obj, posOrNeg, startWhole, endWhole) {
    var posNeg;
    if (posOrNeg == 1 || posOrNeg == '1') {
        posNeg = /^[+]?]*$/;
    } else if (posOrNeg == 2 || posOrNeg == '2') {
        posNeg = /^[-]?]*$/;
    } else {
        posNeg = /^[+-]?]*$/;
    }
    if (!checkIntegerUtil(obj.value, posOrNeg, startWhole, endWhole)
        && !posNeg.test(obj.value)) {
        obj.value = "";
        obj.focus();
        return false;
    }
}

// 验证是否为正整数 可是为0
// obj：数字 posOrNeg：1正整数 2：负整数 0：不要求
function checkIntegerHasZero(obj, posOrNeg, startWhole, endWhole) {
    var posNeg;
    if (posOrNeg == 1 || posOrNeg == '1') {
        posNeg = /^[+]?]*$/;
    } else if (posOrNeg == 2 || posOrNeg == '2') {
        posNeg = /^[-]?]*$/;
    } else {
        posNeg = /^[+-]?]*$/;
    }
    if (!checkIntegerUtil(obj.value, posOrNeg, startWhole, endWhole)
        && !posNeg.test(obj.value) && obj.value != 0) {
        obj.value = "";
        obj.focus();
        return false;
    }
}

// 是否是小数
// obj dec小数位
function checkDecimal(obj, posOrNeg, startWhole, endWhole, startDec, endDec) {
    var re;
    var posNeg;
    if (posOrNeg == 1 || posOrNeg == '1') {
        re = new RegExp("^[+]?\\d{" + startWhole + "," + endWhole + "}(\\.\\d{"
            + startDec + "," + endDec + "})?$");
        posNeg = /^[+]?]*$/;
    } else if (posOrNeg == 2 || posOrNeg == '2') {
        re = new RegExp("^[-]?\\d{" + startWhole + "," + endWhole + "}(\\.\\d{"
            + startDec + "," + endDec + "})?$");
        posNeg = /^[-]?]*$/;
    } else {
        re = new RegExp("^[+-]?\\d{" + startWhole + "," + endWhole
            + "}(\\.\\d{" + startDec + "," + endDec + "})?$");
        posNeg = /^[+-]?]*$/;
    }
    if (!re.test(obj.value) && !posNeg.test(obj.value)) {
        obj.value = "";
        obj.focus();
        return false;
    }
}

// 是否是小数
// obj
function checkPlusMinus(obj) {
    posNeg1 = /^[+]?]*$/;
    posNeg2 = /^[-]?]*$/;
    if (posNeg1.test(obj.value) || posNeg2.test(obj.value)) {
        obj.value = "";
        // obj.focus();
        return false;
    }
}
// 验证文件的大小
function checkFileSize(obj, size) {
    if (obj.value.length == 0)
        return true;
    var Sys = {};
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        Sys.ie = true;
    }
    if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) {
        Sys.firefox = true;
    }
    var filesize = 0;
    // 火狐下
    if (Sys.firefox) {
        filesize = obj.files[0].size;
    } else if (Sys.ie) {
        var filePath = obj.value;
        var image = new Image();
        image.src = filePath;
        filesize = image.fileSize;
    }
    // 如果文件大小超过
    if (filesize > size * 1024 * 1024) {
        alert("附件不能超过" + size + "M");
        obj.focus();
        return false;
    }
    return true;
}

// 验证上传图片格式问题
function checkFileType(obj) {
    if (obj.value.length == 0)
        return true;
    // 判断图片格式
    var type = obj.value.substr(obj.value.lastIndexOf(".") + 1,
        obj.value.length);
    type = type.toUpperCase();
    if (type == "JPG" || type == "PNG" || type == "GIF" || type == "BMP"
        || type == "JPEG") {
    } else {
        obj.focus();
        alert("图片格式不正确");
        return false;
    }
    return true;
}

// 验证上传图片格式问题
function checkFileTypeExcel(obj) {
    if (obj.val().length == 0)
        return true;
    // 判断图片格式
    var type = obj.val().substr(obj.val().lastIndexOf(".") + 1,
        obj.val().length);
    type = type.toUpperCase();
    if (type == "XLSX" || type == "XLS") {
    } else {
        obj.focus();
        alert("文件格式不正确");
        return false;
    }
    return true;
}

