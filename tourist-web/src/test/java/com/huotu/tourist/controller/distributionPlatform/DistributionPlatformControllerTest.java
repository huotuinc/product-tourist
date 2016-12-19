package com.huotu.tourist.controller.distributionPlatform;

import com.huotu.tourist.AbstractMatcher;
import com.huotu.tourist.WebTest;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Created by lhx on 2016/12/17.
 */
public class DistributionPlatformControllerTest extends WebTest {

    @Test
    public void supplierList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/supplierList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", "" + UUID.randomUUID().toString())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void buyerList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/buyerList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("buyerName", "" + UUID.randomUUID().toString())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void purchaserPaymentRecordList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/purchaserPaymentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "" + LocalDate.now())
                .param("endPayDate", "" + LocalDate.MAX)
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("buyerDirector", "" + UUID.randomUUID().toString())
                .param("telPhone", "" + UUID.randomUUID().toString())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void exportPurchaserPaymentRecord() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/exportPurchaserPaymentRecord")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("startPayDate", "" + LocalDate.now())
                .param("endPayDate", "" + LocalDate.MAX)
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("buyerDirector", "" + UUID.randomUUID().toString())
                .param("telPhone", "" + UUID.randomUUID().toString())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void purchaserProductSettingList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/exportPurchaserPaymentRecord")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("name", "" + UUID.randomUUID().toString())

        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void touristGoodList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/touristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", "" + UUID.randomUUID().toString())
                .param("touristTypeId", "" + 1)
                .param("activityTypeId", "" + 1)
                .param("touristCheckState", "" + TouristCheckStateEnum.CheckFinish)

        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void recommendTouristGoodList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/recommendTouristGoodList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("touristName", "" + UUID.randomUUID().toString())
                .param("touristTypeId", "" + 1)
                .param("activityTypeId", "" + 1)
                .param("touristCheckState", "" + TouristCheckStateEnum.CheckFinish)

        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void activityTypeList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/activityTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }


    @Test
    public void touristTypeList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/touristTypeList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void supplierOrders() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/supplierOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("orderNo", "" + UUID.randomUUID().toString())
                .param("touristName", "" + UUID.randomUUID().toString())
                .param("buyerName", "" + UUID.randomUUID().toString())
                .param("orderState", "" + OrderStateEnum.Finish)
                .param("startCreateTime", "" + LocalDate.now())
                .param("endCreateTime", "" + LocalDate.MAX)
                .param("startPayTime", "" + LocalDate.now())
                .param("endPayTime", "" + LocalDate.MAX)
                .param("fromDate", "" + LocalDate.now())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void settlementSheetList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/supplierOrders")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", "" + UUID.randomUUID().toString())
                .param("platformChecking", "" + SettlementStateEnum.CheckFinish)
                .param("createTime", "" + LocalDate.now())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }

    @Test
    public void presentRecordList() throws Exception {
        int pageSize = random.nextInt(100) + 10;
        int pageNo = random.nextInt(10) + 1;
        mockMvc.perform(get("/distributionPlatform/presentRecordList")
                .param("pageSize", "" + pageSize)
                .param("pageNo", "" + pageNo)
                .param("supplierName", "" + pageNo)
                .param("presentState", "" + PresentStateEnum.CheckFinish)
                .param("createTime", "" + LocalDate.now())
        )
                .andExpect(model().attribute("page", new AbstractMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        return false;
                    }
                }));
    }


}