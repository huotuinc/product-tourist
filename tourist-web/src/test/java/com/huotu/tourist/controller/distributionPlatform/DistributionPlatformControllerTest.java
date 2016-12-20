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
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.TouristBuyer;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void testDateTimeFormatter() throws ParseException {
        System.out.println(LocalDateTimeFormatter.toStr(LocalDateTime.now()));
    }

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


        json = mockMvc.perform(get("/distributionPlatform/supplierList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("name为空也能够查询列表");
    }


    @Test
    public void buyerList() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        String buyerName = UUID.randomUUID().toString().replace("-", "");
        String buyerDirector = UUID.randomUUID().toString().replace("-", "");
        String telPhone = "13000000000";
        BuyerCheckStateEnum checkStateEnum = randomBuyerCheckState();
        createTouristBuyer(null, null, null, checkStateEnum);
        String json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerCheckState", "" + BuyerCheckStateEnum.CheckFinish)
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            Map checkState = (Map) m.get("checkState");
            if (checkState.get("code").equals(checkStateEnum.getCode())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("找到添加的数据完成");

        createTouristBuyer(null, buyerDirector, null, null);
        json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerDirector", "" + buyerDirector)
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("buyerDirector").equals(buyerDirector)) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("找到添加的数据完成");

        createTouristBuyer(null, null, telPhone, null);
        json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("telPhone", "" + UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("telPhone").equals(telPhone)) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("找到添加的数据完成");

        createTouristBuyer(buyerName, null, null, null);
        json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerName", "" + buyerName)

        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("buyerName").equals(buyerName)) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("找到添加的数据完成");

        TouristBuyer touristBuyer = createTouristBuyer(null, null, null, null);
        json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerDirector", "" + touristBuyer.getBuyerDirector())
                .param("buyerName", "" + touristBuyer.getBuyerName())
                .param("telPhone", "" + touristBuyer.getTelPhone())
                .param("buyerCheckState", "" + touristBuyer.getCheckState())
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("buyerDirector").equals(touristBuyer.getBuyerDirector())
                    && m.get("buyerName").equals(touristBuyer.getBuyerName())
                    && m.get("telPhone").equals(touristBuyer.getTelPhone())
                    && ((Map) m.get("buyerCheckState")).get("code").equals(touristBuyer.getCheckState().getCode())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("找到添加的数据完成");


        json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有条件也能够查询列表");


        json = mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("能够分页查询列表");
    }

    @Test
    public void purchaserPaymentRecordList() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        PurchaserPaymentRecord purchaserPaymentRecord = createPurchaserPaymentRecord(null
                , null);
        String json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerName", "" + purchaserPaymentRecord.getTouristBuyer().getBuyerName())
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("buyerName").equals(purchaserPaymentRecord.getTouristBuyer().getBuyerName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("采购商名称查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerDirector", "" + purchaserPaymentRecord.getTouristBuyer().getBuyerDirector())
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("buyerDirector").equals(purchaserPaymentRecord.getTouristBuyer().getBuyerDirector())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("采购商负责人查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("telPhone", "" + purchaserPaymentRecord.getTouristBuyer().getTelPhone())
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("telPhone").equals(purchaserPaymentRecord.getTouristBuyer().getTelPhone())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("采购商电话查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "" + LocalDateTime.now())
                .param("endPayDate", "" + LocalDate.MAX)

        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("payDate").equals(purchaserPaymentRecord.getPayDate())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("支付时间查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "" + LocalDate.now())
                .param("endPayDate", "" + LocalDate.MAX)
                .param("telPhone", "" + purchaserPaymentRecord.getTouristBuyer().getTelPhone())
                .param("buyerDirector", "" + purchaserPaymentRecord.getTouristBuyer().getBuyerDirector())
                .param("buyerName", "" + purchaserPaymentRecord.getTouristBuyer().getBuyerName())
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("条件筛选全部筛选也能查找到相关的数据列表");


        json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("筛选条件不选也能查询列表相关的数据");

        createPurchaserPaymentRecord(null, null);
        json = mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("筛选条件不选下一页也能查询列表相关的数据");


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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
    }


}