/**
 * Created by slt  2016/12/26.
 */
$(function(){
    bindSearch();
});

var bindSearch=function(){
    $("#searchList").on("click",function(){
        search();
    });
};

var search=function(){
    $("table").bootstrapTable('refresh');
};


var actionFormatter = function (value, row, index) {
    var detailUrl=/*[[@{/supplier/showTouristGood}]]*/"goodsDetailsH+.html";
    return  '<a href="'+detailUrl+'?id='+row.id+'" class="btn btn-link showDetail">查看</a>' +
            '<button class="btn btn-link recycle">回收</button>';
};

var touristNameFormatter=function(value,row,index){
    return '<span>'+row.touristName+'</span>';
};

var touristImgUriFormatter=function(value,row,index){
    return '<img src="'+row.touristImgUri+'" height="50px" width="80px">';
};

var actionEvents = {
    //'click .showDetail': function (e, value, row, index) {
    //    var id=row.id;
    //    location.href=/*[[@{/supplier/showTouristGood(id=)}]]*/"goodsDetailsH+.html";
    //},
    'click .recycle': function (e, value, row, index) {
        var url=/*[[@{/supplier/showTouristGood(id=)}]]*/"../../../mock/supplier/httpJson.json";
        var tr=$("#goodsTable tr").eq(index+1);
        var td=$("td",tr).eq(9);
        if($(td).text()!="已回收"){
            layer.confirm('确定回收吗？', {
                btn: ['回收','取消'] //按钮
            }, function(){
                //ajax异步回收
                $.ajax({
                    url: url,
                    method: "post",
                    data: {id: row.id, checkState: 3},
                    dataType: "json",
                    success: function () {
                        //$table.bootstrapTable('refresh');
                        layer.msg('修改成功', {icon: 1});
                        $(td).text("已回收");
                    },
                    error: function (error) {
                        layer.msg("修改失败")
                    }
                })
            }, function(index){
                layer.close(index);
            });
        }else {
            layer.msg("线路商品已回收");
        }



    }
};

var getParams= function(params) {
    var touristName=$("input[name='touristName']").val();
    var touristTypeId=$("select[name='touristType'] option:selected").val();
    var activityTypeId=$("select[name='activityType'] option:selected").val();
    var touristCheckState=$("select[name='touristCheckState'] option:selected").val();
    var temp = {
        pageSize: params.limit, //页面大小
        pageNo: params.offset/params.limit, //页码
        sortName: params.sort, sortOrder:params.order,   //排序
        touristName: touristName!=""?touristName:undefined,
        touristTypeId:touristTypeId!="-1"?touristTypeId:undefined,
        activityTypeId:activityTypeId!="-1"?activityTypeId:undefined,
        touristCheckState:touristCheckState!="-1"?touristCheckState:undefined
    };
    //console.log(temp);
    return temp;
};

