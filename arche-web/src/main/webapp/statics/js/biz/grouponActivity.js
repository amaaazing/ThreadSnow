var productList = [];
// 是否输入了skuId，完成了商品信息的填写
var isQuerySkuInfo = false;
var isShowProductErrorTips = false;
$(function () {

    $(".time").click(function () {
        if($(this).val() == "start time"){
            $(this).val("");
            $(this).css("color","#000000");
        }
        if($(this).val() == "end time"){
            $(this).val("");
            $(this).css("color","#000000");
        }
    });

//    <!--自定义jquery validation校验规则-->
    $.validator.addMethod("custom", function(value, element) {
        if(value == "start time" || value == "end time"){
            return this.optional(element) || false;
        }else{
            return this.optional(element) || true;
        }
    }, "The activity time is required.");

    $.validator.addMethod("upperLimitGtLowerLimit", function(value, element) {
        var lowerLimit = $.trim($("#lowerLimit").val());
        if(!isNull(value) && value < lowerLimit){
            return this.optional(element) || false;
        }else{
            return this.optional(element) || true;
        }
    }, "");

    $.validator.addMethod("isQueryProduct", function(value, element) {
        if(!isQuerySkuInfo){
            return this.optional(element) || false;
        }else{
            return this.optional(element) || true;
        }
    }, "");

    $.validator.addMethod("isExistpriceAndStock", function(value, element) {
        if(isShowProductErrorTips){
            return this.optional(element) || false;
        }else{
            return this.optional(element) || true;
        }
    }, "");

    $("#createGrouponForm").on("blur",".assit_int_input",function () {
        var $priceAndStock = $(".assit_int_input");
        var isAllBingo = 0;
        $.each($priceAndStock, function (i,item) {
            if(isNull(item.value)){
                isShowProductErrorTips = true;
                return false;
            }
            isAllBingo++;
        });

        if(isAllBingo == $priceAndStock.length){
            isShowProductErrorTips = false;
            $("#skuErrorDiv").html("");
        }
    });

    $("#skuId").change(function () {
        isQuerySkuInfo = false;
        $("#skuErrorDiv").html('<label id="skuId-error" class="error" for="skuId">Please click the "Search" button to complete product information.</label>');

    });

    UIAssit.initAssit($("#createGrouponForm"));

});

var isNull = function(data) {
    if(data == null || data == ''|| data == undefined) {
        return true;
    }
    return false;
}

/**
 * 展示商品信息列表
 * @param list
 */
var showProductList = function (data) {
    if(isNull(data)){
        return;
    }
    var defaultHtml ='';
    var proudct = {"priceList":[{"originalPrice":"","price":""}],"stockList":[{"dcId":"","warehouseId":""}]};
    proudct.skuId = data.skuId;
    proudct.skuName = data.name;
    proudct.priceList[0].originalPrice = data.price;

    $.each(data.whList,function (i,item) {
        defaultHtml +='<tr><td>'+data.skuId+'</td>';
        defaultHtml +='<td>'+data.name+'</td>';
        defaultHtml +='<td>'+data.price+'</td>';
        defaultHtml +='<td>'+item.name+'</td>';
        defaultHtml +='<td><input type="text" id="activityPrice'+item.dcId+item.warehouseId+i+'" name="price" class="form-control assit_int_input"></td>';
        defaultHtml +='<td><input type="text" id="stock'+item.dcId+item.warehouseId+i+'" name="stockNum" class="form-control assit_int_input"></td></tr>';

        if( i == 0){
            proudct.stockList[i].warehouseId = item.warehouseId;
            proudct.stockList[i].dcId = item.dcId;
            // 初始化productList，product对象对应的是 (id 为#skuTable)下的列表，需要改动的是里面的stockList子对象
            productList.push(proudct);
        }

        if( i > 0 ){
            //  settings对象为复制为proudct.stockList[0]对象，而proudct.stockList[0]不变
            var settings = $.extend( {}, proudct.stockList[0], {} );
            settings.warehouseId = item.warehouseId;
            settings.dcId = item.dcId;
            proudct.stockList.push(settings);
        }
    });

    $("#skuList").html(defaultHtml);
    $("#skuDiv").css("display","block");
}

