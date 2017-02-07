/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package me.jiangcai.dating;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.common.SexEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.common.TravelerTypeEnum;
import com.huotu.tourist.core.ServiceConfig;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.PurchaserProductSetting;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.BannerRepository;
import com.huotu.tourist.repository.PresentRecordRepository;
import com.huotu.tourist.repository.PurchaserPaymentRecordRepository;
import com.huotu.tourist.repository.PurchaserProductSettingRepository;
import com.huotu.tourist.repository.SettlementSheetRepository;
import com.huotu.tourist.repository.TouristBuyerRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TouristTypeRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.SettlementSheetService;
import com.huotu.tourist.service.TouristGoodService;
import me.jiangcai.lib.resource.service.ResourceService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author CJ
 */
@WebAppConfiguration
@ActiveProfiles({"test", "unit_test"})
@ContextConfiguration(classes = {TestConfig.class, ServiceConfig.class})
public abstract class ServiceBaseTest extends SpringWebTest {
    @Autowired
    protected ResourceService resourceService;
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected ConnectMallService connectMallService;
    @Autowired
    protected TouristBuyerRepository touristBuyerRepository;
    @Autowired
    protected TouristRouteRepository touristRouteRepository;
    @Autowired
    protected TravelerRepository travelerRepository;
    @Autowired
    protected TouristOrderRepository touristOrderRepository;
    @Autowired
    protected PresentRecordRepository presentRecordRepository;
    @Autowired
    protected TouristTypeRepository touristTypeRepository;
    @Autowired
    protected TouristSupplierRepository touristSupplierRepository;
    @Autowired
    protected TouristGoodRepository touristGoodRepository;
    @Autowired
    protected ActivityTypeRepository activityTypeRepository;

    @Autowired
    protected SettlementSheetService settlementSheetService;

    @Autowired
    protected SettlementSheetRepository settlementSheetRepository;

    @Autowired
    protected BannerRepository bannerRepository;

    @Autowired
    protected PurchaserPaymentRecordRepository purchaserPaymentRecordRepository;

    @Autowired
    protected PurchaserProductSettingRepository purchaserProductSettingRepository;

    @Autowired
    protected TouristGoodService touristGoodService;
    /**
     * 第几页字符串名称
     */
    protected String pageParameterName = "pageNo";
    /**
     * 每页几条字符串名称
     */
    protected String sizeParameterName = "pageSize";



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
     * 创建一个供应商
     *
     * @param supplierName
     * @return
     */
    protected TouristSupplier createTouristSupplier(String supplierName) {
        TouristSupplier supplier = new TouristSupplier();
        supplier.setSupplierName(supplierName == null ? UUID.randomUUID().toString() : supplierName);
        supplier.setCreateTime(LocalDateTime.now());
        return touristSupplierRepository.saveAndFlush(supplier);
    }

    /**
     * @return 创建好的一个随机供应商
     */
    protected TouristSupplier createRandomTouristSupplier() {
        return createTouristSupplier(null);
    }

    /**
     * @return 随机地址
     */
    protected Address randomAddress() {
        return new Address(org.apache.commons.lang.RandomStringUtils.randomAlphabetic(5)
                , org.apache.commons.lang.RandomStringUtils.randomAlphabetic(5)
                , org.apache.commons.lang.RandomStringUtils.randomAlphabetic(6));
    }

    /**
     * @return 已创建的随机线路
     */
    protected TouristGood createRandomTouristGood() {
        return createTouristGood(null, null, null, null, null
                , UUID.randomUUID().toString(), randomAddress(), randomAddress(), randomAddress(), randomPrice()
                , randomRate(), randomRate(), RandomStringUtils.randomAlphabetic(6), randomMobile()
                , UUID.randomUUID().toString(), UUID.randomUUID().toString(), null
                , 30, null, Collections.emptyList());
    }

