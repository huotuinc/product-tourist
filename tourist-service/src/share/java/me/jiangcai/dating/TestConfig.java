/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package me.jiangcai.dating;

import com.huotu.tourist.model.VerificationType;
import com.huotu.tourist.service.VerificationCodeService;
import com.huotu.tourist.service.impl.AbstractVerificationCodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

/**
 * @author CJ
 */
@Import({TestConfig.Config.class, DSConfig.class})
@ImportResource("classpath:/datasource_local.xml")
@ComponentScan("me.jiangcai.dating.mock")
class TestConfig {

    static class Config {

        @Bean
        @Primary
        public VerificationCodeService verificationCodeService() {
            // 1234 always work
            return new AbstractVerificationCodeService() {
                @Override
                protected void send(String to, String content) throws IOException {
                    System.err.println("Send Code " + content + " to " + to);
                }

                @Override
                protected String generateCode(String mobile, VerificationType type) {
                    return "1234";
                }
            };
        }

    }

}
