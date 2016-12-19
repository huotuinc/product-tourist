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
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.OrderService;
import com.huotu.tourist.service.TouristRouteService;
import com.huotu.tourist.service.TravelerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        
        int pageSize = random.nextInt(100)+10;

        mockMvc.perform(get("/supplier/orderList").param("size",""+pageSize))
                .andExpect(model().attribute("page", new AbstractMatcher<Object>(){
                    @Override
                    public boolean matches(Object o) {
                        return false;
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


}