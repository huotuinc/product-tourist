/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import me.jiangcai.dating.ServiceBaseTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author CJ
 */

public class OrderServiceTest extends ServiceBaseTest {

    private static final Log log = LogFactory.getLog(OrderServiceTest.class);
    @Autowired
    TouristOrderRepository touristOrderRepository;

    @Autowired
    ActivityTypeRepository activityTypeRepository;

    @Autowired
    TouristGoodRepository touristGoodRepository;

    @Autowired
    TouristSupplierRepository touristSupplierRepository;

    @Autowired
    SettlementSheetService settlementSheetService;


    @Test
    public void runnableTest() {
        log.info("sadfasdf");
    }


    @Test
    public void searchActivityTypeGruopTest(){
        Map<Long,Long> map=new HashMap<>();
        List<ActivityType> types=new ArrayList<>();
        for(int i=0;i<10;i++){
            ActivityType activityType=new ActivityType();
            activityType.setActivityName("活动"+i);
            activityType.setCreateTime(LocalDateTime.now());
            types.add(activityTypeRepository.saveAndFlush(activityType));
        }

        List<TouristGood> goods=new ArrayList<>();
        for(int i=0;i<10;i++){
            TouristGood touristGood=new TouristGood();
            touristGood.setActivityType(types.get(i));
            goods.add(touristGoodRepository.saveAndFlush(touristGood));
        }

        for(int i=0;i<50;i++){
            Random random=new Random();
            int f=random.nextInt(10);
            Long typeId=goods.get(f).getActivityType().getId();
            Long number=map.get(typeId);
            if(number==null){
                map.put(typeId,1L);
            }else {
                map.put(typeId,number+1);
            }


            TouristOrder touristOrder=new TouristOrder();
            touristOrder.setTouristGood(goods.get(f));
            touristOrder.setOrderNo(UUID.randomUUID().toString());
            touristOrderRepository.saveAndFlush(touristOrder);
        }

        Object[] o =touristOrderRepository.searchActivityTypeGruop();
        for(Object os: o){
            Object[] oo=(Object[])os;
            ActivityType actType=(ActivityType) oo[0];
            Long numberAct=(Long)oo[1];
            Long number=map.get(actType.getId());
            Assert.isTrue(numberAct.equals(number));
        }


    }

    @Test
    public void countBalanceTest() throws Exception{
        TouristSupplier supplier=new TouristSupplier();
        supplier.setSupplierName("slt");
        supplier=touristSupplierRepository.saveAndFlush(supplier);
        TouristGood touristGood=new TouristGood();
        touristGood.setTouristSupplier(supplier);
        touristGood=touristGoodRepository.saveAndFlush(touristGood);

        settlementSheetService.countBalance(supplier,LocalDateTime.now());
    }

    @Test
    public void countSettledTest() throws Exception{
        TouristSupplier supplier=new TouristSupplier();
        supplier.setSupplierName("slt");
        supplier=touristSupplierRepository.saveAndFlush(supplier);
        BigDecimal settled= settlementSheetService.countSettled(supplier);
        BigDecimal notSettled=settlementSheetService.countNotSettled(supplier);
        BigDecimal withdrawal=settlementSheetService.countWithdrawal(supplier, null);
    }




}