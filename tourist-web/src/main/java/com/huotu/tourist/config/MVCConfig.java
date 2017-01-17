/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.config;


import com.huotu.tourist.TouristRouteResolver;
import com.huotu.tourist.converter.AutowireConverter;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.converter.PageAndSelectionResolver;
import com.huotu.tourist.core.ServiceConfig;
import com.huotu.tourist.util.ArrayUtil;
import com.huotu.tourist.util.TravelerListResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebMvc
@ComponentScan({
        "com.huotu.tourist.controller",
//        "com.huotu.tourist.interceptor",
        "com.huotu.tourist.converter"
})
@Import({MVCConfig.MVCConfigLoader.class, ServiceConfig.class, SecurityConfig.class})
public class MVCConfig extends WebMvcConfigurerAdapter {

    private static final String UTF8 = "UTF-8";
    @Autowired
    PageAndSelectionResolver pageAndSelectionResolver;
    private String pageParameterName = "pageNo";
    private String sizeParameterName = "pageSize";
    @Autowired
    private ThymeleafViewResolver htmlViewResolver;
    @Autowired
    private ThymeleafViewResolver javascriptViewResolver;
    @Autowired
    private ThymeleafViewResolver cssViewResolver;
    @Autowired
    private LocalDateTimeFormatter localDateTimeFormatter;
    @Autowired
    private Set<AutowireConverter> commonEnumConverterSet;

    /**
     * for upload
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(pageAndSelectionResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatterForFieldType(LocalDateTime.class, localDateTimeFormatter);
        commonEnumConverterSet.forEach(registry::addConverter);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/assets/**","/_resources/**").addResourceLocations("/assets/","/_resources/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        PageableHandlerMethodArgumentResolver resolver=
                new PageableHandlerMethodArgumentResolver(new SortHandlerMethodArgumentResolver());
        resolver.setPageParameterName(pageParameterName);
        resolver.setSizeParameterName(sizeParameterName);
        argumentResolvers.add(resolver);
        argumentResolvers.add(new TouristRouteResolver());
        argumentResolvers.add(new TravelerListResolver());
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(htmlViewResolver);
        registry.viewResolver(javascriptViewResolver);
        registry.viewResolver(cssViewResolver);
        registry.viewResolver(redirectViewResolver());
        registry.viewResolver(forwardViewResolver());

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(routeInterceptor);
    }

    private ViewResolver redirectViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewNames(ArrayUtil.array("redirect:*"));
        return resolver;
    }

    private ViewResolver forwardViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewNames(ArrayUtil.array("forward:*"));
        return resolver;
    }

    @Import(MVCConfigLoader.EngineLoader.class)
    static class MVCConfigLoader {

        @Autowired
        private SpringTemplateEngine javascriptTemplateEngine;
        @Autowired
        private SpringTemplateEngine cssTemplateEngine;

        @Autowired
        private SpringTemplateEngine htmlViewTemplateEngine;

        @Autowired
        public void setTemplateEngineSet(Set<SpringTemplateEngine> templateEngineSet) {
            // 所有都增加安全方言
            templateEngineSet.forEach(engine -> engine.addDialect(new SpringSecurityDialect()));
        }

        @Bean
        public ThymeleafViewResolver htmlViewResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(htmlViewTemplateEngine);
            resolver.setContentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8");
            resolver.setCharacterEncoding(UTF8);
            resolver.setCache(false);
            resolver.setViewNames(ArrayUtil.array("*.html"));
            return resolver;
        }

        @Bean
        public ThymeleafViewResolver javascriptViewResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(javascriptTemplateEngine);
            resolver.setContentType("application/javascript");
            resolver.setCharacterEncoding(UTF8);
            resolver.setViewNames(ArrayUtil.array("*.js"));
            return resolver;
        }

        @Bean
        public ThymeleafViewResolver cssViewResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(cssTemplateEngine);
            resolver.setContentType("text/css");
            resolver.setCharacterEncoding(UTF8);
            resolver.setViewNames(ArrayUtil.array("*.css"));
            return resolver;
        }

        static class EngineLoader {

            @Autowired
            private ApplicationContext applicationContext;
            @Autowired
            private Set<IDialect> dialectSet;
            @Autowired
            private Environment environment;


            SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
                SpringTemplateEngine engine = new SpringTemplateEngine();
                engine.setTemplateResolver(templateResolver);
                engine.addDialect(new Java8TimeDialect());
                dialectSet.forEach(engine::addDialect);
                return engine;
            }


            private ITemplateResolver htmlTemplateResolver() {
                SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
                resolver.setCacheable(!environment.acceptsProfiles("development")
                        && !environment.acceptsProfiles("test"));
                resolver.setCharacterEncoding(UTF8);
                resolver.setApplicationContext(applicationContext);
                resolver.setTemplateMode(TemplateMode.HTML);
                return resolver;
            }

            private ITemplateResolver javascriptTemplateResolver() {
                SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
                resolver.setCacheable(!environment.acceptsProfiles("development")
                        && !environment.acceptsProfiles("test"));
                resolver.setCharacterEncoding(UTF8);
                resolver.setApplicationContext(applicationContext);
                resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
                return resolver;
            }

            private ITemplateResolver cssTemplateResolver() {
                SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
                resolver.setCacheable(!environment.acceptsProfiles("development")
                        && !environment.acceptsProfiles("test"));
                resolver.setCharacterEncoding(UTF8);
                resolver.setApplicationContext(applicationContext);
                resolver.setTemplateMode(TemplateMode.CSS);
                return resolver;
            }

            @Bean
            public SpringTemplateEngine htmlViewTemplateEngine() {
                return templateEngine(htmlTemplateResolver());
            }

            @Bean
            public SpringTemplateEngine javascriptTemplateEngine() {
                return templateEngine(javascriptTemplateResolver());
            }

            @Bean
            public SpringTemplateEngine cssTemplateEngine() {
                return templateEngine(cssTemplateResolver());
            }

        }
    }


}
