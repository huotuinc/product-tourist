package com.huotu.tourist.controller;

import com.huotu.tourist.AbstractMatcher;
import com.huotu.tourist.WebTest;
import com.huotu.tourist.common.SexEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.Traveler;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Created by lhx on 2016/12/22.
 */
public class BaseControllerTest extends WebTest {


    @Test
    public void modifyOrderRemarks() throws Exception {
        TouristOrder order = createTouristOrder(null, null, null, null, null, null, null,null);
        String remark = UUID.randomUUID().toString();
        int status = mockMvc.perform(post("/base/modifyOrderRemarks")
                .param("id", order.getId().toString())
                .param("remark", remark)
        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("相应成功");
        order = touristOrderRepository.getOne(order.getId());
        assertThat(order.getRemarks()).isEqualTo(remark).as("修改成功");

        status = mockMvc.perform(post("/base/modifyOrderRemarks")
                .param("remark", remark)
        ).andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(HttpStatus.OK).as("修改失败");

        status = mockMvc.perform(post("/base/modifyOrderRemarks")
                .param("id", order.getId().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(HttpStatus.OK).as("修改失败");
    }

    @Test
    public void modifyOrderState() throws Exception {

    }

    @Test
    public void modifyTravelerBaseInfo() throws Exception {
        TouristGood good = createTouristGood(null, null, null, null, null);
        Traveler traveler = createTraveler(createTouristRoute(UUID.randomUUID().toString(), good,
                null, null, 0), createTouristOrder(good, createTouristBuyer(null,null,null,null),
        null, null, null, null,null,
                null));

        String name = UUID.randomUUID().toString();
        SexEnum sex = SexEnum.man;
        String age = UUID.randomUUID().toString();
        String tel = "13030638993";
        String IDNo = "13000000000";
        int status = mockMvc.perform(post("/base/modifyTravelerBaseInfo")
                .param("id", traveler.getId().toString())
                .param("name", name)
                .param("sex", sex.getCode().toString())
                .param("age", age)
                .param("tel", tel)
                .param("IDNo", IDNo)
        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("相应成功");
        traveler = travelerRepository.getOne(traveler.getId());
        assertThat(traveler).isNotNull().as("查找到对应实体");
        assertThat(traveler.getName()).isEqualTo(name);
        assertThat(traveler.getSex()).isEqualTo(sex);
        assertThat(traveler.getAge()).isEqualTo(age);
        assertThat(traveler.getTelPhone()).isEqualTo(tel);
        assertThat(traveler.getIDNo()).isEqualTo(IDNo);

        status = mockMvc.perform(post("/base/modifyTravelerBaseInfo")
                .param("id", traveler.getId().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(HttpStatus.OK).as("系统应当报错");
    }

    @Test
    public void modifyOrderTouristDate() throws Exception {
        TouristGood good = createTouristGood(null, null, null, null, null);
        TouristRoute route1 = createTouristRoute(UUID.randomUUID().toString(), good,
                null, null, 0);
        TouristRoute route2 = createTouristRoute(UUID.randomUUID().toString(), good,
                null, null, 0);
        TouristOrder order = createTouristOrder(good, createTouristBuyer(null, null, null, null), null,null, null,
                null, null, null);
        Traveler traveler = createTraveler(route1, order);
        int status = mockMvc.perform(post("/base/modifyTravelerBaseInfo")
                .param("formerId", route1.getId().toString())
                .param("laterId", route2.getId().toString())

        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("相应成功");
        traveler = travelerRepository.getOne(traveler.getId());
        assertThat(traveler.getRoute().getId()).isEqualTo(route2.getId()).as("修改成功");

        status = mockMvc.perform(post("/base/modifyTravelerBaseInfo")
                .param("formerId", route2.getId().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(HttpStatus.OK).as("系统应当报错");
    }

    @Test
    public void showTouristGood() throws Exception {
        TouristGood touristGood=createTouristGood("slt", null,null,TouristCheckStateEnum.CheckFinish
                ,null);

        TouristRoute touristRouteExpect=createTouristRoute(null,touristGood,null,null,10);
        //商品预期
        TouristGood touristGoodExpect=touristGoodRepository.findOne(touristRouteExpect.getId());

        mockMvc.perform(get("/base/showTouristGood").param("id",""+touristGoodExpect.getId()))
                .andExpect(model().attribute("good", new AbstractMatcher<Object>(){
                    @Override
                    public boolean matches(Object o) {
                        TouristGood touristGoodActual=(TouristGood)o;
                        return touristGoodExpect.getId().equals(touristGoodActual.getId());
                    }
                })).andExpect(model().attribute("routes", new AbstractMatcher<Object>() {

            @Override
            public boolean matches(Object item) {
                List<TouristRoute> touristRoutes=(List<TouristRoute>)item;
                TouristRoute touristRouteActual= touristRoutes.get(0);
                return touristRouteExpect.getId().equals(touristRouteActual.getId());
            }
        }));

    }

    @Test
    public void modifyTouristGoodState() throws Exception {
        TouristGood touristGood = createTouristGood(null, null, null, TouristCheckStateEnum.NotChecking, null);

        int status = mockMvc.perform(post("/base/modifyTouristGoodState")
                .param("id", touristGood.getId().toString())
                .param("checkState", TouristCheckStateEnum.CheckFinish.getCode().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(HttpStatus.BAD_REQUEST).as("相应正确");

        // TODO: 2016/12/22 模拟平台登陆

        status = mockMvc.perform(post("/base/modifyTouristGoodState")
                .param("id", touristGood.getId().toString())
                .param("checkState", TouristCheckStateEnum.CheckFinish.getCode().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("相应成功");

        status = mockMvc.perform(post("/base/modifyTouristGoodState")
                .param("id", touristGood.getId().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(HttpStatus.OK).as("缺少参数系统报错相应");

        // TODO: 2016/12/22 模拟供应商登陆
        status = mockMvc.perform(post("/base/modifyTouristGoodState")
                .param("id", touristGood.getId().toString())
                .param("checkState", TouristCheckStateEnum.Recycle.getCode().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("相应成功");

        status = mockMvc.perform(post("/base/modifyTouristGoodState")
                .param("id", touristGood.getId().toString())
        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("缺少参数系统报错相应");


    }


}