// define(function (require, exports, module) {
$("#modelForm").validate({
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
                required: true
            },

            detailedAddress: {
                required: true
            },
            address: {
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
            address: {
                selrequired: "请选择城市"
            },
            remarks: {
                maxlength: "长度不能超过200个字符"
            }

        },
        submitHandler: function (form, ev) {
            form.submit();
        },
        invalidHandler: function () {
            return true;
        }
    });

// uploader
parent.uploader($('#supplier-uploader'), function (path) {
    $("#businessLicenseUri").val(path);
}, {
    allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
    itemLimit: 1,
    sizeLimit: 3 * 1024 * 1024
});

// });
