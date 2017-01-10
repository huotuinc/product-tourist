/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 该服务将建立与伙伴商城的关联
 *
 * @author CJ
 */
public interface ConnectMallService {

    /**
     * 将本地商品与远程商品同步
     * 并设置当前商品的商城商品id
     *
     * @param touristGoodId 本地商品id
     * @return 已更新好的本地商品
     */
    @Transactional
    TouristGood pushGoodToMall(long touristGoodId) throws IOException;

    /**
     * 订单状态是否正常（我们只会认可唯一的一种状态）
     *
     * @param order 订单
     * @return true 表示这个订单可以进行结算
     * @throws IOException
     */
    boolean statusNormal(TouristOrder order) throws IOException;
}
