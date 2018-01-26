/**
 * 使用方法：
 *         popupAssit.dialog({
            popupName: ".edit_companyinfo_popwin",
            maskLayer: true
        });
 * 其中edit_companyinfo_popwin为div的class
 */
jQuery.popupUtil = popupAssit = {
    ieLower:$.browser.msie&&$.browser.version==6||false,
    popArgs:{popName:false,popFixed:false,popDelayTime:false},
    dialog:function(args,callback){
        args = args||{};
        var defaultArgs = {
                popupName:"",
                maskLayer:false,
                popupFixed:false,
                delayTime:false,
                fun:[]
            },
            popupArgs = $.extend({},defaultArgs,args),
            popupWidth = $(popupArgs.popupName).outerWidth(),
            popupHeight = $(popupArgs.popupName).outerHeight(),
            winScrollTop = $(window).scrollTop(),
            winHeight = $(window).height(),
            docHeight = $(document).height(),
            popupTop = (winHeight-popupHeight)/2;
        this.popArgs.popName = popupArgs.popupName;
        this.popArgs.popFixed = popupArgs.popupFixed;

        clearTimeout(popupAssit.popArgs.popDelayTime);

        //回调方法
        if(typeof callback !="undefined"){
            +callback();
            popupHeight = $(popupArgs.popupName).outerHeight();
            popupTop = (winHeight-popupHeight)/2;
        }

        //弹窗
        this.popReset(true);

        //遮罩
        if(popupArgs.maskLayer){
            $(popupArgs.popupName).after("<div class='popup_win_mask'></div>");
            $(".popup_win_mask").css("height",docHeight);
            if(popupAssit.ieLower){
                $(".popup_win_mask").append("<iframe class='popup_win_iframe' frameborder='0'></iframe>");
                $(".popup_win_iframe").css("height",docHeight);
            }
        }

        //X秒关闭
        if(popupArgs.delayTime){
            this.popArgs.popDelayTime = setTimeout(function(){
                popupAssit.closePopup(popupArgs.popupName);
            },popupArgs.delayTime);
        }

        //点击关闭按钮
        $(".popup_btn_close",popupArgs.popupName).click(function(){
            popupAssit.closePopup(popupArgs.popupName);
        });

        //回调方法
        if(popupArgs.fun.length)(new Function(popupArgs.fun.join('();')+'();'))();
    },

    /**
     * 关闭弹窗方法
     */
    closePopup:function(popupName,callback){
        $(popupName).fadeOut(200);
        $(popupName).next(".popup_win_mask").remove();
        //回调方法
        if(typeof callback !="undefined"){
            +callback();
        }
    },

    /**
     * 弹窗方法
     */
    popReset:function(arg){
        var popArgs = this.popArgs,
            popName = $(popArgs.popName),
            popupWidth = popName.width(),
            popupHeight = popName.height(),
            winHeight = $(window).height(),
            popupTop = (winHeight-popupHeight)/2,
            winScrollTop = $(window).scrollTop(),
            popFixed = popArgs.popFixed;
        console.log(winHeight);

        //悬浮定位
        if(popFixed){
            if(typeof arg !="undefined"){
                if(winHeight>=popupHeight){
                    popName.css({position:"fixed",marginLeft:-(popupWidth/2)+"px",top:popupTop+"px"}).fadeIn();
                    if(popupAssit.ieLower){
                        popName.css({position:"absolute",top:popupTop+winScrollTop+"px"});
                        $(window).scroll(function(){
                            popName.css({top:popupTop+$(window).scrollTop()});
                        });
                    }
                }else{
                    popName.css({position:"fixed",marginLeft:-(popupWidth/2)+"px",top:"0"}).fadeIn();
                    if(popupAssit.ieLower){
                        popName.css({position:"absolute",top:winScrollTop+"px"});
                        $(window).scroll(function(){
                            popName.css({top:$(window).scrollTop()});
                        });
                    }
                }
            }else{
                if(winHeight>=popupHeight){
                    popName.css({top:popupTop+"px"});
                    if(popupAssit.ieLower){
                        popName.css({position:"absolute",top:popupTop+winScrollTop+"px"});
                        $(window).scroll(function(){
                            popName.css({top:popupTop+$(window).scrollTop()});
                        });
                    }
                }else{
                    popName.css({top:"0"});
                    if(popupAssit.ieLower){
                        popName.css({position:"absolute",top:winScrollTop+"px"});
                        $(window).scroll(function(){
                            popName.css({top:$(window).scrollTop()});
                        });
                    }
                }
            }
        }else{
            if(typeof arg !="undefined"){
                if(winHeight>=popupHeight){
                    popName.css({marginLeft:-(popupWidth/2)+"px",top:popupTop+winScrollTop+"px"}).fadeIn();
                }else{
                    popName.css({marginLeft:-(popupWidth/2)+"px",top:winScrollTop+"px"}).fadeIn();
                }
            }else{
                if(winHeight>=popupHeight){
                    popName.css({top:popupTop+winScrollTop+"px"});
                }else{
                    popName.css({top:winScrollTop+"px"});
                }
            }
        }
    }
}