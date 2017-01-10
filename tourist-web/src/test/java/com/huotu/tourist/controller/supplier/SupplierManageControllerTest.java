/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.AbstractSupplierTest;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.TouristRouteService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by Administrator on 2016/12/17.
 */
//@Transactional
public class SupplierManageControllerTest extends AbstractSupplierTest {

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
        TouristGood good=createTouristGood("slt",null,null,null,supplier);
        TouristOrder order=createTouristOrder(good,null,orderNo,null,null,null,null,null);


        String json=mockMvc.perform(get("/base/touristOrders")
                .param("orderId",orderNo)
                .param(pageParameterName,"0")
                .param(sizeParameterName,"10")
                .param("sort","id,asc")
                .session(session))
                .andExpect(jsonPath("$.rows").isArray())
                .andReturn().getResponse().getContentAsString();

        //实际
        Long idActual=Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
        assertThat(order.getId().equals(idActual)).isTrue().as("订单号校验");



        //预期
        order = createTouristOrder(createTouristGood("wy", null, null, null, supplier), null,null, null
                , null, null, null, null);

        json=mockMvc.perform(get("/base/touristOrders")
                .param("touristName","wy")
                .session(session)
//                .param(pageParameterName,"0")
//                .param(sizeParameterName,"10")
                ).andReturn().getResponse().getContentAsString();

        //实际
        idActual= Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
        assertThat(order.getId().equals(idActual)).isTrue().as("线路名称校验");

        //预期
        order=createTouristOrder(good,createTouristBuyer("wy",null,null,null),null,null,null,null,null,null);

        json=mockMvc.perform(get("/base/touristOrders")
                .param("buyerName","wy").session(session))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
        assertThat(order.getId().equals(idActual)).isTrue().as("购买人校验");

        //预期
        List<TouristOrder> touristOrders=new ArrayList<>();
        for(int i=0;i<3;i++){
            touristOrders.add(createTouristOrder(good,null,null,null,
                    LocalDateTime.of(2016,i*3+1,1,1,1),null,null,null));
        }
        order=touristOrders.get(1);
        json=mockMvc.perform(get("/base/touristOrders")
                .param("orderDate",LocalDateTimeFormatter.toStr(LocalDateTime.of(2016,3,5,5,5)))
                .param("endOrderDate",LocalDateTimeFormatter.toStr(LocalDateTime.of(2016,6,7,7,7)))
                .session(session))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
        assertThat(order.getId().equals(idActual)).isTrue().as("下单时间校验");

        //预期
        touristOrders=new ArrayList<>();
        for(int i=0;i<3;i++){
            touristOrders.add(createTouristOrder(good,null,null,null,null,
                    LocalDateTime.of(2016,i*3+1,1,1,1),null,null));
        }
        order=touristOrders.get(1);
        json=mockMvc.perform(get("/base/touristOrders")
                .param("payDate",LocalDateTimeFormatter.toStr(LocalDateTime.of(2016,3,5,5,5)))
                .param("endPayDate",LocalDateTimeFormatter.toStr(LocalDateTime.of(2016,6,7,7,7)))
                .session(session))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
        assertThat(order.getId().equals(idActual)).isTrue().as("支付时间校验");



        //预期
        TouristGood touristGood = createTouristGood("slt", null, null, null, supplier);
        TouristRoute touristRoute=createTouristRoute(null,touristGood,LocalDateTime.of(2016,12,10,0,0,0)
                ,LocalDateTime.of(2017,10,10,0,0),0);
        order=createTouristOrder(touristGood,null,null,null,null,null,null,null);
        Traveler traveler=createTraveler(touristRoute,order);
        json=mockMvc.perform(get("/base/touristOrders")
                .param("touristDate",LocalDateTimeFormatter.toStr(LocalDateTime.of(2015,11,10,0,0,0)))
                .param("endTouristDate",LocalDateTimeFormatter.toStr(LocalDateTime.of(2018,11,10,0,0,0)))
                .session(session))
                .andReturn().getResponse().getContentAsString();

