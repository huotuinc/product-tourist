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
import com.huotu.tourist.entity.*;
import me.jiangcai.dating.ServiceBaseTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
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
                null, null, null, null, null, null, null, null, null);
        TouristGood good = createTouristGood(null, null, null, null, null, null, null, null, null, null, null, null, null, null
                , null, null, null, null, null, null, null);
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


    @Test
    public void touristOrdersTest() throws Exception {
        String sltSuplierName = "slt";
        String wySupplierName = "wy";
        String queryOrderNo = "123456789";
        PayTypeEnum queryPayType = PayTypeEnum.Alipay;
        LocalDateTime queryOrderDate = LocalDateTime.of(2016, 5, 1, 0, 0, 0);
        LocalDateTime queryPayDate = LocalDateTime.of(2016, 7, 1, 0, 0, 0);
        LocalDateTime queryTouristDate = LocalDateTime.of(2016, 3, 1, 0, 0, 0);
        OrderStateEnum queryOrderState = OrderStateEnum.Finish;
        TouristSupplier sltSupplier = createTouristSupplier(sltSuplierName);
        TouristSupplier wySupplier = createTouristSupplier(wySupplierName);
        TouristGood sltGoods = createTouristGood("sltGoods", null, null, null, sltSupplier, null, null, null, null
                , null, null, null, null, null, null, null, null, 11, null, null, null);
        TouristGood wyGoods = createTouristGood("wyGoods", null, null, null, wySupplier, null, null, null, null
                , null, null, null, null, null, null, null, null, 11, null, null, null);
        String GoodsNameStr = "sltNameGoods";
        TouristGood nameGoods = createTouristGood(GoodsNameStr, null, null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, 11, null, null, null);
        String queryBuyerName = "sltBuyer";
        TouristBuyer buyer = createTouristBuyer(queryBuyerName, null, "12345678955", null);
        String buyerTel = "13012345678";
        TouristBuyer telBuyer = createTouristBuyer("wyBuyer", null, buyerTel, null);

        TouristBuyer otherBuyer = createTouristBuyer("otherBuyer", null, "13065489787", null);

        SettlementSheet sheet = createSettlementSheet(null, sltSupplier, new BigDecimal(200), null, null, null);

        SettlementSheet querySheetId = createSettlementSheet(null, wySupplier, new BigDecimal(400), null, null, null);

        List<TouristOrder> supplierList = new ArrayList<>();
        List<TouristOrder> supplierNameList = new ArrayList<>();
        List<TouristOrder> orderNoList = new ArrayList<>();
        List<TouristOrder> touristNameList = new ArrayList<>();
        List<TouristOrder> buyerNameList = new ArrayList<>();
        List<TouristOrder> telList = new ArrayList<>();
        List<TouristOrder> payTypeEnumList = new ArrayList<>();
        List<TouristOrder> orderDateList = new ArrayList<>();
        List<TouristOrder> payDateList = new ArrayList<>();
        List<TouristOrder> touristDateList = new ArrayList<>();
        List<TouristOrder> orderStateEnumList = new ArrayList<>();
        List<TouristOrder> settlementList = new ArrayList<>();
        List<TouristOrder> pageableList = new ArrayList<>();
        List<TouristOrder> settlementIdList = new ArrayList<>();

        Random random = new Random();
        List<TouristBuyer> allBuyers = touristBuyerRepository.findAll();
        int n = 0;
        for (int i = 0; i < 40; i++) {
            boolean supplierRan = random.nextBoolean();
            boolean supplierNameRan = random.nextBoolean();
            boolean orderNoLRan = i == 0;
            boolean touristNameRan = random.nextBoolean();
            boolean buyerNameRan = random.nextBoolean();
            boolean telRan = random.nextBoolean();
            boolean payTypeEnumRan = random.nextBoolean();
            boolean orderDateRan = random.nextBoolean();
            boolean payDateRan = random.nextBoolean();
            boolean touristDateRan = random.nextBoolean();
            boolean orderStateEnumRan = random.nextBoolean();
            boolean settlementRan = random.nextBoolean();
            boolean pageableRan = i > 9 && i < 20;
            boolean settlementIdRan = random.nextBoolean();

            TouristOrder order = new TouristOrder();
            if (supplierRan) {
                order.setTouristGood(sltGoods);
            } else {
                if (supplierNameRan) {
                    order.setTouristGood(wyGoods);
                } else {
                    if (touristNameRan) {
                        order.setTouristGood(nameGoods);
                    }
                }
            }


            if (orderNoLRan) {
                order.setOrderNo(queryOrderNo);
            }

            if (buyerNameRan) {
                order.setTouristBuyer(buyer);
            } else {
                if (telRan) {
                    order.setTouristBuyer(telBuyer);
                } else {
                    order.setTouristBuyer(otherBuyer);
                }
            }


            if (payTypeEnumRan) {
                order.setPayType(queryPayType);
            } else {
                order.setPayType(PayTypeEnum.WeixinPay);
            }


            if (orderDateRan) {
                order.setCreateTime(queryOrderDate);
            }


            if (payDateRan) {
                order.setPayTime(queryPayDate);
            }


            if (orderStateEnumRan) {
                order.setOrderState(queryOrderState);
            } else {
                order.setOrderState(OrderStateEnum.NotFinish);
            }

            if (settlementRan && settlementIdRan) {
                order.setSettlement(querySheetId);

            }

            TouristOrder orderAct = createTouristOrder(order.getTouristGood(), order.getTouristBuyer(), order.getOrderNo()
                    , order.getOrderState(), order.getCreateTime(), order.getPayTime(), order.getPayType(), null
                    , order.getSettlement(), order.getOrderMoney());


            if (supplierRan) {
                supplierList.add(orderAct);
            } else {
                if (supplierNameRan) {
                    supplierNameList.add(orderAct);
                } else {
                    if (touristNameRan) {
                        touristNameList.add(orderAct);
                    }
                }
            }

            if (orderNoLRan) {
                orderNoList.add(orderAct);
            }

            if (buyerNameRan) {
                buyerNameList.add(orderAct);
            } else {
                if (telRan) {
                    telList.add(orderAct);
                }
            }

            if (payTypeEnumRan) {
                payTypeEnumList.add(orderAct);
            }

            if (orderDateRan) {
                orderDateList.add(orderAct);
            }

            if (payDateRan) {
                payDateList.add(orderAct);
            }

            if (orderStateEnumRan) {
                orderStateEnumList.add(orderAct);
            }

            if (settlementRan && settlementIdRan) {
                settlementList.add(orderAct);
                settlementIdList.add(orderAct);

            }

            if (pageableRan) {
                pageableList.add(orderAct);
            }


        }

        List<TouristOrder> orderListAct = touristOrderService.touristOrders(sltSupplier, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(supplierList));

        orderListAct = touristOrderService.touristOrders(null, wySupplierName, null, null, null, null, null
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(supplierNameList));

        orderListAct = touristOrderService.touristOrders(null, null, queryOrderNo, null, null, null, null
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(orderNoList));

        orderListAct = touristOrderService.touristOrders(null, null, null, GoodsNameStr, null, null, null
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(touristNameList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, queryBuyerName, null, null
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(buyerNameList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, buyerTel, null
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(telList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, queryPayType
                , null, null, null, null, null, null, null, null, null, null).getContent();
        Assert.isTrue(orderListAct.equals(payTypeEnumList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , queryOrderDate.plusDays(-10), queryOrderDate.plusDays(20), null, null, null, null, null, null, null, null)
                .getContent();
        Assert.isTrue(orderListAct.equals(orderDateList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, queryPayDate.plusDays(-10), queryPayDate.plusDays(10), null, null, null, null, null, null)
                .getContent();
        Assert.isTrue(orderListAct.equals(payDateList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, null, null, null, null, queryOrderState, null, null, null)
                .getContent();
        Assert.isTrue(orderListAct.equals(orderStateEnumList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, null, null, null, null, queryOrderState, null, null, null)
                .getContent();
        Assert.isTrue(orderListAct.equals(orderStateEnumList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, true, null, null)
                .getContent();
        Assert.isTrue(orderListAct.equals(settlementList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, new PageRequest(1, 10), null)
                .getContent();
        Assert.isTrue(orderListAct.equals(pageableList));

        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, null, querySheetId.getId())
                .getContent();
        Assert.isTrue(orderListAct.equals(settlementIdList));


        for (int i = 0; i < 10; i++) {
            addRouteAndTravelers(sltGoods, queryTouristDate);
        }


        orderListAct = touristOrderService.touristOrders(null, null, null, null, null, null, null
                , null, null, null, null, queryTouristDate.plusDays(-10), queryTouristDate.plusDays(20), null, null, null, null)
                .getContent();
//        Assert.isTrue(orderListAct.equals(touristDateList));


    }

    @Transactional
    private void addRouteAndTravelers(TouristGood good, LocalDateTime touristDate) {
        TouristOrder order = createTouristOrder(good, null, null, null, null, null, null, null, null, null);
        TouristRoute route = createTouristRoute(randomString(), good, touristDate, null, 10);
        List<Traveler> travelers = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            travelers.add(createTraveler(route, order));
        }
        order.setTravelers(travelers);
    }


}