var queryProductInfo = function(){

    var skuId = $.trim($("#skuId").val());
    if(isNull(skuId)){
        BootstrapDialog.show({
            message: 'SkuId is required.'
        });
        return;
    }

    jQuery.ajax( {
        url:'/grouponCreation/queryProductInfo',
        data:{"skuId":skuId},
        type:'post',
        dataType:'json',
        success:function(data) {
            if (data.success) {
                // 展示商品数据
                showProductList(data.result);
                isQuerySkuInfo = true;
                $("#skuErrorDiv").html("");
                return;
            }
        },
        error: function () {
            BootstrapDialog.show({
                message: 'An network error occurred. Please try again later.'
            });
        }
    });
}

var getParams = function () {
    var grouponName = $.trim($("#grouponName").val()),
        description = $.trim($("#description").val()),
        remarks = $.trim($("#remarks").val()),
        startTime = $("#startTime").val(),
        endTime = $("#endTime").val(),
        lowerLimit = $.trim($("#lowerLimit").val()),
        upperLimit = $.trim($("#upperLimit").val()),
        timesLimit = $.trim($("#timesLimit").val()),
        updateTime = $("#updateTime").val(),
        grouponId = $("#grouponId").val(),
        skuId = $.trim($("#skuId").val());

    if(productList.length > 0){
        // 新增团购参数获取
        $.each(productList[0].stockList,function (i,item) {
            var stockNode = "stock" + item.dcId + item.warehouseId+i,
                priceNode = "activityPrice" + item.dcId + item.warehouseId+i,
                stockNum = $.trim($("#"+stockNode).val()),
                price = $.trim($("#"+priceNode).val()),
                stock = {};

            if(isNull(price) || isNull(stockNum)){
                isShowProductErrorTips = true;
            }

            // 设置团购价
            productList[0].priceList[0].price = price;
            // 设置库存
            productList[0].stockList[i].stockNum = stockNum;

        });
    }else{
        // 编辑团购参数
        var proudct = {"priceList":[{"originalPrice":"","price":""}],"stockList":[{"dcId":"","warehouseId":""}]};
        var mytable = document.getElementById("skuTable");
        for(var i=0,rows = mytable.rows.length; i<rows; i++){

            if( i == 1){
                // 对应表格中的第一行数据
                proudct.skuId = mytable.rows[1].cells[0].innerHTML;
                proudct.skuName = mytable.rows[1].cells[1].innerHTML;
                var grouponProductId = $("#grouponProductId").val();
                proudct.id = grouponProductId;
                proudct.priceList[0].originalPrice = mytable.rows[1].cells[2].innerHTML;
                proudct.stockList[0].warehouseId = $(''+mytable.rows[1].cells[4].innerHTML).attr("data-whId");
                proudct.stockList[0].dcId = $(''+mytable.rows[1].cells[4].innerHTML).attr("data-dcId");
                // 设置团购价
                proudct.priceList[0].price = $(''+mytable.rows[1].cells[4].innerHTML).val();
                // 设置库存
                proudct.stockList[0].stockNum = $(''+mytable.rows[1].cells[5].innerHTML).val();

                productList.push(proudct);
            }

            if(i > 1){
                //  settings对象为复制为proudct.stockList[0]对象，而proudct.stockList[0]不变
                var settings = $.extend( {}, proudct.stockList[0], {} );
                settings.warehouseId = $(''+mytable.rows[i].cells[4].innerHTML).attr("data-whId");
                settings.dcId = $(''+mytable.rows[i].cells[4].innerHTML).attr("data-dcId");
                // 设置库存
                settings.stockNum = $(''+mytable.rows[i].cells[5].innerHTML).val();

                proudct.stockList.push(settings);

            }


        }


    }

    return {
        "id": grouponId,
        "name": grouponName,
        "description": description,
        "remarks": remarks,
        "startTime": startTime,
        "endTime": endTime,
        "upperLimit": upperLimit,
        "lowerLimit": lowerLimit,
        "timesLimit": timesLimit,
        "updateTime": updateTime,
        "productList": productList
    }
    //productList是数组对象，必须转成数组字符串（如果是传单独的数组字符串参数，须指定contentType = "application/json" ）
    //这里返回的参数是json对象，而不是字符串，不需要指定contentType
}

