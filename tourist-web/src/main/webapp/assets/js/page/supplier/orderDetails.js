/**
 * Created by Administrator slt 2016/12/29
 */
$(function(){
    checksOptimize();
});

var checksOptimize=function(){
    $("input[type='radio']").iCheck({
        checkboxClass: "icheckbox_square-green",
        radioClass: "iradio_square-green"
    })

};

var modifyTravelerInfo=function(obj){
    var tds=$(obj).parent().parent().children("td");
    var id=$(tds[0]).text();
    var name=$(tds[1]).text();
    var sex=$(tds[2]).text()=="男"?0:1;
    var age=$(tds[3]).text();
    var tel=$(tds[4]).text();
    var IDNo=$(tds[5]).text();
    $("input[name='name']","#modifyTravelerInfo").val(name);
    $("input[name='sex'][value='"+sex+"']","#modifyTravelerInfo").iCheck('check');
    $("input[name='age']","#modifyTravelerInfo").val(age);
    $("input[name='tel']","#modifyTravelerInfo").val(tel);
    $("input[name='IDNo']","#modifyTravelerInfo").val(IDNo);



    layer.open({
        type:1,
        title:"修改人员信息",
        btn:["确定"],
        area:["600px","360px"],
        content:$("#modifyTravelerInfo"),
        yes:function(index){
            name=$("input[name='name']","#modifyTravelerInfo").val();
            sex=$("input[name='sex']:checked","#modifyTravelerInfo").val();
            age=$("input[name='age']","#modifyTravelerInfo").val();
            tel=$("input[name='tel']","#modifyTravelerInfo").val();
            IDNo=$("input[name='IDNo']","#modifyTravelerInfo").val();
            $.ajax({
                type:'POST',
                url: '../../mock/supplier/orderDetails.json',
                dataType: 'json',
                data: {id:id,name:name,sex:sex,age:age,tel:tel,IDNo:IDNo},
                success:function(result){
                    layer.msg("保存成功！");
                    window.setTimeout("window.location=location.href",1000);
                },
                error:function(e){
                    layer.msg("保存出错！");
                    //window.setTimeout("window.location=location.href",1000);
                }
            });
            layer.close(index);
        }
    });

    console.log();

};