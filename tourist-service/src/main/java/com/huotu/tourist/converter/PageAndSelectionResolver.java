/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.tourist.model.PageAndSelection;
import com.huotu.tourist.model.Selection;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CJ
 */
public class PageAndSelectionResolver implements HandlerMethodReturnValueHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest) throws Exception {
        PageAndSelection pageAndSelection = (PageAndSelection) returnValue;

        Map<String, Object> json = new HashMap<>();
        json.put("total", pageAndSelection.getPage().getTotalElements());
        List<Object> rows = new ArrayList<>();
        for (Object data : pageAndSelection.getPage()) {
            Map<String, Object> row = new HashMap<>();
            for (Object selectionObject : pageAndSelection.getSelectionList()) {
                Selection selection = (Selection) selectionObject;
                row.put(selection.getName(), selection.apply(data));
            }
            rows.add(row);
        }
        json.put("rows", rows);

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        objectMapper.writeValue(response.getOutputStream(), json);
        mavContainer.setRequestHandled(true);

    }
}
