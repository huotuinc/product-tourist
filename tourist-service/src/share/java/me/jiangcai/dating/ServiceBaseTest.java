/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package me.jiangcai.dating;

import com.huotu.tourist.core.ServiceConfig;
import me.jiangcai.lib.resource.service.ResourceService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, ServiceConfig.class})
public abstract class ServiceBaseTest extends SpringWebTest {

    @Autowired
    protected ResourceService resourceService;
    @Autowired
    protected ApplicationContext applicationContext;

    public String randomBankCard() {
        return RandomStringUtils.randomNumeric(16);
    }

    public MockMvc mockMVC() {
        return mockMvc;
    }

    /**
     * @return 随机生成的图片资源路径
     */
    protected String randomImageResourcePath() throws IOException {
        String name = "tmp/" + UUID.randomUUID().toString() + ".png";
        resourceService.uploadResource(name, applicationContext.getResource("/images/1.png").getInputStream());
        return name;
    }

    /**
     * @return 随机身份证
     */
    protected String randomPeopleId() {
        // 8 + 4
        return "33032419831021"
                + org.apache.commons.lang.RandomStringUtils.randomNumeric(4);
    }

    public static class RandomComparator implements Comparator<Object> {
        static Random random = new Random();

        @Override
        public int compare(Object o1, Object o2) {
            return random.nextInt();
        }
    }
}
