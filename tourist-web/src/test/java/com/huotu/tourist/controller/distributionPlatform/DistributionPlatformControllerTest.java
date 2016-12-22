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
import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.PurchaserProductSetting;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.huotu.tourist.controller.distributionPlatform.DistributionPlatformController.ROWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
                , LocalDateTimeFormatter.toLocalDateTime("2016-12-12 10:00:00"));
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
                .param("startPayDate", "2016-12-12 08:00:00")
                .param("endPayDate", "2016-12-12 23:00:00")

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
                .param("startPayDate", "2016-12-12 08:00:00")
                .param("endPayDate", "2016-12-12 23:00:00")
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
        int pageSize = 1;
        int pageNo = 1;
        PurchaserPaymentRecord purchaserPaymentRecord = createPurchaserPaymentRecord(null
                , LocalDateTimeFormatter.toLocalDateTime("2016-12-12 10:00:00"));
        String json = mockMvc.perform(get("/distributionPlatform/exportPurchaserPaymentRecord")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "2016-12-12 08:00:00")
                .param("endPayDate", "2016-12-12 23:00:00")
                .param("telPhone", "" + purchaserPaymentRecord.getTouristBuyer().getTelPhone())
                .param("buyerDirector", "" + purchaserPaymentRecord.getTouristBuyer().getBuyerDirector())
                .param("buyerName", "" + purchaserPaymentRecord.getTouristBuyer().getBuyerName())
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        // TODO: 2016/12/21
    }

    @Test
    public void purchaserProductSettingList() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        PurchaserProductSetting productSetting = createPurchaserProductSetting(UUID.randomUUID().toString());
        String json = mockMvc.perform(get("/distributionPlatform/purchaserProductSettingList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", productSetting.getName().toString())
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("buyerName").equals(productSetting.getName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("名称查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/purchaserProductSettingList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("未输入名称查找到相关的数据");

        createPurchaserProductSetting(UUID.randomUUID().toString());
        json = mockMvc.perform(get("/distributionPlatform/purchaserProductSettingList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("下一页查找到相关的数据");

    }

    @Test
    public void touristGoodList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        TouristGood touristGood = createTouristGood("123", null, null, null, null);
        String json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", touristGood.getTouristName())
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("touristName").equals(touristGood.getTouristName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("名称查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", touristGood.getTouristSupplier().getSupplierName())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("supplierName").equals(touristGood.getTouristSupplier().getSupplierName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("供应商名称查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("activityTypeId", "" + touristGood.getTouristType().getId())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("activityType").equals(touristGood.getActivityType().getActivityName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("活动类型查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristTypeId", "" + touristGood.getTouristType().getId())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("touristType").equals(touristGood.getTouristType().getTypeName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("线路类型查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristCheckState", "" + touristGood.getTouristCheckState())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (((Map) m.get("touristCheckState")).get("code").equals(touristGood.getTouristCheckState().getCode())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("审核类型查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", touristGood.getTouristName())
                .param("supplierName", touristGood.getTouristSupplier().getSupplierName())
                .param("touristTypeId", "" + touristGood.getTouristType().getId())
                .param("activityTypeId", "" + touristGood.getTouristType().getId())
                .param("touristCheckState", "" + touristGood.getTouristCheckState())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("查询条件全部精确查询条件查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有查询条件查找到相关的数据");


        createTouristGood(null, null, null, null, null);
        json = mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有查询条件进行第二页查找到相关的数据");
    }

    @Test
    public void activityTypeList() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        ActivityType activityType = createActivityType(UUID.randomUUID().toString());
        String json = mockMvc.perform(get("/distributionPlatform/activityTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", activityType.getActivityName())
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("activityName").equals(activityType.getActivityName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("名称查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/activityTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("未输入名称查找到相关的数据");

        createActivityType(UUID.randomUUID().toString());
        json = mockMvc.perform(get("/distributionPlatform/activityTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有查询条件进行第二页查找到相关的数据");

    }


    @Test
    public void touristTypeList() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        TouristType type = createTouristType(UUID.randomUUID().toString());
        String json = mockMvc.perform(get("/distributionPlatform/touristTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", type.getTypeName())
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("typeName").equals(type.getTypeName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("名称查找到相关的数据");

        json = mockMvc.perform(get("/distributionPlatform/touristTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("未输入名称查找到相关的数据");

        createTouristType(UUID.randomUUID().toString());
        json = mockMvc.perform(get("/distributionPlatform/touristTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有查询条件进行第二页查找到相关的数据");
    }


    @Test
    public void touristOrders() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        TouristOrder order = createTouristOrder(null, null, null, null, LocalDateTimeFormatter.toLocalDateTime
                ("2016-12-12 01:00:00"), LocalDateTimeFormatter.toLocalDateTime("2016-12-12 05:00:00"), null);
        String json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有查询条件查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("orderNo", "" + order.getOrderNo())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("orderNo").equals(order.getOrderNo())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定订单号查询到相应数据列表");


        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", order.getTouristGood().getTouristName())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("touristName").equals(order.getTouristGood().getTouristName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定线路名称查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerName", order.getTouristBuyer().getBuyerName())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("buyerName").equals(order.getTouristBuyer().getBuyerName())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定采购商名称查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("tel", "" + order.getTouristBuyer().getTelPhone())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("指定采购商电话查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("orderState", order.getOrderState().getCode() + "")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("orderStateCode").equals(order.getOrderState().getCode())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定订单状态查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("payType", "" + order.getPayType().getCode())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("payTypeCode").equals(order.getPayType().getCode())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定支付类型查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("orderDate", "2016-12-12 00:00:00")
                .param("endOrderDate", "2016-12-12 22:00:00")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("指定创建时间范围查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("payDate", "2016-12-12 00:00:00")
                .param("endPayDate", "2016-12-12 22:00:00")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("指定支付时间范围查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("orderNo", order.getOrderNo())
                .param("touristName", order.getTouristGood().getTouristName())
                .param("buyerName", order.getTouristBuyer().getBuyerName())
                .param("tel", order.getTouristBuyer().getTelPhone())
                .param("orderState", LocalDateTimeFormatter.toStr(order.getCreateTime()))
                .param("payType", order.getPayType().getCode() + "")
                .param("orderDate", "2012-12-12 00:00:00")
                .param("endOrderDate", "2012-12-12 22:00:00")
                .param("payDate", "2012-12-12 00:00:00")
                .param("endPayDate", "2012-12-12 22:00:00")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("id").equals(order.getId())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("查询条件全部指定查询到相应数据列表");

        createTouristOrder(null, null, null, null, null, null, null);
        json = mockMvc.perform(get("/distributionPlatform/touristOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有筛选条件的分页查询到相应数据列表");

    }

    @Test
    public void exportTouristOrdersOrders() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        PurchaserPaymentRecord purchaserPaymentRecord = createPurchaserPaymentRecord(null
                , LocalDateTimeFormatter.toLocalDateTime("2016-12-12 10:00:00"));
        String json = mockMvc.perform(get("/distributionPlatform/exportTouristOrdersOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        // TODO: 2016/12/21
    }

    @Test
    public void settlementSheetList() throws Exception {
        int pageSize = 1;
        int pageNo = 1;
        SettlementSheet settlementSheet = createSettlementSheet(LocalDateTimeFormatter.toLocalDateTime("2016-12-12 " +
                "08:00:00"), null, "ss");
        String json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        List<Map> list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("没有查询条件查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", settlementSheet.getTouristOrder().getTouristGood().getTouristSupplier
                        ().getSupplierName())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        boolean flag = false;
        for (Map m : list) {
            if (m.get("id").equals(settlementSheet.getId())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("供应商名称查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("platformChecking", settlementSheet.getPlatformChecking().getCode() + "")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("id").equals(settlementSheet.getId())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定状态查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("createTime", "2016-12-12 08:00:00")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("id").equals(settlementSheet.getId())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("指定日期到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", settlementSheet.getTouristOrder().getTouristGood().getTouristSupplier()
                        .getSupplierName())
                .param("platformChecking", settlementSheet.getPlatformChecking().getCode().toString())
                .param("createTime", "2016-12-12 08:00:00")
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("id").equals(settlementSheet.getId())) {
                flag = true;
            }
        }
        assertThat(flag).isTrue().as("全部指定查询条件查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", UUID.randomUUID().toString())
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        flag = false;
        for (Map m : list) {
            if (m.get("id").equals(settlementSheet.getId())) {
                flag = true;
            }
        }
        assertThat(flag).isFalse().as("错误的供应商名称查询不到到相应数据列表");

        createSettlementSheet(null, null, null);
        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 1)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isGreaterThan(0).as("分页无查询条件查询到相应数据列表");

        json = mockMvc.perform(get("/distributionPlatform/settlementSheetList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo + 2)
        ).andReturn().getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        map = objectMapper.readValue(json, Map.class);
        list = (List<Map>) map.get(ROWS);
        assertThat(list.size()).isEqualTo(0).as("分页无查询条件查询不到到相应数据列表");

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


    //    --------------------------------------------------------
    @Test
    public void addTouristSupplierAndUpdateSupplier() throws Exception {
        String supplierName = UUID.randomUUID().toString();
        String adminAccount = UUID.randomUUID().toString();
        String adminPassword = UUID.randomUUID().toString();
        String contacts = UUID.randomUUID().toString();
        String contactNumber = "13000000000";
        String address = null;
        String remarks = UUID.randomUUID().toString();
        int status = mockMvc.perform(post("/distributionPlatform/addSupplier")
                .param("supplierName", supplierName)
                .param("adminAccount", adminAccount)
                .param("adminPassword", adminPassword)
                .param("contacts", contacts)
                .param("contactNumber", contactNumber)
                .param("address", address)
                .param("remarks", remarks)
        ).andReturn().getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK).as("添加相应成功");
        TouristSupplier touristSupplier = touristSupplierRepository.findByAdminAccount(adminAccount);
        assertThat(touristSupplier).isNotNull().as("查找到对应实体");
        assertThat(touristSupplier.getAdminAccount()).isEqualTo(adminAccount);



    }


}