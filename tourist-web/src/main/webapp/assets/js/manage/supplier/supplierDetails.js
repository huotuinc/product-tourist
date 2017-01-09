/**
 * Created by Administrator slt 2016/12/30.
 */
$(function(){

});

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
                $("#pictureUrl").attr("src",resultModel.url);
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
    var supplierName=$("input[name='supplierName']").val();
    var businessLicenseUri=$("#pictureUrl").attr("src");
    var address=$("input[name='address']").val();
    var detailedAddress=$("input[name='detailedAddress']").val();

    var contacts=$("input[name='contacts']").val();

    var contactNumber=$("input[name='contactNumber']").val();

    var remarks=$("textarea[name='remarks']").text();

    //goods.customerId=/*[[${message.customerId}]]*/ '';

    var ld=layer.load(5, {shade: false});
    $(obj).attr("class","btn btn-primary disabled");

    $.ajax({
        type:'POST',
        url: '',
        dataType: 'json',
        contentType:"application/json",
        data:{id:id,supplierName:supplierName,businessLicenseUri:businessLicenseUri,address:address
            ,detailedAddress:detailedAddress, contacts:contacts,contactNumber:contactNumber
            ,remarks:remarks},
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