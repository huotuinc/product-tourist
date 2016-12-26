/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.login.SystemUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * @author CJ
 */
public interface TouristOrderService {

    /**
     * 用户意图建立订单
     *
     * @param good    用户选择的线路
     * @param success 成功回调
     * @param failed  用户取消订单的回调（可能永远无法被调用）
     * @return 即将转发给用户的URL
     */
    URL startOrder(TouristGood good, Consumer<TouristOrder> success, Consumer<String> failed);



    /**
     * 订单列表，
     * @param supplier
     * @param orderId        订单ID
     * @param touristName    路线名称
     * @param buyerName      购买人
     * @param tel            购买人电话
     * @param payTypeEnum    付款状态
     * @param orderDate      下单时间
     * @param endOrderDate   结束下单时间
     * @param payDate        支付时间
     * @param endPayDate     结束支付时间
     * @param touristDate    出行时间
     * @param orderStateEnum 结算状态
     * @param pageable       分页信息(必须)
     * @return 返回带分页信息的订单列表
     * @throws IOException 获取订单列表发生错误
     */
    Page<TouristOrder> touristOrders(TouristSupplier supplier, String orderId, String touristName, String buyerName, String tel
            , PayTypeEnum payTypeEnum, LocalDate orderDate, LocalDate endOrderDate, LocalDate payDate
            , LocalDate endPayDate, LocalDate touristDate, OrderStateEnum orderStateEnum, Pageable pageable) throws IOException;


    /**
     * 计算订单总金额
     * @param supplierId  供应商ID(必须)
     * @return            总金额
     * @throws IOException
     */
    long countMoneyTotal(Long supplierId) throws IOException;

    /**
     * 计算总佣金
     * @param supplierId    供应商ID(必须)
     * @return              总佣金
     * @throws IOException
     */
    long countCommissionTotal(Long supplierId) throws IOException;

    /**
     * 计算总退款
     * @param supplierId    供应商ID(必须)
     * @return              总退款
     * @throws IOException
     */
    long countRefundTotal(Long supplierId) throws IOException;

    /**
     * 计算总订单数
     * @param supplierId    供应商ID(必须)
     * @return              总订单数
     * @throws IOException
     */
    long countOrderTotal(Long supplierId) throws IOException;

    /**
     * 判断订单的状态是否可以被修改,详情请看讨论组共享文件 “行装.doc”
     * @param user          当前用户权限，如果是null，则看作系统
     * @param formerStatus  先前的订单状态
     * @param laterStatus   修改的订单状态
     * @return              是否可以被修改
     */
    boolean checkOrderStatusCanBeModified(SystemUser user,OrderStateEnum formerStatus,OrderStateEnum laterStatus);



}
