// 定义一个独立模块
define(function () {
    // 定义一个拥有method1、method2两个方法的模块。
    return {
        method1: function() {},
        method2: function() {}
    }
});

// 定义一个非独立模块
define(['module1', 'jQuery'], function(m1, $) {
    return {

    }
});


// 为了在模块内部调用全局变量，必须显式地将其他变量输入模块。
var module1 = (function ($, YAHOO) {

    //...

})(jQuery, YAHOO);


