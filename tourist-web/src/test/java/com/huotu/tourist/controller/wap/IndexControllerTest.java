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