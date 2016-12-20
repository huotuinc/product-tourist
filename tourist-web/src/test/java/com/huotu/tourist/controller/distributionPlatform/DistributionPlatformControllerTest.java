/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.distributionPlatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.tourist.WebTest;
import com.huotu.tourist.common.*;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.huotu.tourist.controller.distributionPlatform.DistributionPlatformController.ROWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by lhx on 2016/12/17.
 */
public class DistributionPlatformControllerTest extends WebTest {

    @Test
    public void supplierList() throws Exception {
        String name = UUID.randomUUID().toString();
        createTouristSupplier(name);
        createTouristSupplier(name);
        int pageSize = 1;
        int pageNo = 1;
        String json = mockMvc.perform(get("/distributionPlatform/supplierList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", "" + name)
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("supplierName").equals(name)) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("找到添加的数据完成");

        json = mockMvc.perform(get("/distributionPlatform/supplierList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
                .param("name", name)
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);

        assertThat(list.size()).isGreaterThan(0).as("第二页数据加载完成，分页验证完成");
    }


    @Test
    public void buyerList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("buyerDirector", "" + UUID.randomUUID().toString())
                .param("telPhone", "" + UUID.randomUUID().toString())
                .param("buyerCheckState", "" + BuyerCheckStateEnum.CheckFinish)
        ).andReturn().getResponse().getContentAsString();
    }


    @Test
    public void purchaserPaymentRecordList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;


        String json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "" + LocalDate.now())
                .param("endPayDate", "" + LocalDate.MAX)
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("buyerDirector", "" + UUID.randomUUID().toString())
                .param("telPhone", "" + UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();


    }

    @Test
    public void exportPurchaserPaymentRecord() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/exportPurchaserPaymentRecord")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "" + LocalDate.now())
                .param("endPayDate", "" + LocalDate.MAX)
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("buyerDirector", "" + UUID.randomUUID().toString())
                .param("telPhone", "" + UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void purchaserProductSettingList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/purchaserProductSettingList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", "" + UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void touristGoodList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", "" + UUID.randomUUID().toString())
                .param("supplierName", "" + UUID.randomUUID().toString())
                .param("touristTypeId", "" + 1)
                .param("activityTypeId", "" + 1)
                .param("touristCheckState", "" + TouristCheckStateEnum.CheckFinish)
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void recommendTouristGoodList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/recommendTouristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", "" + UUID.randomUUID().toString())
                .param("supplierName", "" + UUID.randomUUID().toString())
                .param("touristTypeId", "" + 1)
                .param("activityTypeId", "" + 1)
                .param("touristCheckState", "" + TouristCheckStateEnum.CheckFinish)
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void activityTypeList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/activityTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", "" + UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();
    }


    @Test
    public void touristTypeList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/touristTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", "" + UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();
    }


    @Test
    public void supplierOrders() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/supplierOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("orderNo", "" + UUID.randomUUID().toString())
                .param("touristName", "" + UUID.randomUUID().toString())
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("tel", "" + UUID.randomUUID().toString())
                .param("orderState", "" + OrderStateEnum.Finish)
                .param("payType", "" + PayTypeEnum.Alipay)
                .param("orderDate", "" + LocalDate.now())
                .param("endOrderDate", "" + LocalDate.MAX)
                .param("payDate", "" + LocalDate.now())
                .param("endPayDate", "" + LocalDate.MAX)
                .param("touristDate", "" + LocalDate.now())
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void settlementSheetList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", "" + UUID.randomUUID().toString())
                .param("platformChecking", "" + SettlementStateEnum.CheckFinish)
                .param("createTime", "" + LocalDate.now())
        ).andReturn().getResponse().getContentAsString();
        JsonPath.read(json, "$.rows");
    }

    @Test
    public void presentRecordList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        String json = mockMvc.perform(get("/distributionPlatform/presentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", "" + pageNo)
                .param("presentState", "" + PresentStateEnum.CheckFinish)
                .param("createTime", "" + LocalDate.now())
        ).andReturn().getResponse().getContentAsString();
    }


}