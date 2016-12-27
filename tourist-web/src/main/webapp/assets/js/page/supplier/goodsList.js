/**
 * Created by slt  2016/12/26.
 */
$(function(){
});


var actionFormatter = function (value, row, index) {
    return  '<button class="btn btn-link showDetail">查看</button>' +
            '<button class="btn btn-link showDetail">回收</button>';
};

var touristNameFormatter=function(value,row,index){
    return '<img src="'+row.touristImgUri+'" height="50px" width="80px"><span>'+row.touristName+'</span>';
};

var remarkFormatter=function(value,row,index){
    var txt="";
    var size=row.remarks.length;
    if(size>5){
        txt =row.remarks.substr(0,5)+"...";
    }else {
        txt= row.remarks;
    }
    return "<a href='#' class=''>"+txt+"</a>";

};

var remarkEvents={
    'click a': function (e, value, row, index) {
        layer.open({
            type: 1,
            title: false,
            area: ['400px', '200px'],
            shade: false,
            btn:['确定'],
            //closeBtn: 0,
            shadeClose: true,
            content: "<p contenteditable='true' class='text-area'>"+value+"</p>",
            yes: function(index){
                //ajax修改备注
                var newText=$("p").text();
                layer.msg(newText);
                value=newText;
                layer.close(index);
            }
        });
    }
};

var modifyLocalRemark=function(){

};

var actionEvents = {
    'click .showDetail': function (e, value, row, index) {
        var id=row.id;
        location.href="orderDetails.html";
        console.log(id);
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
}

