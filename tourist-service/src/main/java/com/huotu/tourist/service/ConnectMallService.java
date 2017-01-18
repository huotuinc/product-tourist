/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.exception.NotLoginYetException;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 该服务将建立与伙伴商城的关联
 *
 * @author CJ
 */
public interface ConnectMallService {

    /**
     * @return 积分转换为余额的汇率
     */
    int getExchangeRate();

    /**
     * 用于决策T+1
     *
     * @return 售后时间（天）
     */
    long getServiceDays();

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

    /**
     * 订单状态是否正常（我们只会认可唯一的一种状态）
     *
     * @param mallTradeId 商城订单号
     * @return true 表示这个订单可以被认可为已支付
     * @throws IOException
     */
    boolean statusNormal(String mallTradeId) throws IOException;


    /**
     * 获取用户的账户信息
     *
     * @param mallUserId 商城用户小伙伴id
     * @return
     * @throws IOException 获取信息失败
     */
    Map getUserDetailByUserId(Long mallUserId) throws IOException;

    /**
     * 推订单到商城
     *
     * @param order 订单信息
     * @return
     * @throws IOException 同步商城订单出错
     */
    String pushOrderToMall(TouristOrder order) throws IOException;


    /**
     * 建议使用{@link #getUserDetailByUserId(Long)}
     *
     * @param buyer 采购商
     * @return
     */
    @Deprecated
    String getTouristBuyerHeadUrl(TouristBuyer buyer);

    /**
     * @param request 当前请求
     * @return 用户id, 这个值应该被赋予到 {@link TouristBuyer#id}
     * @throws NotLoginYetException 尚未登录的话
     */
    long currentUserId(HttpServletRequest request) throws NotLoginYetException;
}
