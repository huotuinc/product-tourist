package com.huotu.tourist.controller.wap;

import com.huotu.tourist.AbstractPlatformTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lhx on 2017/1/22.
 */
public class IndexControllerTest extends AbstractPlatformTest {
    @Test
    public void index() throws Exception {

    }

    @Test
    public void goodInfo() throws Exception {

    }

    @Test
    public void procurementGood() throws Exception {

    }

    @Test
    public void addOrderInfo1() throws Exception {

    }

    @Test
    public void cancelOrder() throws Exception {

    }

    @Test
    public void toProcurementPayPage() throws Exception {

    }

    @Test
    public void newTouristList() throws Exception {

    }

    @Test
    public void activityTypeList() throws Exception {

    }

    @Test
    public void activityTypeGoods() throws Exception {

    }

    @Test
    public void activityTourist() throws Exception {

    }

    @Test
    public void supplierGoods() throws Exception {

    }

    @Test
    public void supplierTourist() throws Exception {

    }

    @Test
    public void destinationList() throws Exception {

    }

    @Test
    public void destinationGoods() throws Exception {

    }

    @Test
    public void destinationTourist() throws Exception {

    }

    @Test
    public void recommendGoods() throws Exception {

    }

    @Test
    public void recommendTourist() throws Exception {

    }

    @Test
    public void buyerApply() throws Exception {

    }

    @Test
    public void addTouristBuyer() throws Exception {

    }

    @Test
    public void sendCode() throws Exception {

    }

    @Test
    public void verifyCode() throws Exception {

    }

    @Test
    public void addOrderInfo() throws Exception {
        mockMvc.perform(post("/wap/addOrderInfo")
                .session(session)
                .param("goodId", "1")
                .param("routeId", "1")
                .param("remark", "1")
                .param("travelers", "[{\"travelerType\":0,\"name\":\"123\",\"telPhone\":\"123\",\"number\":\"123\",\"age\":12,\"sex\":0}]")
        ).andDo(print())
                .andExpect(status().isOk());
    }

}