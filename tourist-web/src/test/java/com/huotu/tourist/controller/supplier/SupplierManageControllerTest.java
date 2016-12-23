/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.AbstractMatcher;
import com.huotu.tourist.WebTest;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.TouristRouteService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SupplierManageControllerTest extends WebTest {
    @Autowired
    private TouristRouteRepository touristRouteRepository;

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private TouristRouteService touristRouteService;

    /**
     * 显示订单列表
     * @throws Exception
     */
    @Test
    public void orderList() throws Exception {
        //预期
        String orderNo=randomString();
        TouristOrder order=createTouristOrder(null,null,orderNo,null,null,null,null,null);


        String json=mockMvc.perform(get("/supplier/orderList")
                .param("orderId",orderNo))
                .andReturn().getResponse().getContentAsString();

        //实际
        Long idActual= JsonPath.read(json,"$.row[0].id");
        assertThat(order.getId().equals(idActual)).isTrue().as("订单号校验");



        //预期
        order = createTouristOrder(createTouristGood("slt", null, null, null, null), null,null, null, null, null, null,
                null);

        json=mockMvc.perform(get("/supplier/orderList")
                .param("name","slt"))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= JsonPath.read(json,"$.row[0].id");
        assertThat(order.getId().equals(idActual)).isTrue().as("线路名称校验");

        //预期
        order=createTouristOrder(null,createTouristBuyer("wy",null,null,null),null,null,null,null,null,null);

        json=mockMvc.perform(get("/supplier/orderList")
                .param("buyer","wy"))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= JsonPath.read(json,"$.row[0].id");
        assertThat(order.getId().equals(idActual)).isTrue().as("购买人校验");

        //预期
        List<TouristOrder> touristOrders=new ArrayList<>();
        for(int i=0;i<3;i++){
            touristOrders.add(createTouristOrder(null,null,null,null,
                    LocalDateTime.of(2016,i*3+1,1,1,1),null,null,null));
        }
        order=touristOrders.get(1);
        json=mockMvc.perform(get("/supplier/orderList")
                .param("orderDate",""+ LocalDateTime.of(2016,3,5,5,5))
                .param("endOrderDate",""+LocalDateTime.of(2016,6,7,7,7)))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= JsonPath.read(json,"$.row[0].id");
        assertThat(order.getId().equals(idActual)).isTrue().as("下单时间校验");

        //预期
        touristOrders=new ArrayList<>();
        for(int i=0;i<3;i++){
            touristOrders.add(createTouristOrder(null,null,null,null,null,
                    LocalDateTime.of(2016,i*3+1,1,1,1),null,null));
        }
        order=touristOrders.get(1);
        json=mockMvc.perform(get("/supplier/orderList")
                .param("payDate",""+ LocalDateTime.of(2016,3,5,5,5))
                .param("endPayDate",""+LocalDateTime.of(2016,6,7,7,7)))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= JsonPath.read(json,"$.row[0].id");
        assertThat(order.getId().equals(idActual)).isTrue().as("支付时间校验");



        //预期
        TouristGood touristGood = createTouristGood("slt", null, null, null, null);
        TouristRoute touristRoute=createTouristRoute(null,touristGood,LocalDateTime.of(2016,10,10,0,0),null,0);
        order=createTouristOrder(touristGood,null,null,null,null,null,null,null);
        json=mockMvc.perform(get("/supplier/orderList")
                .param("touristDate",""+ LocalDate.of(2016,10,10)))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= JsonPath.read(json,"$.row[0].id");
        assertThat(order.getId().equals(idActual)).isTrue().as("出行时间校验");
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
        for(int i=0;i<10;i++){
            TouristRoute route=createTouristRoute(null,null,LocalDateTime.now(),null,random.nextInt(50)+20);
            int peopleNumber=random.nextInt(route.getMaxPeople());

            List<Traveler> travelers=new ArrayList<>();
            for(int j=0;j<peopleNumber;j++){
                Traveler traveler=createTraveler(route,null);
                travelers.add(traveler);
            }
            routes.add(route);
        }

        TouristRoute ownRoute=routes.get(random.nextInt(routes.size()));
        //预期
        List<TouristRouteModel> touristRouteModelsExpect=new ArrayList<>();
        for(TouristRoute route :routes){
            //排除自己的线路订单
            if(route.getId().equals(ownRoute.getId())){
                continue;
            }
            TouristRouteModel model=new TouristRouteModel();
            model.setId(route.getId());
            model.setFromDate(route.getFromDate());
            model.setRemainPeople(touristRouteService.getRemainPeopleByRoute(route));
            touristRouteModelsExpect.add(model);
        }

        String result=mockMvc.perform(get("/supplier/getAllOrderTouristDate")
                .param("id",""+ownRoute.getId()))
                .andReturn().getResponse().getContentAsString();
        //实际
        List<TouristRouteModel> touristRouteModelsActual=JsonPath.read(result,"$.data");
        assertThat(touristRouteModelsExpect.equals(touristRouteModelsActual)).isTrue().as("其他路线校验");


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

        TouristOrder order=createTouristOrder(null,null,"slt",null,null,null,null,null);
        TouristRoute routeF=createTouristRoute(null,null,LocalDateTime.now(),null,4);
        TouristRoute routeL=createTouristRoute(null,null,LocalDateTime.of(2016,10,10,0,0),null,4);
        Traveler traveler=createTraveler(routeF,order);
        mockMvc.perform(get("/supplier/modifyOrderTouristDate")
                .param("formerId",""+routeF.getId()).param("laterId",""+routeL.getId()));
        Traveler travelerA=travelerRepository.findOne(traveler.getId());
        assertThat(routeL.getFromDate().equals(travelerA.getRoute().getFromDate())).isTrue().as("出行人数校验");


    }

    /**
     * 查看订单详情测试
     * @throws Exception
     */
    @Test
    public void showOrder() throws Exception{

        //预期
        TouristOrder orderExp=createTouristOrder(null,null,"1234",null,null,null,null,null);

        TouristRoute routeExp=createTouristRoute("4567",null,null,null,10);

        List<Traveler> travelersExp=new ArrayList<>();
        for(int i=0;i<2;i++){
            travelersExp.add(createTraveler(routeExp,orderExp));
        }

        mockMvc.perform(get("/supplier/showOrder").param("id",""+orderExp.getId()))
                .andExpect(model().attribute("order", new AbstractMatcher<Object>(){
                    @Override
                    public boolean matches(Object o) {
                        TouristOrder orderAct=(TouristOrder)o;
                        return orderExp.getId().equals(orderAct.getId());
                    }
                })).andExpect(model().attribute("route", new AbstractMatcher<Object>() {

            @Override
            public boolean matches(Object item) {
                TouristRoute routeAct=(TouristRoute)item;
                return routeExp.getId().equals(routeAct.getId());
            }
        })).andExpect(model().attribute("travelers", new AbstractMatcher<Object>() {

            @Override
            public boolean matches(Object item) {
                List<Traveler> travelersAct=(List<Traveler>)item;
                return ListMatch(travelersExp,travelersAct);
            }
        }));
    }

    /**
     * 比较两个实体list是否相等
     * @param exp       预期
     * @param act       实际
     * @return
     */
    private boolean ListMatch(List<? extends BaseModel> exp,List<? extends BaseModel> act){
        if(exp.size()!=act.size()){
            return false;
        }
        for(int i=0;i<exp.size();i++){
            BaseModel bexp=exp.get(i);
            BaseModel bact=act.get(i);
            if(!bexp.getId().equals(bact.getId())){
                return false;
            }
         }
        return true;

    }

    //================================================================订单详情页面

    /**
     * 修改订单状态
     * @throws Exception
     */
    @Test
    public void modifyOrderState() throws Exception{

    }



    //=================================================================线路商品列表

    /**
     * 显示线路商品列表
     * @throws Exception
     */
    @Test
    public void TouristGoodList() throws Exception{
        //当前测试的登录用户
        TouristSupplier touristSupplier=null;

        String json=mockMvc.perform(get("/base/touristGoodList")
                .param("payDate",""+ LocalDateTime.of(2016,3,5,5,5))
                .param("endPayDate",""+LocalDateTime.of(2016,6,7,7,7)))
                .andReturn().getResponse().getContentAsString();



    }







}