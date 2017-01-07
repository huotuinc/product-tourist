/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.TouristRoute;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  保存行程
 * @author slt
 */
public class TouristRouteResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return TouristRoute[].class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        List<TouristRoute> routes=new ArrayList<>();
        String routesStr=webRequest.getParameter("routes");
        ObjectMapper objectMapper=new ObjectMapper();
        List<LinkedHashMap<String, Object>> list=objectMapper.readValue(routesStr,List.class);
        for(LinkedHashMap<String, Object> l:list){
            TouristRoute touristRoute=new TouristRoute();
            String routeNo=l.get("routeNo")==null?null:l.get("routeNo").toString();
            LocalDateTime formDate= LocalDateTimeFormatter.toLocalDateTime(
                    l.get("fromDate")==null?null:l.get("fromDate").toString());
            LocalDateTime toDate= LocalDateTimeFormatter.toLocalDateTime(
                    l.get("toDate")==null?null:l.get("toDate").toString());
            int maxPeople= StringUtils.isEmpty(l.get("maxPeople"))?0:Integer.valueOf(l.get("maxPeople").toString());
            touristRoute.setMaxPeople(maxPeople);
            touristRoute.setRouteNo(routeNo);
            touristRoute.setFromDate(formDate);
            touristRoute.setToDate(toDate);
            routes.add(touristRoute);
        }
        return routes.toArray(new TouristRoute[routes.size()]);

    }
}
