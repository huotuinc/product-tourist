/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 错误处理
 *
 * @author slt
 */
@Controller
public class ErrorController {
    /**
     * 错误处理
     *
     * @param code        错误代码
     * @param message     错误消息
     * @param description 错误描述
     * @param path        请求的路径
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/error")
    public String error(String path, Integer code, String message, String description, Model model)
            throws Exception {
        model.addAttribute("code", code);
        model.addAttribute("errorMsg", message);
        model.addAttribute("description", description);
        if (path.startsWith("/wap/")) {
            return "view/wap/errorMsg.html";
        }
        return "view/manage/error.html";
    }

}
