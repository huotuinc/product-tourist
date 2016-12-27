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

    /**
     * 将2017-01-01:12:00:00 - 2017-01-01:12:00:00这种格式的字符串转换成两个时间格式的字符串
     * @param text
     */
    var dateRangeFormat=function(text){
        var s="2017-01-01:12:00:00";
        var array=[];
        array[0]=text.substr(0, s.length);
        array[1]=text.substr(s.length+3);
        return array;


    };

     var actionFormatter = function (value, row, index) {
            return '<button class="btn btn-link showDetail">详情</button>';
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
        var orderDates=dateRangeFormat($("input[name='orderDate']").val());
        var payDate=dateRangeFormat($("input[name='payDate']").val());
        var touristDate=dateRangeFormat($("input[name='touristDate']").val());
        var temp = {
            pageSize: params.limit, //页面大小
            pageNo: params.offset, //页码
            orderId: $("input[name='orderId']").val(),
            name:$("input[name='name']").val(),
            buyer:$("input[name='buyer']").val(),
            tel:$("input[name='tel']").val(),
            payTypeEnum:$("select[name='orderStateEnum'] option:selected").val(),
            orderStateEnum:$("select[name='orderStateEnum'] option:selected").val(),
            orderDate:orderDates[0],
            endOrderDate:orderDates[1],
            payDate:payDate[0],
            endPayDate:payDate[1],
            touristDate:touristDate[0],
            endTouristDate:touristDate[1],
        };
        //console.log(temp);
        return temp;
    }

