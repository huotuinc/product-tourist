/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.repository.TouristOrderRepository;
import me.jiangcai.dating.ServiceBaseTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author CJ
 */

public class OrderServiceTest extends ServiceBaseTest {

    private static final Log log = LogFactory.getLog(OrderServiceTest.class);
    @Autowired
    TouristOrderRepository touristOrderRepository;

    @Test
    public void runnableTest() {
        log.info("sadfasdf");
    }


}