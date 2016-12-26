seajs.config({
    alias: {
        "jquery": "//cdn.bootcss.com/jquery/3.1.1/jquery.min.js",
        "bootstrap": "http://resali.huobanplus.com/cdn/bootstrap/3.3.7/bootstrap.min.js",
        "validate": "libs/validate/jquery.validate.min.js",
        "message": "libs/validate/jquery.validate.addMethod.js",
        "common": "js/page/common.js?t=552225",
        "main": "js/page/main.js",
        "home": "js/page/home.js",
        "JGrid": "libs/JGrid/jquery.JGrid.js",
        "bootstrap-table.min": "//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.0/bootstrap-table.min.js",
        "bootstrap-table-zh-CN": "//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.0/locale/bootstrap-table-zh-CN.min.js",
        "layer": "libs/layer/layer.js",
        "supplierList": "js/page/platform/supplierList.js",
        "addSupplier": "js/page/platform/addSupplier.js",

        //供应商
        "goodsDetails.js": "js/page/supplier/goodsDetails.js",
        "goodsList.js": "js/page/supplier/goodsList.js",
        "orderDetails.js": "js/page/supplier/orderDetails.js",
        "orderList.js": "js/page/supplier/orderList.js",
        "payeeAccountDetails.js": "js/page/supplier/payeeAccountDetails.js",
        "salesStatistics.js": "js/page/supplier/salesStatistics.js",
        "supplierDetails.js": "js/page/supplier/supplierDetails.js",
        //供应商END
    },
    preload: ['jquery']
});