    /**
     * @return 已创建的随机线路
     */
    protected TouristGood createRandomTouristGood(Long mallProductId) {
        return createTouristGood(null, null, null, null, null
                , UUID.randomUUID().toString(), randomAddress(), randomAddress(), randomAddress(), randomPrice()
                , randomRate(), randomRate(), RandomStringUtils.randomAlphabetic(6), randomMobile()
                , UUID.randomUUID().toString(), UUID.randomUUID().toString(), null
                , 30, mallProductId, Collections.emptyList());
    }

    /**
     * @return 随机比例 <1
     */
    protected BigDecimal randomRate() {
        return new BigDecimal(Math.abs(random.nextDouble())).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @return 随机价格
     */
    protected BigDecimal randomPrice() {
        return new BigDecimal(1D + Math.abs(random.nextDouble()) * 200D).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 创建一个线路商品,详细
     *
     * @param name               线路名称
     * @param activityType       活动类型
     * @param touristType        线路类型
     * @param checkState         线路审核状态
     * @param touristSupplier    线路供应商
     * @param touristFeatures    线路特色
     * @param destination        目的地
     * @param placeOfDeparture   出发地
     * @param travelledAddress   途经地
     * @param price              单价
     * @param childrenDiscount   儿童折扣
     * @param rebate             返利比例
     * @param receptionPerson    地接人
     * @param receptionTelephone 地接人电话
     * @param eventDetails       活动详情
     * @param beCareful          注意事项
     * @param touristImgUri      商品图片
     * @param maxPeople          最大人数
     * @param mallProductId
     * @param images             组图
     * @return 线路商品
     */
    protected TouristGood createTouristGood(String name, ActivityType activityType, TouristType touristType
            , TouristCheckStateEnum checkState, TouristSupplier touristSupplier, String touristFeatures
            , Address destination, Address placeOfDeparture, Address travelledAddress, BigDecimal price
            , BigDecimal childrenDiscount, BigDecimal rebate, String receptionPerson, String receptionTelephone
            , String eventDetails, String beCareful, String touristImgUri, Integer maxPeople, Long mallProductId, List<String> images) {
        TouristGood touristGood = new TouristGood();
        touristGood.setTouristName(name == null ? UUID.randomUUID().toString() : name);
        touristGood.setActivityType(activityType == null ? createActivityType(null) : activityType);
        touristGood.setTouristType(touristType == null ? createTouristType(null) : touristType);
        touristGood.setTouristCheckState(checkState == null ? randomTouristCheckStateEnum() : checkState);
        touristGood.setTouristSupplier(touristSupplier == null ? createTouristSupplier(null) : touristSupplier);
        touristGood.setRecommend(false);
        touristGood.setMallProductId(mallProductId);
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
        touristGood.setCreateTime(LocalDateTime.now());
        touristGood.setImages(images);
        return touristGoodRepository.saveAndFlush(touristGood);
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
     * @return 创建好的随机活动类型
     */
    protected ActivityType createRandomActivityType() {
        return createActivityType(null);
    }

    protected TouristBuyer createRandomTouristBuyer(Long id) {
        TouristBuyer buyer = new TouristBuyer();
        buyer.setId(id);
        buyer.setBuyerName(UUID.randomUUID().toString().replace("-", ""));
        buyer.setBuyerDirector(UUID.randomUUID().toString().replace("-", ""));
        buyer.setTelPhone("13030000000");
        buyer.setPayState(BuyerPayStateEnum.NotPay);
        buyer.setPayType(PayTypeEnum.WeixinPay);
        buyer.setCreateTime(LocalDateTime.now());
        buyer.setIDNo("312225100000000000");
        return touristBuyerRepository.saveAndFlush(buyer);
    }

    protected TouristOrder createRandomTouristOrder(TouristGood good, TouristBuyer buyer) {

        TouristRoute touristRoute = new TouristRoute();
        touristRoute.setGood(good);
        touristRoute.setRouteNo(UUID.randomUUID().toString().replace("-", "-"));
        touristRoute.setCreateTime(LocalDateTime.now());
        touristRoute.setFromDate(LocalDateTime.now());
        touristRoute.setToDate(LocalDateTime.now());
        touristRoute.setMaxPeople(good.getMaxPeople());
        touristRoute = touristRouteRepository.saveAndFlush(touristRoute);

        Traveler traveler = new Traveler();
        traveler.setRoute(touristRoute);
        traveler.setSex(SexEnum.man);
        traveler.setCreateTime(LocalDateTime.now());
        traveler.setName("赵四");
        traveler.setNumber("341225111111111111");
        traveler.setTelPhone("13000000000");
        traveler.setTravelerType(TravelerTypeEnum.adult);
        traveler.setAge(25);
        traveler = travelerRepository.saveAndFlush(traveler);
        List<Traveler> travelers = new ArrayList<>();
        travelers.add(traveler);

        TouristOrder touristOrder = new TouristOrder();
        touristOrder.setTouristGood(good);
        touristOrder.setTouristBuyer(buyer);
        touristOrder.setCreateTime(LocalDateTime.now());
        touristOrder.setOrderState(OrderStateEnum.NotPay);
        touristOrder.setMallBalance(new BigDecimal(0));
        touristOrder.setMallIntegral(new BigDecimal(0));
        touristOrder.setMallCoffers(new BigDecimal(0));
        touristOrder.setTravelers(travelers);
        touristOrder.setOrderMoney(good.getPrice().multiply(new BigDecimal(travelers.size())));
        touristOrder.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        touristOrder.setPayType(PayTypeEnum.WeixinPay);

        return touristOrderRepository.saveAndFlush(touristOrder);
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

    /**
     * @return 创建好的随机线路类型
     */
    protected TouristType createRandomTouristType() {
        return createTouristType(null);
    }

    /**
     * 创建一个体现记录
     *
     * @param recordNo       体现号
     * @param supplier       供应商
     * @param amountOfMoney  钱
     * @param presentState   状态
     * @param createDateTime 创建时间
     * @return
     */
    protected PresentRecord createPresentRecord(String recordNo, TouristSupplier supplier, BigDecimal amountOfMoney
            , PresentStateEnum presentState, LocalDateTime createDateTime) {
        PresentRecord presentRecord = new PresentRecord();
        presentRecord.setCreateTime(createDateTime != null ? createDateTime : LocalDateTime.now());
        presentRecord.setRecordNo(recordNo != null ? recordNo : RandomStringUtils.randomNumeric(20));
        presentRecord.setTouristSupplier(supplier);
        presentRecord.setAmountOfMoney(amountOfMoney != null ? amountOfMoney : randomPrice());
        presentRecord.setPresentState(presentState);
        return presentRecordRepository.saveAndFlush(presentRecord);
    }


    /**
     * 创建结算单
     *
     * @param settlementNo      结算单
     * @param touristSupplier   供应商
     * @param receivableAccount 应收款
     * @param selfChecking      自审状态
     * @param platformChecking  平台审核状态
     * @param createDateTime    创建时间
     * @return
     */
    protected SettlementSheet createSettlementSheet(String settlementNo, TouristSupplier touristSupplier
            , BigDecimal receivableAccount, SettlementStateEnum selfChecking, SettlementStateEnum platformChecking
            , LocalDateTime createDateTime) {

        SettlementSheet settlementSheet = new SettlementSheet();
        settlementSheet.setSettlementNo(settlementNo != null ? settlementNo : RandomStringUtils.randomNumeric(20));
        settlementSheet.setTouristSupplier(touristSupplier);
        settlementSheet.setReceivableAccount(receivableAccount != null ? receivableAccount : randomPrice());
        settlementSheet.setSelfChecking(selfChecking != null ? selfChecking : SettlementStateEnum.NotChecking);
        settlementSheet.setPlatformChecking(platformChecking != null ? platformChecking : SettlementStateEnum.NotChecking);
        settlementSheet.setCreateTime(createDateTime != null ? createDateTime : LocalDateTime.now());

        return settlementSheetRepository.saveAndFlush(settlementSheet);


    }

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

//    protected boolean ListEqualsNotSort(List listExp,List listAct){
//
//    }

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
            , OrderStateEnum orderState, LocalDateTime createTime, LocalDateTime payTime
            , PayTypeEnum payType, String remark, SettlementSheet settlement) {
        TouristOrder order = new TouristOrder();
        order.setTouristGood(good == null ? createTouristGood(null, null, null, null, null) : good);
        order.setTouristBuyer(buyer == null ? createTouristBuyer(null, null, null, null) : buyer);
        order.setOrderNo(orderNo == null ? UUID.randomUUID().toString() : orderNo);
        order.setCreateTime(createTime == null ? LocalDateTime.now() : createTime);
        order.setOrderState(orderState == null ? randomOrderStateEnum() : orderState);
        order.setPayTime(payTime == null ? LocalDateTime.now() : payTime);
        order.setPayType(payType == null ? randomPayTypeEnum() : payType);
        order.setRemarks(remark);
        order.setSettlement(settlement);

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
        settlementSheet.setTouristSupplier(createTouristSupplier(supplierName));
        settlementSheet.setSelfChecking(SettlementStateEnum.NotChecking);
        settlementSheet.setPlatformChecking(platformChecking != null ? platformChecking : SettlementStateEnum.NotChecking);
        settlementSheet.setCreateTime(LocalDateTime.now());
        return settlementSheetRepository.saveAndFlush(settlementSheet);
    }

    /**
     * 创建一个线路商品
     *
     * @param name            线路名称
     * @param activityType    活动类型
     * @param touristType     线路类型
     * @param checkState      线路审核状态
     * @param touristSupplier 线路供应商
     * @return 线路
     */
    protected TouristGood createTouristGood(String name, ActivityType activityType, TouristType touristType
            , TouristCheckStateEnum checkState, TouristSupplier touristSupplier) {
        TouristGood touristGood = new TouristGood();
        touristGood.setTouristName(name == null ? UUID.randomUUID().toString() : name);
        touristGood.setActivityType(activityType == null ? createActivityType(null) : activityType);
        touristGood.setTouristType(touristType == null ? createTouristType(null) : touristType);
        touristGood.setTouristCheckState(checkState == null ? randomTouristCheckStateEnum() : checkState);
        touristGood.setTouristSupplier(touristSupplier == null ? createTouristSupplier(null) : touristSupplier);
        touristGood.setRecommend(false);
        return touristGoodRepository.saveAndFlush(touristGood);
    }

    /**
     * 创建一个行程路线
     *
     * @param routeNo
     * @param good
     * @param fromDate
     * @param toDate
     * @param maxPeople
     * @return
     */
    protected TouristRoute createTouristRoute(String routeNo, TouristGood good, LocalDateTime fromDate, LocalDateTime toDate,
                                              int maxPeople) {
        TouristRoute touristRoute = new TouristRoute();
        touristRoute.setRouteNo(routeNo);
        touristRoute.setGood(good);
        touristRoute.setFromDate(fromDate);
        touristRoute.setToDate(toDate);
        touristRoute.setMaxPeople(maxPeople);
        return touristRouteRepository.saveAndFlush(touristRoute);
    }

    /**
     * 创建一个游客
     *
     * @param touristRoute
     * @param order
     * @return
     */
    protected Traveler createTraveler(TouristRoute touristRoute, TouristOrder order) {
        Traveler traveler = new Traveler();
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
        touristBuyer.setId(256421L);
        touristBuyer.setCreateTime(LocalDateTime.now());
        touristBuyer.setBuyerName(buyerName == null ? UUID.randomUUID().toString() : buyerName);
        touristBuyer.setBuyerDirector(buyerDirector == null ? UUID.randomUUID().toString() : buyerDirector);
//        touristBuyer.setBuyerId(UUID.randomUUID().toString().replace("-", ""));
        touristBuyer.setTelPhone(telPhone == null ? "13012345678" : telPhone);
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

    public static class RandomComparator implements Comparator<Object> {
        static Random random = new Random();

        @Override
        public int compare(Object o1, Object o2) {
            return random.nextInt();
        }
    }

}
