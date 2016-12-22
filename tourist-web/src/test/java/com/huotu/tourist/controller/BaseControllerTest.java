package com.huotu.tourist.controller;

import com.huotu.tourist.WebTest;
import com.huotu.tourist.entity.TouristOrder;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by lhx on 2016/12/22.
 */
public class BaseControllerTest extends WebTest {


    @Test
    public void modifyOrderRemarks() throws Exception {
        TouristOrder order = createTouristOrder(null, null, null, null, null, null, null);
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

    }

    @Test
    public void modifyOrderTouristDate() throws Exception {

    }

    @Test
    public void showTouristGood() throws Exception {

    }

    @Test
    public void modifyTouristGoodState() throws Exception {

    }


}