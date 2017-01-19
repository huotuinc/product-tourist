/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({
        "com.huotu.tourist.converter"
        , "com.huotu.tourist.service"
})
@EnableJpaRepositories("com.huotu.tourist.repository")
@ImportResource(value = {"classpath:spring-jpa.xml"})
@Import({CommonConfig.class, DataSupportConfig.class})
public class ServiceConfig {

}
