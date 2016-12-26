/**
 * Created by Administrator xhl 2015/12/23.
 */
define(function (require, exports, module) {
    $("#addModelForm").validate({
        rules: {
            supplierName: {
                required: true,
                maxlength: 50
            },
            adminAccount: {
                required: true,
                maxlength: 50
            },
            adminPassword: {
                required: true,
                maxlength: 50
            },
            contacts: {
                required: true,
                maxlength: 20
            },
            contactNumber: {
                required: true,
                maxlength: 15
            },
            businessLicenseUri: {
                required: true,
            },

            detailedAddress: {
                required: true
            },
            province: {
                selrequired: "-1"
            },
            town: {
                selrequired: "-1"
            },
            district: {
                selrequired: "-1"
            },
            remarks: {
                maxlength: 200
            }
        },
        messages: {
            supplierName: {
                required: "供应商名称为必输项",
                maxlength: "长度不能超过50个字符"
            },
            adminAccount: {
                required: "管理员账户为必输项",
                maxlength: "长度不能超过50个字符"
            },
            adminPassword: {
                required: "管理员密码为必输项",
                maxlength: "长度不能超过50个字符"
            },
            contacts: {
                required: "联系人为必输项",
                maxlength: "长度不能超过20个字符"
            },
            contactNumber: {
                required: "联系电话为必输项",
                maxlength: "长度不能超过50个字符"
            },
            businessLicenseUri: {
                required: "营业执照为必输项"
            },
            detailedAddress: {
                required: "详细地址必输项"
            },
            province: {
                selrequired: "请选择省"
            },
            town: {
                selrequired: "请选择市"
            },
            district: {
                selrequired: "请选择区"
            },
            remarks: {
                maxlength: "长度不能超过200个字符"
            }

        },
        submitHandler: function (form, ev) {
            var commonUtil = require("common");
            commonUtil.setDisabled("jq-cms-Save");
            $("input[name='address']").val($("select[name='province']").val()
                + "," + $("select[name='town']").val() + ","
                + $("select[name='district']").val() + "," + $("input[name='detailedAddress']").val());
            var layer = require("layer");
            layer.msg("操作成功", {time: 2000});
            commonUtil.cancelDisabled("jq-cms-Save");
            form.submit();
        },
        invalidHandler: function () {
            return true;
        }
    });
});
