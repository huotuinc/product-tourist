/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 目前还没有提供采购商的认证
 *
 * @author CJ
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@DependsOn("loginService")
//需要依赖于userDetailsService 强制userDetailsService优先加载。
class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    // Since MultiSecurityConfig does not extend GlobalMethodSecurityConfiguration and
    // define an AuthenticationManager, it will try using the globally defined
    // AuthenticationManagerBuilder to create one

    // The @Enable*Security annotations create a global AuthenticationManagerBuilder
    // that can optionally be used for creating an AuthenticationManager that is shared
    // The key to using it is to use the @Autowired annotation
    @Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @EnableWebSecurity
    @Configuration
    public static class ClassicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private Environment environment;
        // Since we didn't specify an AuthenticationManager for this class,
        // the global instance is used

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);

            web.ignoring()
                    .antMatchers(
                            // 安全系统无关的uri
//                            mvcConfig.staticResourceAntPatterns()
                    );
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
//            super.configure(http);

            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry
                    = http.authorizeRequests();

            // 2 个点  登录时 具体请求的页面应该是什么; 2 登录的处理者

            registry
                    .anyRequest()
                    .permitAll()
                    .and().csrf().disable()
                    .formLogin()
//                .failureHandler()
                    .loginProcessingUrl("/auth")
                    .loginPage("/login")//
                    .permitAll()
                    .and()
                    .logout().logoutUrl("/logout").permitAll();

        }
    }
}
