modifyTouristGoodState = /*[[@{/distributionPlatform/modifyTouristGoodState}]]*/ "../../../mock/platform/httpJson.json";
toTouristGoodList = /*[[@{/distributionPlatform/toTouristGoodList}]]*/ "touristGoodList.html";
// $(".modifyCheckState").click(function () {
//     layer.confirm('确定取审核通过吗？', {
//         btn: ['确定', '取消']
//     }, function (index) {
//         layer.close(index);
//         var load = layer.load();
//         $.ajax({
//             url: modifyTouristGoodState,
//             method: "post",
//             data: {id: $("#id").val(), checkState: 2},
//             dataType: "json",
//             success: function () {
//                 layer.close(load)
//                 window.location.href = toTouristGoodList;
//             },
//             error: function (error) {
//                 layer.close(load)
//                 layer.msg(error)
//             }
//         })
//
//     }, function () {
//
//     });
//
// })

// ck editor
var editor = $(".editor");
if (editor.length > 0) {
    var url = parent.imageUploaderUrl;
    url = url || 'http://cms.51flashmall.com/upload/image';
    editor.ckeditor({
        extraPlugins: 'uploadimage',
        uploadUrl: url,
        language: parent.language
    });
}

