seajs.config({
    alias: {
        "jquery": "http://resali.huobanplus.com/cdn/jquery/3.1.1/jquery.min.js",
        "bootstrap": "http://resali.huobanplus.com/cdn/bootstrap/3.3.7/bootstrap.min.js",
        "validate": "libs/validate/jquery.validate.min.js",
        "message": "libs/validate/jquery.validate.addMethod.js",
        "common": "js/page/common.js?t=552225",
        "main": "js/page/main.js",
        "home": "js/page/home.js",
        "JGrid": "libs/JGrid/jquery.JGrid.js",
        "bootstrap-table.min": "libs/bootstrap-table/bootstrap-table.min.js",
        "bootstrap-table-zh-CN": "libs/bootstrap-table/bootstrap-table-zh-CN.min.js",
        "fineUploader": "libs/fine-upload/jquery.fine-uploader.min.js",
        "layer": "libs/layer/layer.js",
        "cityPicker": "libs/cityPicker/js/city-picker.js",
        "cityPickerData": "libs/cityPicker/js/city-picker.data.js",

        //分销平台
        "supplierList": "js/page/platform/supplierList.js",
        "supplier": "js/page/platform/supplier.js",
        //分销平台END

        //供应商
        "goodsDetails.js": "js/page/supplier/goodsDetails.js",
        "goodsList.js": "js/page/supplier/goodsList.js",
        "orderDetails.js": "js/page/supplier/orderDetails.js",
        "orderList.js": "js/page/supplier/orderList.js",
        "payeeAccountDetails.js": "js/page/supplier/payeeAccountDetails.js",
        "salesStatistics.js": "js/page/supplier/salesStatistics.js",
        "supplierDetails.js": "js/page/supplier/supplierDetails.js",
        //供应商END

        //时间选择控件
        "moment.js":"http://resali.huobanplus.com/cdn/hotui/js/plugins/datetimepick/moment.js",
        "daterangepicker.js":"http://resali.huobanplus.com/cdn/hotui/js/plugins/datetimepick/daterangepicker.js",

    },
    preload: ['jquery']
});