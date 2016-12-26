/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist;

import com.huotu.tourist.common.*;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.repository.*;
import com.huotu.tourist.service.TouristGoodService;
import me.jiangcai.dating.ServiceBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 常用web测试基类
 */
@SuppressWarnings("ALL")
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

    @Autowired
    protected PurchaserProductSettingRepository purchaserProductSettingRepository;

    @Autowired
    protected ActivityTypeRepository activityTypeRepository;

    @Autowired
    protected TouristTypeRepository touristTypeRepository;
    @Autowired
    protected TravelerRepository travelerRepository;
    @Autowired
    SettlementSheetRepository settlementSheetRepository;

    @Autowired
    protected TouristGoodService touristGoodService;

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
     * 创建一个审查状态
     *
     * @return 审查状态
     */
    protected SettlementStateEnum randomSettlementStateEnum() {
        int stateNo = random.nextInt(2);
        switch (stateNo) {
            case 0:
                return SettlementStateEnum.NotChecking;
            default:
                return SettlementStateEnum.CheckFinish;
        }
    }

    /**
     * 创建一个线路审核的状态
     *
     * @return 线路审核的状态
     */
    protected TouristCheckStateEnum randomTouristCheckStateEnum() {
        int checkStateNo = random.nextInt(4);
        switch (checkStateNo) {
            case 0:
                return TouristCheckStateEnum.NotChecking;
            case 1:
                return TouristCheckStateEnum.CheckFinish;
            case 2:
                return TouristCheckStateEnum.Saved;
            default:
                return TouristCheckStateEnum.Recycle;
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
            , OrderStateEnum orderState,LocalDateTime createTime, LocalDateTime payTime
            , PayTypeEnum payType,String remark) {
        TouristOrder order = new TouristOrder();
        order.setTouristGood(good == null ? createTouristGood(null, null, null, null, null) : good);
        order.setTouristBuyer(buyer == null ? createTouristBuyer(null, null, null, null) : buyer);
        order.setOrderNo(orderNo == null ? UUID.randomUUID().toString() : orderNo);
        order.setCreateTime(createTime == null ? LocalDateTime.now() : createTime);
        order.setOrderState(orderState == null ? randomOrderStateEnum() : orderState);
        order.setPayTime(payTime == null ? LocalDateTime.now() : payTime);
        order.setPayType(payType == null ? randomPayTypeEnum() : payType);
        order.setRemarks(remark);

        return touristOrderRepository.saveAndFlush(order);
    }

    /**
     * 创建一个结算单
     *
     * @param createTime       创建时间
     * @param platformChecking 平台审核状态
     * @param supplierName     供应商名称
     * @return
     */
    protected SettlementSheet createSettlementSheet(LocalDateTime createTime, SettlementStateEnum platformChecking,
                                                    String supplierName) {
        SettlementSheet settlementSheet = new SettlementSheet();
        settlementSheet.setCreateTime(createTime == null ? LocalDateTime.now() : createTime);
        settlementSheet.setPlatformChecking(platformChecking == null ? randomSettlementStateEnum() : platformChecking);
        settlementSheet.setTouristOrder(
                createTouristOrder(
                        createTouristGood(null, null, null, null,
                                createTouristSupplier(supplierName)
                        )
                        , null, null, null, null, null, null,null
                )
        );
        return settlementSheetRepository.saveAndFlush(settlementSheet);
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
     * @param name 线路名称
     * @param activityType 活动类型
     * @param touristType 线路类型
     * @param checkState  线路审核状态
     * @param touristSupplier 线路供应商
     * @return 线路
     */
    protected TouristGood createTouristGood(String name, ActivityType activityType, TouristType touristType
            , TouristCheckStateEnum checkState, TouristSupplier touristSupplier) {
        TouristGood touristGood=new TouristGood();
        touristGood.setTouristName(name == null ? UUID.randomUUID().toString() : name);
        touristGood.setActivityType(activityType == null ? createActivityType(null) : activityType);
        touristGood.setTouristType(touristType == null ? createTouristType(null) : touristType);
        touristGood.setTouristCheckState(checkState == null ? randomTouristCheckStateEnum() : checkState);
        touristGood.setTouristSupplier(touristSupplier == null ? createTouristSupplier(null) : touristSupplier);
        touristGood.setRecommend(null);
        return touristGoodRepository.saveAndFlush(touristGood);
    }

    /**
     * 创建一个线路商品,详细
     * @param name 线路名称
     * @param activityType          活动类型
     * @param touristType           线路类型
     * @param checkState            线路审核状态
     * @param touristSupplier       线路供应商
     * @param touristFeatures       线路特色
     * @param destination           目的地
     * @param placeOfDeparture      出发地
     * @param travelledAddress      途经地
     * @param price                 单价
     * @param childrenDiscount      儿童折扣
     * @param rebate                返利比例
     * @param receptionPerson       地接人
     * @param receptionTelephone    地接人电话
     * @param eventDetails          活动详情
     * @param beCareful             注意事项
     * @param touristImgUri         商品图片
     * @param maxPeople             最大人数
     * @return  线路商品
     */
    protected TouristGood createTouristGood(String name, ActivityType activityType, TouristType touristType
            , TouristCheckStateEnum checkState, TouristSupplier touristSupplier,String touristFeatures
            ,Address destination,Address placeOfDeparture,Address travelledAddress,BigDecimal price
            ,BigDecimal childrenDiscount,BigDecimal rebate,String receptionPerson,String receptionTelephone
            ,String eventDetails,String beCareful,String touristImgUri,Integer maxPeople) {
        TouristGood touristGood=new TouristGood();
        touristGood.setTouristName(name == null ? UUID.randomUUID().toString() : name);
        touristGood.setActivityType(activityType == null ? createActivityType(null) : activityType);
        touristGood.setTouristType(touristType == null ? createTouristType(null) : touristType);
        touristGood.setTouristCheckState(checkState == null ? randomTouristCheckStateEnum() : checkState);
        touristGood.setTouristSupplier(touristSupplier == null ? createTouristSupplier(null) : touristSupplier);
        touristGood.setRecommend(null);
        touristGood.setTouristFeatures(touristFeatures);
        touristGood.setDestination(destination);
        touristGood.setPlaceOfDeparture(placeOfDeparture);
        touristGood.setTravelledAddress(travelledAddress);
        touristGood.setPrice(price);
        touristGood.setChildrenDiscount(childrenDiscount);
        touristGood.setRebate(rebate);
        touristGood.setReceptionPerson(receptionPerson);
        touristGood.setReceptionTelephone(receptionTelephone);
        touristGood.setEventDetails(eventDetails);
        touristGood.setBeCareful(beCareful);
        touristGood.setTouristImgUri(touristImgUri);
        touristGood.setMaxPeople(maxPeople);
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
    protected TouristRoute createTouristRoute(String routeNo, TouristGood good, LocalDateTime fromDate, LocalDateTime toDate,
                                              int maxPeople) {
        TouristRoute touristRoute=new TouristRoute();
        touristRoute.setRouteNo(routeNo);
        touristRoute.setGood(good);
        touristRoute.setFromDate(fromDate);
        touristRoute.setToDate(toDate);
        touristRoute.setMaxPeople(maxPeople);
        return touristRouteRepository.saveAndFlush(touristRoute);
    }

    /**
     * 创建一个游客
     * @param touristRoute
     * @param order
     * @return
     */
    protected Traveler createTraveler(TouristRoute touristRoute, TouristOrder order){
        Traveler traveler=new Traveler();
        traveler.setOrder(order);
        traveler.setRoute(touristRoute);
        return travelerRepository.saveAndFlush(traveler);
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
        touristBuyer.setBuyerId(UUID.randomUUID().toString().replace("-", ""));
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


    /**
     * 采购商产品设置
     *
     * @param name 采购商产品设置名称
     * @return
     */
    protected PurchaserProductSetting createPurchaserProductSetting(String name) {
        PurchaserProductSetting purchaserProductSetting = new PurchaserProductSetting();
        purchaserProductSetting.setName(name == null ? UUID.randomUUID().toString().replace("-", "") : name);
        purchaserProductSetting.setPrice(new BigDecimal(100));
        return purchaserProductSettingRepository.saveAndFlush(purchaserProductSetting);
    }

    /**
     * 创建活动类型
     *
     * @param activityName
     * @return
     */
    protected ActivityType createActivityType(String activityName) {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName(activityName == null ? UUID.randomUUID().toString() : activityName);
        activityType.setCreateTime(LocalDateTime.now());
        return activityTypeRepository.saveAndFlush(activityType);
    }

    /**
     * 创建线路类型
     *
     * @param typeName
     * @return
     */
    protected TouristType createTouristType(String typeName) {
        TouristType touristType = new TouristType();
        touristType.setTypeName(typeName == null ? UUID.randomUUID().toString() : typeName);
        touristType.setCreateTime(LocalDateTime.now());
        return touristTypeRepository.saveAndFlush(touristType);
    }

}
