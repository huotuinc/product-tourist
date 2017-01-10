/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package me.jiangcai.dating;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.core.ServiceConfig;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TouristTypeRepository;
import me.jiangcai.lib.resource.service.ResourceService;
import me.jiangcai.lib.test.SpringWebTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author CJ
 */
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, ServiceConfig.class})
public abstract class ServiceBaseTest extends SpringWebTest {

    @Autowired
    protected ResourceService resourceService;
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    private TouristTypeRepository touristTypeRepository;
    @Autowired
    private TouristSupplierRepository touristSupplierRepository;
    @Autowired
    private TouristGoodRepository touristGoodRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;

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
     * @param mallGoodsId
     * @param images             组图
     * @return 线路商品
     */
    protected TouristGood createTouristGood(String name, ActivityType activityType, TouristType touristType
            , TouristCheckStateEnum checkState, TouristSupplier touristSupplier, String touristFeatures
            , Address destination, Address placeOfDeparture, Address travelledAddress, BigDecimal price
            , BigDecimal childrenDiscount, BigDecimal rebate, String receptionPerson, String receptionTelephone
            , String eventDetails, String beCareful, String touristImgUri, Integer maxPeople, Long mallGoodsId, List<String> images) {
        TouristGood touristGood = new TouristGood();
        touristGood.setTouristName(name == null ? UUID.randomUUID().toString() : name);
        touristGood.setActivityType(activityType == null ? createActivityType(null) : activityType);
        touristGood.setTouristType(touristType == null ? createTouristType(null) : touristType);
        touristGood.setTouristCheckState(checkState == null ? randomTouristCheckStateEnum() : checkState);
        touristGood.setTouristSupplier(touristSupplier == null ? createTouristSupplier(null) : touristSupplier);
        touristGood.setRecommend(false);
        touristGood.setMallGoodId(mallGoodsId);
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

    public static class RandomComparator implements Comparator<Object> {
        static Random random = new Random();

        @Override
        public int compare(Object o1, Object o2) {
            return random.nextInt();
        }
    }
}
