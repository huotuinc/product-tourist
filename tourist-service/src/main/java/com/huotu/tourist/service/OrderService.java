/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;

import java.net.URL;
import java.util.function.Consumer;

/**
 * @author CJ
 */
public interface OrderService {

    // 建立订单

    /**
     * 用户意图建立订单
     *
     * @param good    用户选择的线路
     * @param success 成功回调
     * @param failed  用户取消订单的回调（可能永远无法被调用）
     * @return 即将转发给用户的URL
     */
    URL startOrder(TouristGood good, Consumer<TouristOrder> success, Consumer<String> failed);

}