        //实际
        idActual= Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
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
        TouristGood good=createTouristGood("slt",null,null,null,supplier);
        TouristOrder order=createTouristOrder(good,null,null,null,null,null,null,null);
        List<TouristRoute> routes=new ArrayList<>();
        for(int i=0;i<10;i++){
            TouristRoute route=createTouristRoute(null,good,LocalDateTime.now(),null,random.nextInt(50)+20);
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
                .param("orderId",""+order.getId())
                .param("routeId",""+ownRoute.getId()).session(session))
                .andReturn().getResponse().getContentAsString();
        //实际
        List<TouristRouteModel> touristRouteModelsActual=JsonPath.read(result,"$.data");
        for(int i=0;i<touristRouteModelsActual.size();i++){
            Long exp=touristRouteModelsExpect.get(i).getId();
            Long act=Long.valueOf(JsonPath.read(result,"$.data["+i+"].id").toString());
            assertThat(exp.equals(act)).isTrue().as("其他路线校验");
        }



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
        mockMvc.perform(post("/supplier/modifyOrderTouristDate")
                .param("formerId",""+routeF.getId())
                .param("laterId",""+routeL.getId())
                .session(session));
        Traveler travelerA=travelerRepository.findOne(traveler.getId());
        assertThat(routeL.getFromDate().equals(travelerA.getRoute().getFromDate())).isTrue().as("出行人数校验");


    }

    /**
     * 查看订单详情测试
     * @throws Exception
     */
