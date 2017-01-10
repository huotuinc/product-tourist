/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author CJ
 */
public class ConnectMallServiceTest extends ServiceBaseTest {

    @Autowired
    private ConnectMallService connectMallService;
    @Autowired
    private GoodsRestRepository goodsRestRepository;

    @Test
    public void go() throws IOException {
//        createTouristGood()
        long id = connectMallService.pushGoodToMall(null);
        try {
            System.out.println(goodsRestRepository.getOneByPK(id).getOwner().getNickName());
        } finally {
            goodsRestRepository.deleteByPK(id);
        }

    }

}