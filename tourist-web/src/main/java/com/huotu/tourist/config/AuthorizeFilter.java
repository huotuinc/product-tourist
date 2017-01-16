/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.tourist.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.*;
import java.io.IOException;

/**
 * 前端采购商自动认证过滤器
 *
 * @author slt
 */
public class AuthorizeFilter implements Filter {

    private static final Log log = LogFactory.getLog(AuthorizeFilter.class);

    private ServletContext servletContext;
    private ApplicationContext applicationContext;
    private SecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthorizeFilter(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public AuthorizeFilter() {
        this(null);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("AuthorizeFilter Init.");
        servletContext = filterConfig.getServletContext();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
