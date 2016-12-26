/**
 * Created by Administrator on 2015/12/21.
 */
define(function (require, exports, module) {
    var commonUtil = require("common");
    commonUtil.calcuHeight("mainpanel");
    commonUtil.calcuHeightToTop("con_right", 74);
    commonUtil.calcuWidth("content", $(".leftpanel").width());
    commonUtil.calcuWidth("con_right", $(".leftpanel").width());

    //TODO:左边栏目效果
    var menusObj = $(".js-cms-menus");
    $.each(menusObj, function (item, dom) {
        $(dom).find(".aparent").click(function () {
            if ($(dom).find("ul").hasClass('hidden')) {
                $(dom).removeClass("nav-active")
                $(dom).find("ul").removeClass("hidden");
                //$(dom).find("ul").addClass("hidden")
            }
            else {
                //$(dom).find("ul").removeClass("hidden")
                $(dom).addClass("nav-active");
                $(dom).find("ul").addClass("hidden");
            }
        });
    })

    /**
     * 使用该方法需要在iframe页面中 初始化$.uploaderUrl 如果是原型环境该url需为null
     * 同时也需要存在id为qq-template的上传模板
     * @param ui 需要变成uploader的jquery集合
     * @param uploadedPathConsumer CMS的uploader会在响应中给予path,这就是上传以后的path;原型环境也会杜撰一个path执行
     * @param validation 是否允许该文件的的校验 http://docs.fineuploader.com/branch/master/api/options.html#validation
     * @param otherConfig 其他配置比如 http://docs.fineuploader.com/branch/master/api/options.html#session
     */
    uploader = function (ui, uploadedPathConsumer, validation, otherConfig) {
        if (ui.length == 0)
            return;
        var request;
        if (!parent.uploaderUrl) {
            request = {};
        } else {
            request = {
                inputName: 'file',
                endpoint: parent.uploaderUrl
            };
        }
        otherConfig = otherConfig || {};
        var config = $.extend(true, {
            template: parent.$('#qq-template').get(0),
            request: request,
            thumbnails: {
                placeholders: {
                    waitingPath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/waiting-generic.png',
                    notAvailablePath: 'http://resali.huobanplus.com/cdn/jquery-fine-uploader/5.10.0/placeholders/not_available-generic.png'
                }
            },
            callbacks: {
                onComplete: function (id, name, responseJSON) {
                    if (responseJSON.newUuid)
                        uploadedPathConsumer(responseJSON.newUuid);
                },
                onValidate: function (data, buttonContainer) {
                    if (!parent.uploaderUrl) { //原型
                        uploadedPathConsumer(data.name);
                    }
                }
            }
        }, otherConfig);
        if (validation)
            config.validation = validation;

        ui.fineUploader(config);
    }
});