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

    /**
     * 显示订单列表
     * @throws Exception
     */
    @Test
    public void orderList() throws Exception {
        //
        TouristSupplier currentSupplier = null;
        int pageSize = random.nextInt(100)+10;

        mockMvc.perform(get("/supplier/orderList").param("size",""+pageSize))
                .andExpect(model().attribute("page", new AbstractMatcher<Object>(){
                    @Override
                    public boolean matches(Object o) {
                        return orderService.supplieOrders(currentSupplier, new PageRequest(0, pageSize), null, null, null, null
                                ,null,null,null,null).equals(o);
                    }
                }));

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

    /**
     * 显示某个订单的游客列表
     * @throws Exception
     */
    @Test
    public void showOrderTravelerList() throws Exception{

    }

    /**
     * 保存某个订单游客
     * @throws Exception
     */
    @Test
    public void saveOrderTraveler() throws Exception{

    }


}