//    @Test
//    public void showOrder() throws Exception{
//
//        //预期
//        TouristOrder orderExp=createTouristOrder(null,null,"1234",null,null,null,null,null);
//
//        TouristRoute routeExp=createTouristRoute("4567",null,null,null,10);
//
//        List<Traveler> travelersExp=new ArrayList<>();
//        for(int i=0;i<2;i++){
//            travelersExp.add(createTraveler(routeExp,orderExp));
//        }
//
//        mockMvc.perform(get("/supplier/showOrder").param("id",""+orderExp.getId()))
//                .andExpect(model().attribute("order", new AbstractMatcher<Object>(){
//                    @Override
//                    public boolean matches(Object o) {
//                        TouristOrder orderAct=(TouristOrder)o;
//                        return orderExp.getId().equals(orderAct.getId());
//                    }
//                })).andExpect(model().attribute("route", new AbstractMatcher<Object>() {
//
//            @Override
//            public boolean matches(Object item) {
//                TouristRoute routeAct=(TouristRoute)item;
//                return routeExp.getId().equals(routeAct.getId());
//            }
//        })).andExpect(model().attribute("travelers", new AbstractMatcher<Object>() {
//
//            @Override
//            public boolean matches(Object item) {
//                List<Traveler> travelersAct=(List<Traveler>)item;
//                return ListMatch(travelersExp,travelersAct);
//            }
//        }));
//    }

    /**
     * 比较两个实体list是否相等,只匹配实体的id
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
        //预期
        TouristGood touristGood=createTouristGood("slt",null,null,null,supplier);

        String json=mockMvc.perform(get("/base/touristGoodList").session(session))
                .andReturn().getResponse().getContentAsString();

        //实际
        Long idActual= Long.valueOf(JsonPath.read(json,"$.rows[0].id").toString());
        assertThat(touristGood.getId().equals(idActual)).isTrue().as("供应商校验");
    }

    /**
     * 保存线路商品
     * @throws Exception
     */
    @Test
    public void saveTouristGood() throws Exception{
        //当前测试的登录用户
        ActivityType activityType=createActivityType("sltActivity");
        TouristType touristType=createTouristType("sltTourist");
        Address destination=new Address("浙江省","杭州市","滨江区");
        Address placeOfDeparture=new Address("浙江省","宁波市","鄞州区");
        Address travelledAddress=new Address("浙江省","杭州市","上城区");
        String name="sltname";
        String touristFeatures="线路特色";
        TouristCheckStateEnum checkState=TouristCheckStateEnum.CheckFinish;
        BigDecimal price=new BigDecimal(20);
        BigDecimal childrenDiscount=new BigDecimal(2);
        BigDecimal rebate=new BigDecimal(30);
        String receptionPerson="第接人史利挺";
        String receptionTelephone="13012345678";
        String eventDetails="活动详情";
        String beCareful="注意事项";
        String touristImgUri="图片";
        Integer maxPeople=40;
        List<String> images=new ArrayList<>(Arrays.asList(new String[]{"11","22"}));
        TouristGood touristGood=createTouristGood(name,activityType,touristType, checkState,supplier
                ,touristFeatures,destination,placeOfDeparture,travelledAddress,price,childrenDiscount,rebate
                ,receptionPerson,receptionTelephone,eventDetails,beCareful,touristImgUri,maxPeople,8, images);

        TouristRoute[] touristRoutes=new TouristRoute[2];

        for(int i=0;i<2;i++){
            TouristRoute touristRoute=createTouristRoute(""+i,touristGood,LocalDateTime.now(),LocalDateTime.now()
                    ,touristGood.getMaxPeople());
        }

        mockMvc.perform(get("/supplier/saveTouristGood")
                .param("id",""+ touristGood.getId())
                .param("touristName","modify")
                .param("routes","[\n{\n\"routeNo\": \"48954\",\n\"fromDate\": \"2016-12-12 00:00:00\"\n},\n{\n \"routeNo\": \"1111\",\n\"fromDate\": \"2017-12-12 00:00:00\"\n}\n]")
                .param("activityTypeId",""+touristGood.getActivityType().getId())
                .param("touristTypeId",""+touristGood.getTouristType().getId())
                .param("destination","浙江省/杭州市/江干")
                .param("travelledAddress","浙江省/杭州市/滨江区")
                .param("placeOfDeparture","浙江省/杭州市/下沙")
                .param("maxPeople","20")
                .param("touristFeatures","touristFeatures")
                .param("price","15.2")
                .param("childrenDiscount","3")
                .param("rebate","3")
                .param("receptionPerson","receptionPerson")
                .param("receptionTelephone","18036547894")
                .param("eventDetails","活动详情")
                .param("beCareful","注意事项")
                .param("touristImgUri","www.baidu.com")
                .param("photos","11,334")
                .session(session)
                );
        TouristGood goodsAct=touristGoodRepository.findOne(touristGood.getId());
        assertThat(goodsAct.getTouristName().equals("modify")).isTrue().as("名称校验");

        List<TouristRoute> routesAct=touristRouteRepository.findByGood(touristGood);
        LocalDateTime formDate=routesAct.get(0).getFromDate();
        System.out.println(formDate.toString());
        assertThat(formDate!=null).isTrue().as("出发日期是否存在");

    }


    @Test
    public void showTouristGoodsList() throws Exception{
        TouristGood touristGood=createTouristGood("slt",null,null,TouristCheckStateEnum.CheckFinish,supplier
                ,null,null,null,null,null,null,null,null,null,null,null,null,20,10,null);
        TouristOrder touristOrder=createTouristOrder(touristGood,null,null, OrderStateEnum.Finish,LocalDateTime.now()
                , LocalDateTime.now(), PayTypeEnum.Alipay,"");

        List<TouristRoute> routes=new ArrayList<>();
        for(int i=0;i<3;i++){
            routes.add(createTouristRoute(null,touristGood,LocalDateTime.now(),null,0));
        }
        List<Traveler> travelers=new ArrayList<>();
        for(int i=0;i<5;i++){
           travelers.add(createTraveler(routes.get(2/(i+1)),touristOrder));
        }


        String json=mockMvc.perform(get("/base/touristGoodList")
                .param(pageParameterName,"0")
                .param(sizeParameterName,"10")
                .session(session))
                .andExpect(jsonPath("$.rows").isArray())
                .andReturn().getResponse().getContentAsString();

        //实际
        Long idActual=Long.valueOf(JsonPath.read(json,"$.rows[0].surplus").toString());
        assertThat(routes.size()*touristGood.getMaxPeople()-travelers.size()==idActual).isTrue().as("库存校验");


    };


}