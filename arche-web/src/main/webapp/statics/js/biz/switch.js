/** 开关按钮事件监听
 * 使用事件委托:将事件绑定到还没加载进document的元素，一定要将事件绑定到#onoffswitch。
 */
$(document).on('click','input[id^="onoffswitch"]' ,function(event){
    // 判断是用户点击还是js点击（trigger触发：初始化开关样式）
    var $this = $(this);
    if(event.originalEvent){
        // 用户点击
        var grouponId = $this.next().next().val(),

            url = baseURI + "/qiangManager/save.action?qiangActivityVo.id="+grouponId;

        if ($this.is(':checked')) {
            // 开关：由否----->是，按钮是先进行样式变化，后触发事件
            url = url + "&qiangActivityVo.isShow=1";
        } else {
            // 开关：由是----->否
            url = url + "&qiangActivityVo.isShow=0";
        }

        var index = layer.load(1, {
            shade: [0.6,'#000'] //透明度的背景
        });

        $.ajax( {
            type : "POST",
            dataType : 'json',
            url : url,
            data :"",
            success : function(rtn) {
                layer.close(index);
                layer.alert("保存成功",9);// type = 9 是笑脸，=8 是哭脸
            },
            error:function (rtn) {
                layer.close(index);
                layer.alert("前台投放保存失败，请稍后再试。。。",8);
            }
        });
    }
});

function isNull(data) {
    if(data == null || data == ''|| data == undefined) {
        return true;
    }
    return false;
}

/**
 *
 * @param src
 * @param pos 保留的小数的位数
 * @returns {number}
 */
function formatFloat(src,pos){
    if(isNaN(src) || src == Infinity){
        return 0;
    }
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}