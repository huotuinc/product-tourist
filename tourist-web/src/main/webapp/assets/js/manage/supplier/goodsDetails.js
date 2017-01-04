/**
 * Created by Administrator slt 2016/12/30.
 */
$(function(){
    dateRangeEdit();
    setRichText();
    showDiscountInfo();
    showRebateInfo();
    bindTouristDateDel();

});

/**
 * 儿童折扣信息提示
 */
var showDiscountInfo=function(){
    $("#discountInfo").on("click",function(){
        var that = this;
        layer.tips('无儿童价则此项不填，如果有则填入折扣值1-10，如：8折，则填入8即可。则前台收取儿童费用为成人价格的8折每位，不得大于10',that,{
            tips: [1, '#2F4056'],
            time: 4000
        });
    });
};

/**
 * 返利信息提示
 */
var showRebateInfo=function(){
    $("#rebateInfo").on("click",function(){
        var that = this;
        layer.tips('按路线价格的比例返佣比例。0-100。不得大于100',that,{
            tips: [1, '#2F4056'],
            time: 4000
        });
    });
};

/**
 * 日期控件编辑
 */
var dateRangeEdit=function(){
    $('input[name="toursitDate"]').daterangepicker({
        locale: {
            format: 'YYYY-MM-DD hh:mm:ss',
            cancelLabel:'删除'
        },
        singleDatePicker: true,
        showDropdowns: true,
        startDate: "2017-02-01",
        drops: "up"
    });
};


/**
 * 删除行程时间
 */
var bindTouristDateDel=function(){
    $('input[name="toursitDate"]').on('cancel.daterangepicker', function (ev, picker) {
        if($(this).next().text()=="已售出"){
            layer.msg("线路已售出，无法删除！");
            return;
        }
        var that=this;
        layer.confirm('确定删除吗?', function(index){
            if(false){
                $.ajax({
                    type:'POST',
                    url: '',
                    dataType: 'json',
                    data: {id:id},
                    success:function(result){
                        layer.msg("删除成功！");
                    },
                    error:function(e){
                        layer.msg("删除出错！");
                    }
                });
            }

            $(that).parent().remove();
            layer.close(index);
        });
    });
};

/**
 * 增加一个行程时间
 */
var addTouristDate=function(){
    $("#goodsTouristDates").append('<div class="col-sm-2"> ' +
        '<input name="toursitDate" ' +
        'type="text"class="form-control"/><span>已售出</span><span style="display: none">1</span></div>');
    dateRangeEdit();
    bindTouristDateDel();
};

/**
 * 判断字符串是否为空
 * @param str
 * @returns {boolean}
 */
var checkStringIsEmpty=function(str){
    if(str==undefined||str==null||str==""){
        return true;
    }else {
        return false;
    }
};

/**
 * 富文本处理
 */
var setRichText=function(){
    $(".summernote").summernote({
        lang: "zh-CN",
        onImageUpload: function(files, editor, welEditable) {
            sendFile(files[0],editor,welEditable);
        }
    })

};
/**
 * 富文本图片上传
 * @param file
 * @param editor
 * @param welEditable
 */
var sendFile=function(file,editor,welEditable) {
    data = new FormData();
    data.append("upload", file);
    $.ajax({
        data: data,
        type: "POST",
        url: "/upload/image",
        cache: false,
        contentType: false,
        processData: false,
        success: function(data) {
            editor.insertImage(welEditable, data.url);
        }
    });
}

/**
 * 图片上传
 */
var uploadImage=function(){
    var loadPic=layer.load(0, {shade: false});
    $.ajaxFileUpload({
        url: '/upload/image',
        secureuri: false,
        fileElementId: 'upload',
        dataType: 'json',
        data: null,
        success: function(resultModel) {
            if(resultModel.success){
                layer.close(loadPic);
                layer.msg("上传成功");
                $(".image-crop img").attr("src",resultModel.url);
            }
        },
        error: function(data, status, e) {
            layer.close(loadPic);
            layer.msg("上传失败，请检查网络后重试"+e);
        }
    });
};

/**
 * 提交表单
 * @param obj
 */
var submitForm=function(checkStates,obj) {
    if($(obj).attr("class")=="btn btn-primary disabled"){
        return;
    }
    var id=$("input[name='id']").val();
    var touristName=$("input[name='touristName']").val();
    var pictureUrl=$("#pictureUrl").attr("src");
    var touristType=$("select[name='touristType'] option:checked").val();
    var activityType=$("select[name='activityType'] option:checked").val();
    var touristFeatures=$("input[name='touristFeatures']").val();
    var destination=$("input[name='destination']").val();
    var placeOfDeparture=$("input[name='placeOfDeparture']").val();
    var travelledAddress=$("input[name='travelledAddress']").val();
    var price=$("input[name='price']").val();
    var childrenDiscount=$("input[name='childrenDiscount']").val();
    var rebate=$("input[name='rebate']").val();
    var receptionPerson=$("input[name='receptionPerson']").val();
    var receptionTelephone=$("input[name='receptionTelephone']").val();
    var maxPeople=$("input[name='maxPeople']").val();
    var eventDetails=$("#eventDetails").code();
    var beCareful=$("#beCareful").code();
    var touristRoutes=[];
    $("#goodsTouristDates").children("div").each(function(){
        var fromDate=$("input[name='toursitDate']",this).val();
        var id=$("span",this).eq(1).text();
        touristFeatures.push({id:id,fromDate:fromDate});
    });


    //goods.customerId=/*[[${message.customerId}]]*/ '';

    var ld=layer.load(5, {shade: false});
    $(obj).attr("class","btn btn-primary disabled");

    $.ajax({
        type:'POST',
        url: '',
        dataType: 'json',
        contentType:"application/json",
        data:{id:id,touristName:touristName,pictureUrl:pictureUrl,touristType:touristType,activityType:activityType,
            touristFeatures:touristFeatures,destination:destination,placeOfDeparture:placeOfDeparture,
            travelledAddress:travelledAddress,price:price,childrenDiscount:childrenDiscount,rebate:rebate,
            receptionPerson:receptionPerson,receptionTelephone:receptionTelephone,maxPeople:maxPeople,
            eventDetails:eventDetails,beCareful:beCareful,touristRoutes:touristRoutes
        },
        success:function(result){
            layer.close(ld);
            layer.msg("保存成功！");
            $(obj).attr("class","btn btn-success");
//            window.setTimeout("window.location='/back/showMessageList'",1000);
        },
        error:function(e){
            layer.close(ld);
            layer.msg("保存失败！");
            $(obj).attr("class","btn btn-success");
        }
    });
};