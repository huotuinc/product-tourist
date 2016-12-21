/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.PurchaserPaymentRecordRepository;
import com.huotu.tourist.repository.TouristBuyerRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import me.jiangcai.dating.ServiceBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 常用web测试基类
 */
@WebAppConfiguration
public abstract class WebTest extends ServiceBaseTest {

    @Autowired
    protected TouristOrderRepository touristOrderRepository;

    @Autowired
    protected TouristSupplierRepository touristSupplierRepository;

    @Autowired
    protected TouristBuyerRepository touristBuyerRepository;

    @Autowired
    protected PurchaserPaymentRecordRepository purchaserPaymentRecordRepository;

    @Autowired
    protected TouristGoodRepository touristGoodRepository;

    @Autowired
    protected TouristRouteRepository touristRouteRepository;


    /**
     * 创建一个随机的订单状态
     *
     * @return 订单状态
     */
    protected OrderStateEnum randomOrderStateEnum() {
        int orderStateNo = random.nextInt(7);
        switch (orderStateNo) {
            case 0:
                return OrderStateEnum.NotPay;
            case 1:
                return OrderStateEnum.PayFinish;
            case 2:
                return OrderStateEnum.NotFinish;
            case 3:
                return OrderStateEnum.Finish;
            case 4:
                return OrderStateEnum.Invalid;
            case 5:
                return OrderStateEnum.Refunds;
            default:
                return OrderStateEnum.RefundsFinish;
        }
    }

    /**
     * 创建一个随机的支付方式
     *
     * @return 支付方式
     */
    protected PayTypeEnum randomPayTypeEnum() {
        int payTypeNo = random.nextInt(2);
        switch (payTypeNo) {
            case 0:
                return PayTypeEnum.Alipay;
            default:
                return PayTypeEnum.WeixinPay;
        }
    }

    /**
     * 创建一个随机的支付方式
     *
     * @return 支付方式
     */
    protected BuyerCheckStateEnum randomBuyerCheckState() {
        int payTypeNo = random.nextInt(3);
        switch (payTypeNo) {
            case 0:
                return BuyerCheckStateEnum.Checking;
            case 1:
                return BuyerCheckStateEnum.CheckFinish;
            default:
                return BuyerCheckStateEnum.Frozen;
        }
    }


    /**
     * 创建当前的时间，如果状态是未支付则返回null
     *
     * @param orderStateEnum 支付状态
     * @return 当前时间
     */
    protected LocalDateTime randomLocalDateTime(OrderStateEnum orderStateEnum) {
        if (!OrderStateEnum.NotPay.equals(orderStateEnum)) {
            return LocalDateTime.now();
        } else {
            return null;
        }
    }


    /**
     * 创建一个测试线路订单
     *
     * @return 测试的线路订单
     */
    protected TouristOrder createTouristOrder(TouristGood good, TouristBuyer buyer, String orderNo
            , OrderStateEnum orderState,LocalDateTime createTime, LocalDateTime payTime, PayTypeEnum payType) {
        TouristOrder order = new TouristOrder();
        order.setTouristGood(good);
        order.setTouristBuyer(buyer);
        order.setOrderNo(orderNo);
        order.setCreateTime(createTime);
        order.setOrderState(orderState);
        order.setPayTime(payTime);
        order.setPayType(payType);

        return touristOrderRepository.saveAndFlush(order);
    }

    /**
     * 创建一个供应商
     *
     * @param supplierName
     * @return
     */
    protected TouristSupplier createTouristSupplier(String supplierName) {
        TouristSupplier supplier = new TouristSupplier();
        supplier.setSupplierName(supplierName);
        supplier.setAdminAccount(supplierName);
        supplier.setAdminPassword("1234567890");
        supplier.setCreateTime(LocalDateTime.now());
        return touristSupplierRepository.saveAndFlush(supplier);
    }

    /**
     *  创建一个线路商品
     * @param name
     * @return
     */
    protected TouristGood createTouristGood(String name){
        TouristGood touristGood=new TouristGood();
        touristGood.setTouristName(name);
        return touristGoodRepository.saveAndFlush(touristGood);
    }

    /**
     * 创建一个行程路线
     * @param routeNo
     * @param good
     * @param fromDate
     * @param toDate
     * @param maxPeople
     * @return
     */
    protected TouristRoute createTouristRoute(String routeNo,TouristGood good,LocalDate fromDate,LocalDate toDate,int maxPeople){
        TouristRoute touristRoute=new TouristRoute();
        touristRoute.setRouteNo(routeNo);
        touristRoute.setGood(good);
        touristRoute.setFromDate(fromDate);
        touristRoute.setToDate(toDate);
        touristRoute.setMaxPeople(maxPeople);
        return touristRouteRepository.saveAndFlush(touristRoute);
    }

    /**
     * 创建一个采购商
     *
     * @param buyerName       名称
     * @param buyerDirector   负责人
     * @param telPhone        电话
     * @param buyerCheckState 审核状态
     * @return
     */
    protected TouristBuyer createTouristBuyer(String buyerName, String buyerDirector, String telPhone
            , BuyerCheckStateEnum buyerCheckState) {
        TouristBuyer touristBuyer = new TouristBuyer();
        touristBuyer.setCreateTime(LocalDateTime.now());
        touristBuyer.setBuyerName(buyerName == null ? UUID.randomUUID().toString() : buyerName);
        touristBuyer.setBuyerDirector(buyerDirector == null ? UUID.randomUUID().toString() : buyerDirector);
        //todo 随机生成手机号码
        touristBuyer.setTelPhone(telPhone == null ? UUID.randomUUID().toString() : telPhone);
        touristBuyer.setCheckState(buyerCheckState == null ? randomBuyerCheckState() : buyerCheckState);
        touristBuyer.setCreateTime(LocalDateTime.now());
        return touristBuyerRepository.saveAndFlush(touristBuyer);
    }

    /**
     * 添加采购商支付记录
     *
     * @param touristBuyer 采购商
     * @param payDate      支付时间
     * @return
     */
    protected PurchaserPaymentRecord createPurchaserPaymentRecord(TouristBuyer touristBuyer, LocalDateTime payDate) {
        PurchaserPaymentRecord purchaserPaymentRecord = new PurchaserPaymentRecord();
        purchaserPaymentRecord.setTouristBuyer(touristBuyer == null ? createTouristBuyer(null, null, null, null) : touristBuyer);
        purchaserPaymentRecord.setPayDate(payDate == null ? LocalDateTime.now() : payDate);
        purchaserPaymentRecord.setCreateTime(LocalDateTime.now());
        return purchaserPaymentRecordRepository.saveAndFlush(purchaserPaymentRecord);
    }


}
