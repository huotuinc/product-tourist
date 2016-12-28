/**
 * Created by slt  2016/12/26.
 */
$(function(){
    dateRangeEdit();
});

/**
 * 时间控件编辑
 */
var dateRangeEdit=function(){
    $('input[name$="Date"]').daterangepicker(
        {
            locale: {
                format: 'YYYY-MM-DD hh:mm:ss'
            },
            startDate: '2017-01-01',
            endDate: '2017-06-01'
        }
    );
};


var actionFormatter = function (value, row, index) {
    return  '<button class="btn btn-link showDetail">查看</button>' +
        '<button class="btn btn-link recycle">回收</button>';
};

var touristNameFormatter=function(value,row,index){
    return '<span>'+row.touristName+'</span>';
};

var touristImgUriFormatter=function(value,row,index){
    return '<img src="'+row.touristImgUri+'" height="50px" width="80px">';
};

var actionEvents = {
    'click .showDetail': function (e, value, row, index) {
        var id=row.id;
        location.href="goodsDetails.html";
    },
    'click .recycle': function (e, value, row, index) {
        var id=row.id;
        var tr=$("#goodsTable tr").eq(index+1);
        var td=$("td",tr).eq(9);
        if($(td).text()!="已回收"){
            layer.confirm('确定回收吗？', {
                btn: ['回收','取消'] //按钮
            }, function(){
                //ajax异步回收
                layer.msg('修改成功', {icon: 1});
                $(td).text("已回收");

            }, function(index){
                layer.close(index);
            });
        }else {
            layer.msg("线路商品已回收");
        }



    }
};

var getParams= function(params) {
    var temp = {
        pageSize: params.limit, //页面大小
        pageNo: params.offset, //页码
        touristName: $("input[name='touristName']").val(),
        touristTypeId:$("select[name='touristType'] option:selected").val(),
        activityTypeId:$("select[name='activityType'] option:selected").val(),
        touristCheckState:$("select[name='touristCheckState'] option:selected").val()
    };
    //console.log(temp);
    return temp;
};

