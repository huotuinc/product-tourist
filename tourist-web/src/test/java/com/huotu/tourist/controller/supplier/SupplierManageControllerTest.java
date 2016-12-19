package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.AbstractMatcher;
import com.huotu.tourist.WebTest;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SupplierManageControllerTest extends WebTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void orderList() throws Exception {
        //
        TouristSupplier currenSupplier = null;
        int pageSize = random.nextInt(100)+10;

        mockMvc.perform(get("/supplier/orderList").param("size",""+pageSize))
                .andExpect(model().attribute("page", new AbstractMatcher<Object>(){
                    @Override
                    public boolean matches(Object o) {
                        return orderService.supplieOrders(currenSupplier,new PageRequest(0,pageSize),null,null,null,null
                                ,null,null,null,null).equals(o);
                    }
                }));

    }

}