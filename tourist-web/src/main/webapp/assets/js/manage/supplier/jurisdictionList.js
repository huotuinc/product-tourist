/**
 * Created by slt  2016/12/26.
 */
$(function(){
});

var actionFormatter = function (value, row, index) {
    return  '<button class="btn btn-link showDetail">查看</button>' +
        '<button class="btn btn-link recycle">删除</button>';
};

var actionEvents = {
    'click .showDetail': function (e, value, row, index) {
        var id=row.id;
        location.href=detailUrl+"?id="+id;
    },
    'click .recycle': function (e, value, row, index) {
        var id=row.id;
        layer.confirm('确定删除吗？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            //ajax异步回收
            $.ajax({
                type:'POST',
                url: '/supplier/delSupplierOperator',
                dataType: 'text',
                data: {id:id},
                success:function(result){
                    layer.msg("删除成功！");
                    $("table").bootstrapTable('refresh');
                },
                error:function(e){
                    layer.msg("删除失败！");
                }
            });


            layer.msg('修改成功', {icon: 1});

        }, function(index){
            layer.close(index);
        });

    }
};

var getParams= function(params) {
    var temp = {
        pageSize: params.limit, //页面大小
        pageNo: params.offset/params.limit //页码
    };
    return temp;
};

