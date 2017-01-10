//// define(function (require, exports, module) {
//$("#modelForm").validate({
//    rules: {
//        aliPayName: {
//            required: true,
//            maxlength: 50
//        },
//        accountName: {
//            required: true,
//            maxlength: 50
//        },
//        aliPayAccount: {
//            required: true,
//            maxlength: 20
//        },
//        bank: {
//            required: true,
//            maxlength: 100
//        },
//        bankBranch: {
//            required: true,
//            maxlength: 100
//        },
//        IDCard: {
//            required: true,
//            maxlength:18
//        },
//
//        bankCard: {
//            required: true,
//            maxlength:20
//        }
//    },
//    messages: {
//        aliPayName: {
//            required: "供应商名称为必输项",
//            maxlength: "长度不能超过50个字符"
//        },
//        adminAccount: {
//            required: "管理员账户为必输项",
//            maxlength: "长度不能超过50个字符"
//        },
//        adminPassword: {
//            required: "管理员密码为必输项",
//            maxlength: "长度不能超过50个字符"
//        },
//        contacts: {
//            required: "联系人为必输项",
//            maxlength: "长度不能超过20个字符"
//        },
//        contactNumber: {
//            required: "联系电话为必输项",
//            maxlength: "长度不能超过50个字符"
//        },
//        businessLicenseUri: {
//            required: "营业执照为必输项"
//        },
//        detailedAddress: {
//            required: "详细地址必输项"
//        },
//        address: {
//            selrequired: "请选择城市"
//        },
//        remarks: {
//            maxlength: "长度不能超过200个字符"
//        }
//
//    },
//    submitHandler: function (form, ev) {
//        var commonUtil = require("common");
//        commonUtil.setDisabled("jq-cms-Save");
//
//        var layer = require("layer");
//        layer.msg("操作成功", {time: 2000});
//        commonUtil.cancelDisabled("jq-cms-Save");
//        form.submit();
//
//    },
//    invalidHandler: function () {
//        return true;
//    }
//});
//
////// uploader
////parent.uploader($('#supplier-uploader'), function (path) {
////    $("#businessLicenseUri").val(path);
////}, {
////    allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
////    itemLimit: 1,
////    sizeLimit: 3 * 1024 * 1024
////});
//
//
//// });
