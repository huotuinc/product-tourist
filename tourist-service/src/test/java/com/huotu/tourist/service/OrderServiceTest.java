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
import me.jiangcai.dating.ServiceBaseTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author CJ
 */

public class OrderServiceTest extends ServiceBaseTest {

    private static final Log log = LogFactory.getLog(OrderServiceTest.class);


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
    public void countOrderTotalMoneyTest() throws Exception {
        TouristSupplier supplier = createTouristSupplier("slt");
        SettlementSheet sheet = createSettlementSheet(null, supplier, null, null, null, null);
        TouristGood supplierGood = createTouristGood(null, null, null, null, supplier, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
        TouristGood good = createTouristGood(null, null, null, null, null, null, null, null, null, null, null, null, null, null
                , null, null, null, null, null, null);
        TouristBuyer buyer = createRandomTouristBuyer(100L);

        BigDecimal supplierMoney = new BigDecimal(0);
        BigDecimal orderStateMoney = new BigDecimal(0);
        BigDecimal dateMoney = new BigDecimal(0);
        BigDecimal settlementMoney = new BigDecimal(0);
        BigDecimal touristGoodMoney = new BigDecimal(0);
        BigDecimal touristBuyerMoney = new BigDecimal(0);

        Random random = new Random();

        for (int i = 0; i < 30; i++) {
            boolean supplierRan = random.nextBoolean();
            boolean orderStateRan = random.nextBoolean();
            boolean dateRan = random.nextBoolean();
            boolean settlementRan = random.nextBoolean();
            boolean goodsRan = random.nextBoolean();
            boolean buyerRan = random.nextBoolean();

            TouristOrder order = new TouristOrder();
            order.setOrderMoney(randomPrice());
            if (supplierRan) {
                order.setTouristGood(supplierGood);
                supplierMoney = supplierMoney.add(order.getOrderMoney());
            } else {
                if (goodsRan) {
                    order.setTouristGood(good);
                    touristGoodMoney = touristGoodMoney.add(order.getOrderMoney());
                }
            }
            if (orderStateRan) {
                order.setOrderState(OrderStateEnum.Finish);
                orderStateMoney = orderStateMoney.add(order.getOrderMoney());
            } else {
                order.setOrderState(OrderStateEnum.NotFinish);
            }
            if (dateRan) {
                order.setCreateTime(LocalDateTime.of(2016, 5, 5, 0, 0, 0));
                dateMoney = dateMoney.add(order.getOrderMoney());
            }
            if (settlementRan) {
                order.setSettlement(sheet);
                settlementMoney = settlementMoney.add(order.getOrderMoney());
            }
            if (buyerRan) {
                order.setTouristBuyer(buyer);
                touristBuyerMoney = touristBuyerMoney.add(order.getOrderMoney());
            }
            TouristOrder orderAct = createTouristOrder(order.getTouristGood(), order.getTouristBuyer(), null
                    , order.getOrderState(), order.getCreateTime(), null, null, null, order.getSettlement()
                    , order.getOrderMoney());
        }

        BigDecimal moneyAct = touristOrderService.countOrderTotalMoney(supplier, null, null, null, null, null, null);
        Assert.isTrue(moneyAct.compareTo(supplierMoney) == 0);

        moneyAct = touristOrderService.countOrderTotalMoney(null, OrderStateEnum.Finish, null, null, null, null, null);
        Assert.isTrue(moneyAct.compareTo(orderStateMoney) == 0);

        moneyAct = touristOrderService.countOrderTotalMoney(null, null, LocalDateTime.of(2016, 4, 1, 0, 0, 0)
                , LocalDateTime.of(2016, 6, 1, 0, 0, 0), null, null, null);
        Assert.isTrue(moneyAct.compareTo(dateMoney) == 0);

        moneyAct = touristOrderService.countOrderTotalMoney(null, null, null, null, true, null, null);
        Assert.isTrue(moneyAct.compareTo(settlementMoney) == 0);

        moneyAct = touristOrderService.countOrderTotalMoney(null, null, null, null, null, good, null);
        Assert.isTrue(moneyAct.compareTo(touristGoodMoney) == 0);

        moneyAct = touristOrderService.countOrderTotalMoney(null, null, null, null, null, null, buyer);
        Assert.isTrue(moneyAct.compareTo(touristBuyerMoney) == 0);


    }


}