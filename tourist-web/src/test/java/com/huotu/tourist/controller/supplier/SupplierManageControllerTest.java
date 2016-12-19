/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.supplier;

import com.google.gson.Gson;
import com.huotu.tourist.AbstractMatcher;
import com.huotu.tourist.WebTest;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.OrderService;
import com.huotu.tourist.service.TouristRouteService;
import com.huotu.tourist.service.TravelerService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SupplierManageControllerTest extends WebTest {

    @Autowired
    private TouristSupplierRepository touristSupplierRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TouristOrderRepository touristOrderRepository;

    @Autowired
    private TouristRouteRepository touristRouteRepository;

    @Autowired
    private TravelerService travelerService;

    @Autowired
    private TouristRouteService touristRouteService;

    @Autowired
    private TravelerRepository travelerRepository;

    /**
     * 显示订单列表
     * @throws Exception
     */
    @Test
    public void orderList() throws Exception {
        TouristSupplier currentSupplier = new TouristSupplier();

        List<TouristBuyer> buyers=new ArrayList<>();
        List<String> orderNos=new ArrayList<>();

        for(int i=0;i<50;i++){
            TouristOrder order=new TouristOrder();
            order.setOrderNo(randomString());
            order.setOrderState(randomOrderStateEnum());
            order.setPayTime(randomLocalDateTime(order));
//            order.set
        }

        int pageSize = random.nextInt(100)+10;

        mockMvc.perform(get("/supplier/orderList").param("size",""+pageSize))
                .andExpect(model().attribute("page", new AbstractMatcher<Object>(){
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));

    }

    private LocalDateTime randomLocalDateTime(TouristOrder order){
        OrderStateEnum orderStateEnum=order.getOrderState();
        if(!OrderStateEnum.NotPay.equals(orderStateEnum)){
            return LocalDateTime.now();
        }else {
            return null;
        }
    }

    private OrderStateEnum randomOrderStateEnum(){
        int orderStateNo=random.nextInt(7);
        switch (orderStateNo){
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
     * 导出订单信息
     * @throws Exception
     */
    @Test
    public void exportOrder() throws Exception{

    }


    /**
     * 显示所有与他相同的线路订单的出行时间
     * @throws Exception
     */
    @Test
    public void showAllOrderTouristDate() throws Exception{
        List<TouristRoute> routes=new ArrayList<>();
//        Map<Long,List<Traveler>> map=new HashMap<>();
        for(int i=0;i<10;i++){
            TouristRoute route=new TouristRoute();
            route.setMaxPeople(random.nextInt(50)+20);
            route.setFromDate(LocalDate.now());

            route=touristRouteRepository.saveAndFlush(route);
            int peopleNumber=random.nextInt(route.getMaxPeople());

            List<Traveler> travelers=new ArrayList<>();
            for(int j=0;j<peopleNumber;j++){
                Traveler traveler=new Traveler();
                traveler.setRoute(route);
                travelers.add(travelerRepository.saveAndFlush(traveler));
            }
            routes.add(route);
//            map.put(route.getId(),travelers);
        }

        TouristRoute ownRoute=routes.get(random.nextInt(routes.size()));
        //预期
        List<TouristRouteModel> touristRouteModelsExpect=touristRouteService.touristRouteModelConver(routes);

        String result=mockMvc.perform(get("/supplier/getAllOrderTouristDate")
                .param("id",""+ownRoute.getId()))
                .andReturn().getResponse().getContentAsString();
        Gson gson=new Gson();
        List<TouristRouteModel> touristRouteModelsActual=JsonPath.read(result,"$.data");


//        JSONObject object=JSONObject.
//        JSONObject data=(JSONObject) object.get("resultData");
//        JSONArray articleList=(JSONArray) data.get("articleList");
//        AppCircleArticleModel[] newArticleModels=JSONObject.toJavaObject(articleList,AppCircleArticleModel[].class);

    }

    /**
     * 修改某个订单的出行时间
     * @throws Exception
     */
    @Test
    public void modifyOrderTouristDate() throws Exception{

    }

    /**
     * 查看订单详情测试
     * @throws Exception
     */
    @Test
    public void showOrder() throws Exception{

    }

    /**
     * 修改订单备注
     * @throws Exception
     */
    @Test
    public void modifyRemarks() throws Exception{

    }


}