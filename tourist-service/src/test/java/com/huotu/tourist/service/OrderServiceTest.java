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
import com.huotu.tourist.entity.*;
import com.huotu.tourist.repository.*;
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

    @Autowired
    SettlementSheetRepository settlementSheetRepository;

    @Autowired
    ConnectMallService connectMallService;


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

    @Test
    public void settleOrderTest() throws Exception{
        long days=connectMallService.getServiceDays();

        TouristSupplier supplier=createTouristSupplier("slt");
        TouristSupplier wy=createTouristSupplier("wy");


        TouristGood good=createTouristGood("sltGoods",null,null,null,supplier,null,null,null,null,null,null,null,null
                ,null,null,null,null,11,null,null);
        TouristGood wyGoods=createTouristGood("sltGoods",null,null,null,wy,null,null,null,null,null,null,null,null
                ,null,null,null,null,11,null,null);
        List<TouristGood> goods=new ArrayList<>(Arrays.asList(good,wyGoods));
        List<TouristOrder> orders=new ArrayList<>();
        List<TouristOrder> wyOrders=new ArrayList<>();
        Random random=new Random();
        for(int i=0;i<20;i++){
            TouristOrder order=new TouristOrder();
            order.setOrderState(OrderStateEnum.Finish);
            int goodsnumber=random.nextInt(2);
            order.setTouristGood(goods.get(goodsnumber));
            order.setOrderMoney(new BigDecimal(100));
            int day=random.nextInt(20)+1;
            LocalDateTime now=LocalDateTime.now();
            Calendar calendar=Calendar.getInstance();
            int nowDay=calendar.get(Calendar.DAY_OF_MONTH);
            order.setCreateTime(LocalDateTime.of(now.getYear(),now.getMonthValue(),day,0,0));
            order=touristOrderRepository.saveAndFlush(order);
            if(day<nowDay-days-1){
                if(goodsnumber==0){
                    orders.add(order);
                }
                if(goodsnumber==1){
                    wyOrders.add(order);
                }
            }
        }

        BigDecimal sltMoney=new BigDecimal(0);
        for(TouristOrder order:orders){
            sltMoney=sltMoney.add(order.getOrderMoney());
        }
        BigDecimal wyMoney=new BigDecimal(0);
        for(TouristOrder order:wyOrders){
            wyMoney=wyMoney.add(order.getOrderMoney());
        }

        settlementSheetService.settleOrder();
        List<SettlementSheet> sheets=settlementSheetRepository.findByTouristSupplier(supplier);
        List<SettlementSheet> wysheets=settlementSheetRepository.findByTouristSupplier(wy);
        List<TouristOrder> ordersAct=touristOrderRepository.findBySettlement();
        Assert.isTrue(sltMoney.compareTo(sheets.get(0).getReceivableAccount())==0);
        Assert.isTrue(wyMoney.compareTo(wysheets.get(0).getReceivableAccount())==0);
    }




}