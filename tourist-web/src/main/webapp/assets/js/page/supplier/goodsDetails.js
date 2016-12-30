/**
 * Created by Administrator slt 2016/12/30.
 */
$(function(){
    dateRangeEdit($('input[name="toursitDate"]'));
    bindDelteTouristDate();

});

var dateRangeEdit=function($ele){
    $ele.daterangepicker(
        {
            singleDatePicker: true,
            showDropdowns: true

        }
    );
};

var addTouristDate=function(){
    $(".goodsTouristDates").append('<div> <input type="hidden" name="routeId"/> ' +
        '<input  type="text" class="form-control datePicker"' +
        ' name="toursitDate" placeholder="出行时间"/> ' +
        '<button type="button" class="btn-lit">删除</button> </div>');

    dateRangeEdit($('input[name="toursitDate"]'));

};

var bindDelteTouristDate=function(){
    $(".goodsTouristDates").on("click","button",function(){

        var div=$(this).parent();
        if(!checkStringIsEmpty($("input[name='routeId'][type='hidden']",div).val())){
            //请求服务器删除行程
            layer.confirm('确定删除吗？', {
                btn: ['确定', '取消']
            }, function (index) {
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

                layer.close(index);
            });
        }else {
            $(div).remove();
        }

    });
};


var checkStringIsEmpty=function(str){
    if(str==undefined||str==null||str==""){
        return true;
    }else {
        return false;
    }
};