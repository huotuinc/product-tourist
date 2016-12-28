/**
 * Created by slt  2016/12/26.
 */
    $(function(){
        dateRangeEdit();
        bindSearch();
        changesStyleTouristDate();
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
        var s = "2017-01-01 12:00:00";
        var array=[];
        array[0]=text.substr(0, s.length);
        array[1]=text.substr(s.length+3);
        return array;
    };


    var bindSearch=function(){
        $("#searchList").on("click",function(){
           search();
        });
    };

     var search=function(){
         $("table").bootstrapTable('refresh');
     };

    var actionFormatter = function (value, row, index) {
            return '<button class="btn btn-link showDetail">详情</button>';
    };

    var touristNameFormatter=function(value,row,index){
        return '<img src="'+row.touristImgUri+'" height="50px" width="80px"><span>'+row.touristName+'</span>';
    };

    var touristDateFormatter=function(value,row,index){
        var html='<span>'+row.touristDate+'</span><p><a class="btn btn-link showDetail">修改出行时间</a></p>';
        return html;
    };

    var touristDateEvents={
        'click .showDetail': function (e, value, row, index) {
            var id=row.id;
            var formerId=row.touristRouteId;
            var peopleNumber=parseInt(row.peopleNumber);
            getAllOrderTouristDate(id);
            layer.open({
                type:1,
                area: ['700px', '400px'],
                title:"修改出行时间",
                btn:['确定修改','取消'],
                content:$("#modifyTouristDate"),
                yes:function(index){
                    var div=$("div[class='panel panel-success']",this.content);
                    var laterId=parseInt($(".laterId",div).text());
                    var surplusNumber=parseInt($(".surplusNumber",div).text());
                    if(peopleNumber>surplusNumber){
                        layer.msg("修改人数超出限制");
                    }
                    //layer.msg("formerId:"+formerId+",laterId:"+laterId);
                    layer.close(index);
                }
            });
        }
    };

    var modifyOrderTouristDate=function(formerId,laterId){
        $.ajax({
            type:'POST',
            url: '',
            dataType: 'json',
            data: {formerId:formerId,laterId:laterId},
            success:function(result){
                layer.msg("保存成功！");
            },
            error:function(e){
                layer.msg("保存出错！");
            }
        });
    };

    var getAllOrderTouristDate=function(id){
        $.ajax({
            type:'GET',
            url: '../../mock/supplier/allOrderTouristDate.json',
            dataType: 'json',
            data: {id:id},
            success:function(result){
                console.log(result);
                bulidAllOrderTouristDate(result.data);
            },
            error:function(e){
                layer.msg("获取出行线路信息出错！");
            }
        });
    };

    var bulidAllOrderTouristDate=function(data){
        $("#modifyTouristDate").empty();
        var html="";
        for(var i=0;i<data.length;i++){
            var model=data[i];
            html+= '<div class="col-sm-4"> ' +
                '<div class="panel panel-default"> ' +
                '<div class="panel-body"> ' +
                '<p class="laterId" style="display: none">'+model.id+'</p> ' +
                '<p style="text-align: center">'+model.fromDate+'</p> ' +
                '<p style="text-align: center">剩余<span class="surplusNumber">'+model.remainPeople+'</span>人</p> ' +
                '</div> </div> </div>';

        }
        $("#modifyTouristDate").append(html);
    };

    var changesStyleTouristDate=function(){
        $("#modifyTouristDate").on("mouseover","div[class='panel panel-default']",function(){
            if($(this).attr("class")!="panel panel-success"){
                $(this).attr("class","panel panel-primary");
            }

        });
        $("#modifyTouristDate").on("mouseout","div[class='panel panel-primary']",function(){
            if($(this).attr("class")!="panel panel-success"){
                $(this).attr("class","panel panel-default");
            }

        });
        $("#modifyTouristDate").on("click","div[class='panel panel-primary']",function(){
            $(".panel-success").each(function(){
                $(this).attr("class","panel panel-default");
            })
            $(this).attr("class","panel panel-success");
        });
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

    var actionEvents = {
        'click .showDetail': function (e, value, row, index) {
            var id=row.id;
            location.href="orderDetails.html";
            console.log(id);
        }
    };

    var getParams= function(params) {
        var orderStateEnum=0;
        $("#shortcutSearch li").each(function(index,val){
            if($(val).hasClass("active")){
                orderStateEnum=index;
                return;
            };
        });

        var orderDates=dateRangeFormat($("input[name='orderDate']").val());
        var payDate=dateRangeFormat($("input[name='payDate']").val());
        var touristDate=dateRangeFormat($("input[name='touristDate']").val());
        var orderId=$("input[name='orderId']").val();
        var name=$("input[name='name']").val();
        var buyer=$("input[name='buyer']").val();
        var tel=$("input[name='tel']").val();
        var temp = {
            pageSize: params.limit, //页面大小
            pageNo: params.offset/params.limit, //页码
            orderId:orderId!=""?orderId:undefined,
            name:name!=""?name:undefined,
            buyer:buyer!=""?buyer:undefined,
            tel:buyer!=""?buyer:undefined,
            orderStateEnum:orderStateEnum>0?orderStateEnum:undefined,
            orderDate:orderDates[0]!=""?orderDates[0]:undefined,
            endOrderDate:orderDates[1]!=""?orderDates[1]:undefined,
            payDate:payDate[0]!=""?payDate[0]:undefined,
            endPayDate:payDate[1]!=""?payDate[1]:undefined,
            touristDate:touristDate[0]!=""?touristDate[0]:undefined,
            endTouristDate:touristDate[1]!=""?touristDate[1]:undefined
        };
        //console.log(temp);
        return temp;
    };

