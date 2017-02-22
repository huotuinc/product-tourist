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
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.login.SystemUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
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
     * 订单列表
     *
     * @param supplier
     * @param supplierName   当supplier为null时该参数有效，反之无效
     * @param orderNo        订单ID
     * @param touristName    路线名称
     * @param buyerName      购买人
     * @param tel            购买人电话
     * @param payTypeEnum    付款状态
     * @param orderDate      下单时间
     * @param endOrderDate   结束下单时间
     * @param payDate        支付时间
     * @param endPayDate     结束支付时间
     * @param touristDate    出行时间
     * @param endTouristDate 结束出行时间
     * @param orderStateEnum 订单状态
     * @param settlement     是否结算
     * @param pageable       分页信息(必须)
     * @param settlementId
     * @return 返回带分页信息的订单列表
     * @throws IOException 获取订单列表发生错误
     */
    Page<TouristOrder> touristOrders(TouristSupplier supplier, String supplierName, String orderNo, String touristName
            , String buyerName, String tel, PayTypeEnum payTypeEnum, LocalDateTime orderDate
            , LocalDateTime endOrderDate, LocalDateTime payDate, LocalDateTime endPayDate, LocalDateTime touristDate
            , LocalDateTime endTouristDate, OrderStateEnum orderStateEnum, Boolean settlement
            , Pageable pageable, Long settlementId) throws IOException;


    /**
     * 计算某供应商订单总金额
     *
     * @param supplierId 供应商ID(必须)
     * @return 总金额
     * @throws IOException
     */
    BigDecimal countMoneyTotal(Long supplierId) throws IOException;

    /**
     * 计算某供应商已支付的订单总额(订单状态包括：已支付，已确认，已完成，退款中)
     *
     * @param supplierId
     * @return
     * @throws IOException
     */
    BigDecimal countMoneyPayFinish(Long supplierId) throws IOException;


    /**
     * 计算订单金额
     *
     * @param supplier      供应商
     * @param orderState    单个订单状态
     * @param createDate    大于的创建时间
     * @param endCreateDate 小于的创建时间
     * @param settlement    是否结算
     * @param touristGood   线路商品
     * @param touristBuyer  购买人
     * @param orderStates   订单状态集合(与orderState参数最好不要同时使用)
     * @return
     * @throws IOException
     */
    @Transactional(readOnly = true)
    BigDecimal countOrderTotalMoney(TouristSupplier supplier, OrderStateEnum orderState, LocalDateTime createDate
            , LocalDateTime endCreateDate, Boolean settlement, TouristGood touristGood, TouristBuyer touristBuyer, List<OrderStateEnum> orderStates)
            throws IOException;


    /**
     * 计算订单的总佣金
     *
     * @param supplier      供应商
     * @param orderState    单个订单状态
     * @param createDate    大于的创建时间
     * @param endCreateDate 小于的创建时间
     * @param settlement    是否已经结算
     * @param touristGood   线路商品
     * @param touristBuyer  采购商
     * @param orderStates   订单状态集合(避免和orderState同时使用)
     * @return
     * @throws IOException
     */
    @Transactional(readOnly = true)
    BigDecimal countOrderTotalcommission(TouristSupplier supplier, OrderStateEnum orderState, LocalDateTime createDate
            , LocalDateTime endCreateDate, Boolean settlement, TouristGood touristGood, TouristBuyer touristBuyer
            , List<OrderStateEnum> orderStates)
            throws IOException;

    /**
     * 计算某供应商总佣金
     *
     * @param supplierId 供应商ID(必须)
     * @return 总佣金
     * @throws IOException
     */
    BigDecimal countCommissionTotal(Long supplierId) throws IOException;

    /**
     * 计算某供应商已经付款的订单的总佣金(订单状态包括：已支付，已确认，已完成，退款中)
     *
     * @param supplierId
     * @return
     * @throws IOException
     */
    BigDecimal countCommissionPayFinish(Long supplierId) throws IOException;

    /**
     * 计算总退款
     *
     * @param supplierId 供应商ID(必须)
     * @return 总退款
     * @throws IOException
     */
    BigDecimal countRefundTotal(Long supplierId) throws IOException;

    /**
     * 计算总订单数
     *
     * @param supplierId 供应商ID(必须)
     * @return 总订单数
     * @throws IOException
     */
    long countOrderTotal(Long supplierId) throws IOException;

    /**
     * 判断订单的状态是否可以被修改,详情请看讨论组共享文件 “行装.doc”
     *
     * @param user         当前用户权限，如果是null，则看作系统
     * @param formerStatus 先前的订单状态
     * @param laterStatus  修改的订单状态
     * @return 是否可以被修改
     */
    boolean checkOrderStatusCanBeModified(SystemUser user, OrderStateEnum formerStatus, OrderStateEnum laterStatus);

    /**
     * 获取所给订单能够修改的状态
     *
     * @param user         当前用户权限，如果是null，则看作系统
     * @param touristOrder 订单
     * @return
     */
    List<OrderStateEnum> getModifyState(SystemUser user, TouristOrder touristOrder);

    /**
     * 添加订单，添加游客，商城积分同步，商城订单同步
     *
     * @param user
     * @param travelers
     * @param goodId
     * @param routeId
     * @param mallIntegral
     * @param mallBalance
     * @param mallCoffers
     * @param remark
     * @return null 游客人数不足，not null 订单
     * @throws IOException 游客不能为空，IllegalStateException 积分同步失败
     */
    @Transactional
    TouristOrder addOrderInfo(TouristBuyer user, List<Traveler> travelers, Long goodId, Long routeId, Float mallIntegral
            , Float mallBalance, Float mallCoffers, String remark) throws IOException, IllegalStateException;


    /**
     * 获取采购商的订单列表
     *
     * @param buyerId 采购商
     * @param lastId  最后一条订单的ID，用于查询条件
     * @param states  订单状态：all:全部，going:进行中，finish：已完成，cancel:已取消
     * @return
     * @throws IOException
     */
    List<TouristOrder> getBuyerOrders(Long buyerId, Long lastId, String states) throws IOException;

    /**
     * 定时调度取消未支付的订单
     */
    void scheduledCancelOrder();
}
