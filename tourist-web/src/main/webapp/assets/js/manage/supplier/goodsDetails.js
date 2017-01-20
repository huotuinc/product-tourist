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
    $("#goodsTouristDates").append('<div class="col-sm-2"> <span>出行时间：</span>' +
        '<input name="toursitDate" ' +
        'type="text"class="form-control"/><span style="display: none">1</span></div>');
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
        contentType:'application/json',
        processData: false,
        success: function(data) {
            editor.insertImage(welEditable, data.url);
        }
    });
}

/**
 * 图片上传
 */
var uploadImage=function(isMoreImg,obj){
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
                var imgTemp=getImgTemp(resultModel.fileName,resultModel.url);
                var parentDiv=$(obj).parent().parent().parent();
                var imgTempDiv=$(".imgTemp",parentDiv);
                bulidImgTemp(isMoreImg,imgTemp,imgTempDiv);
            }
        },
        error: function(data, status, e) {
            layer.close(loadPic);
            layer.msg("上传失败，请检查网络后重试"+e);
        }
    });
};

/**
 * 返回一个图片模板
 * @param url
 * @param path
 */
var getImgTemp=function(path,url){
    var temp='<div class="col-sm-2"> ' +
        '<img style="width: 230px;height: 150px" src="'+url+'"/> ' +
        '<input name="path" value="'+path+'" type="hidden"/> ' +
        '<a onclick="delImg(this)" title="Close" class="fancybox-item fancybox-close" href="javascript:;"></a> </div>';
    return temp;

};

/**
 *
 * @param isMoreImg 是否是多图
 * @param imgTemp   图片模板
 * @param obj       添加的对象
 */
var bulidImgTemp=function(isMoreImg,imgTemp,obj){
    if(!isMoreImg){
        $(obj).empty();
    }
    $(obj).append(imgTemp);

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
    //var pictureUrl=$("#pictureUrl").attr("src");
    var touristTypeId=$("select[name='touristType'] option:checked").val();
    var activityTypeId=$("select[name='activityType'] option:checked").val();
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
        var dateLenth="2017-02-02 04:31:00";
        if(fromDate.length<dateLenth.length){
            fromDate=fromDate.replace("T"," ");
            fromDate=fromDate+":00";
        }
        var id=$("span",this).eq(1).text();
        if(fromDate!=""){
            touristRoutes.push({id:id,fromDate:fromDate});
        }
    });
    if(touristRoutes.length==0){
        layer.msg("请至少添加一个行程");
        return;
    }




    var ld=layer.load(5, {shade: false});
    $(obj).attr("class","btn btn-primary disabled");

    $.ajax({
        type:'POST',
        url: submitUrl,
        dataType: 'text',
        data:{id:id,touristName:touristName,touristImgUri:touristImgUri,touristTypeId:touristTypeId,activityTypeId:activityTypeId,
            touristFeatures:touristFeatures,destination:destination,placeOfDeparture:placeOfDeparture,
            travelledAddress:travelledAddress,price:price,childrenDiscount:childrenDiscount,rebate:rebate,
            receptionPerson:receptionPerson,receptionTelephone:receptionTelephone,maxPeople:maxPeople,
            eventDetails:eventDetails,beCareful:beCareful,routes:JSON.stringify(touristRoutes),checkState:checkStates
        },
        success:function(){
            layer.close(ld);
            layer.msg("保存成功！");
            $(obj).attr("class","btn btn-success");
            window.setTimeout("window.location='/base/showGoodsList'",1000);
        },
        error:function(e){
            layer.close(ld);
            layer.msg("保存失败！");
            $(obj).attr("class","btn btn-success");
        }
    });
};


/**
 * 删除图片
 */
var delImg=function(obj){
    var imgDiv=$(obj).parent();
    var path=$("input[name='path']",imgDiv).val();
    //ajax删除图片
    $.ajax({
        type:'POST',
        url: '/upload/delete',
        dataType: 'text',
        data:{path:path},
        success:function(){
            layer.msg("删除失败");

        },
        error:function(){
            layer.msg("删除失败");
        }
    });
    $(imgDiv).remove();


};

