/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.config;


import com.huotu.tourist.ServiceConfig;
import com.huotu.tourist.converter.DateFormatter;
import com.huotu.tourist.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Set;

@Configuration
@EnableWebMvc
@ComponentScan({
        "com.huotu.tourist.controller",
        "com.huotu.tourist.interceptor",
})
@Import({MVCConfig.MVCConfigLoader.class, ServiceConfig.class})
public class MVCConfig extends WebMvcConfigurerAdapter {

    private static final String UTF8 = "UTF-8";
    @Autowired
    private ThymeleafViewResolver htmlViewResolver;
    @Autowired
    private ThymeleafViewResolver javascriptViewResolver;
    @Autowired
    private ThymeleafViewResolver cssViewResolver;

    @Autowired
    private DateFormatter dateFormatter;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(dateFormatter);
    }

    /**
     * 允许访问静态资源
     * NO 不再允许 JiangCai
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
//        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
//        registry.addResourceHandler("/_resources/**").addResourceLocations("/_resources/");
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