function getTableContent(id){
    var mytable = document.getElementById(id);
    var data = [];
    for(var i=0,rows=mytable.rows.length; i<rows; i++){
        for(var j=0,cells=mytable.rows[i].cells.length; j<cells; j++){
            if(!data[i]){
                data[i] = new Array();
            }
            data[i][j] = mytable.rows[i].cells[j].innerHTML;
        }
    }
    return data;
}

var saveOrCommit = function (obj) {
    var button = $(obj).attr("id");
    var params = getParams();
    $("#createGrouponForm").validate({
        errorElement: 'label',
        errorPlacement: function(error, element) {
            error.appendTo(element.parents(".jv").siblings().last().find(".validation-error"));
        },
        rules : {
            "grouponActivity.name": {required:true,maxlength:30},
            "grouponActivity.description": {required:true,maxlength:200},
            "grouponActivity.startTime": {required:true,custom:true},
            "grouponActivity.endTime": {required:true,custom:true},
            "grouponActivity.lowerLimit": {required:true,digits:true},
            "grouponActivity.upperLimit": {digits:true,upperLimitGtLowerLimit:true},
            "grouponActivity.timesLimit": {required:true,digits:true},
            "skuId":{isQueryProduct:true,required:true,digits:true,isExistpriceAndStock:true}
        },
        messages : {
            "grouponActivity.name" : {required:"The activity name is required.",maxlength:$.validator.format("Please enter no more than {0} characters.")},
            "grouponActivity.description" : {required:"The description is required.",maxlength:$.validator.format("Please enter no more than {0} characters.")},
            "grouponActivity.startTime" : {required:"The startTime is required.",custom:"The startTime is required."},
            "grouponActivity.endTime" : {required:"The endTime is required.",custom:"The endTime is required."},
            "grouponActivity.lowerLimit" : {required:"The lower limit name is required.",digits:"Please enter only digits."},
            "grouponActivity.upperLimit" : {digits:"Please enter only digits.",upperLimitGtLowerLimit:"The upperLimit should be greater to the lowerLimit."},
            "grouponActivity.timesLimit": {required:"The times limit is required.",digits:"Please enter only digits."},
            "skuId": {required:"The skuId is required.",digits:"Please enter only digits.",isQueryProduct:"Please click the \"Search\" button to complete product information.",isExistpriceAndStock:"The activity price and stock are required."},
        },
        submitHandler:function(){

            if(button == "saveActivity"){
                save(params);
            }

            if(button == "commitActivity"){
                commit(params);
            }
        }
    });
}

/**
 * 保存团购
 */
function save(params) {
    console.log(JSON.stringify(params));
    jQuery.ajax( {
        url:'/grouponCreation/save',
        data:JSON.stringify(params),
        type:'post',
        dataType:'json',
        contentType: "application/json",
        success:function(data) {
            if (data.success) {
                BootstrapDialog.show({
                    message: 'The activity save successfully.'
                });
            }else{
                BootstrapDialog.show({
                    message: data.errors[0]
                });
            }
        },
        error: function () {
            BootstrapDialog.show({
                message: 'An network error occurred. Please try again later.'
            });
        }
    });
}

/**
 * 提交团购
 */
function commit(params) {
    jQuery.ajax( {
        url:'/grouponCreation/commit',
        data:JSON.stringify(params),
        type:'post',
        dataType:'json',
        contentType: "application/json",
        success:function(data) {
            if (data.success) {
                BootstrapDialog.show({
                    message: 'The activity save successfully.'
                });
            }else{
                BootstrapDialog.show({
                    message: data.errors[0]
                });
            }
        },
        error: function () {
            BootstrapDialog.show({
                message: 'An network error occurred. Please try again later.'
            });
        }
    });
}



