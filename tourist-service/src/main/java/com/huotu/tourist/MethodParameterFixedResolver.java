/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

/**
 * 这种一种丑陋的解决方案,但至少解决了。
 *
 * @author CJ
 */
public class MethodParameterFixedResolver implements HandlerMethodArgumentResolver {

    private final RequestParamMethodArgumentResolver requestParamMethodArgumentResolver
            = new RequestParamMethodArgumentResolver(true);
    private final ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor
            = new ServletModelAttributeMethodProcessor(true);
    private final ServletRequestMethodArgumentResolver servletRequestMethodArgumentResolver
            = new ServletRequestMethodArgumentResolver();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MethodParameterFixed.class)
                && (requestParamMethodArgumentResolver.supportsParameter(parameter)
                || servletModelAttributeMethodProcessor.supportsParameter(parameter)
                || servletRequestMethodArgumentResolver.supportsParameter(parameter));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (servletRequestMethodArgumentResolver.supportsParameter(parameter))
            return servletRequestMethodArgumentResolver.resolveArgument(parameter, mavContainer, webRequest
                    , binderFactory);
        Class clazz = parameter.getParameterType();
        if (Number.class.isAssignableFrom(clazz))
            return requestParamMethodArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (clazz == String.class)
            return requestParamMethodArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        // others
        return servletModelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}
