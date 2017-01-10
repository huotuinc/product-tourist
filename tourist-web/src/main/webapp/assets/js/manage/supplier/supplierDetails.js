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
