package com.huotu.tourist.service;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 结算测试
 * Created by slt on 2017/2/6.
 */
public class SettlementSheetServiceTest extends ServiceBaseTest {

    @Test
    public void settleOrderTest() throws Exception {
        long days = connectMallService.getServiceDays();

        Random random = new Random();

        List<TouristSupplier> suppliers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            suppliers.add(createTouristSupplier("supplier" + i));
        }

        List<TouristGood> goods = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            goods.add(createTouristGood("goods" + i, null, null, null, suppliers.get(i), null, null, null, null
                    , null, null, null, null, null, null, null, null, 11, null, null));
        }

        //key:供应商ID，val:该供应商的结算单的应收款
        Map<Long, BigDecimal> sheetMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            int n = random.nextInt(10) + 3;
            TouristGood good = goods.get(i);
            BigDecimal totalMoney = new BigDecimal(0);
            for (int j = 0; j < n; j++) {
                int orderStateRandom = random.nextInt(2);
                int orderDateRandom = random.nextInt(2);
                TouristOrder order = new TouristOrder();
                order.setOrderState(orderStateRandom == 1 ? OrderStateEnum.Finish : OrderStateEnum.NotFinish);
                order.setCreateTime(orderDateRandom == 1 ? LocalDateTime.now().plusDays(-days - j - 3) : LocalDateTime.now());
                order.setTouristGood(good);
                BigDecimal money = randomPrice();
                order.setOrderMoney(money);
                if (orderStateRandom == 1 && orderDateRandom == 1) {
                    totalMoney = totalMoney.add(money);
                }
                touristOrderRepository.saveAndFlush(order);
            }
            if (totalMoney.intValue() != 0) {
                sheetMap.put(good.getTouristSupplier().getId(), totalMoney);
            }
        }

        settlementSheetService.settleOrder();

        List<SettlementSheet> sheets = settlementSheetRepository.findAll();
        for (SettlementSheet s : sheets) {
            Assert.isTrue(sheetMap.get(s.getTouristSupplier().getId()).compareTo(s.getReceivableAccount()) == 0);
        }

    }

    @Test
    public void settlementSheetListTest() throws Exception {

        TouristSupplier sltSupplier = createTouristSupplier("slt");

        TouristSupplier wySupplier = createTouristSupplier("wy");

        List<SettlementSheet> supplierNameSheets = new ArrayList<>();
        List<SettlementSheet> touristSupplierSheets = new ArrayList<>();
        List<SettlementSheet> platformCheckingSheets = new ArrayList<>();
        List<SettlementSheet> dateSheets = new ArrayList<>();
        List<SettlementSheet> pageSheets = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            SettlementSheet sheet = new SettlementSheet();
            int supplierNameRan = random.nextInt(2);
            int platformCheckingRan = random.nextInt(2);
            int dateRan = random.nextInt(2);

            if (supplierNameRan == 1) {
                sheet.setTouristSupplier(sltSupplier);
            } else {
                sheet.setTouristSupplier(wySupplier);
            }

            if (platformCheckingRan == 1) {
                sheet.setPlatformChecking(SettlementStateEnum.CheckFinish);
            } else {
                sheet.setPlatformChecking(SettlementStateEnum.NotChecking);
            }

            if (dateRan == 1) {
                sheet.setCreateTime(LocalDateTime.of(2016, 9, 1, 0, 0, 0));
            }


            SettlementSheet sheetAct = createSettlementSheet(sheet.getSettlementNo(), sheet.getTouristSupplier()
                    , sheet.getReceivableAccount(), sheet.getSelfChecking(), sheet.getPlatformChecking()
                    , sheet.getCreateTime());


            if (supplierNameRan == 1) {
                supplierNameSheets.add(sheetAct);
            } else {
                touristSupplierSheets.add(sheetAct);
            }

            if (platformCheckingRan == 1) {
                platformCheckingSheets.add(sheetAct);
            }

            if (dateRan == 1) {
                dateSheets.add(sheetAct);
            }


            if (i > 9) {
                pageSheets.add(sheetAct);
            }

        }

        List<SettlementSheet> sheetsAct = settlementSheetService.settlementSheetList(wySupplier, null, null, null, null, null)
                .getContent();

        Assert.isTrue(sheetsAct.equals(touristSupplierSheets));
        sheetsAct = settlementSheetService.settlementSheetList(null, "slt", null, null, null, null)
                .getContent();
        Assert.isTrue(sheetsAct.equals(supplierNameSheets));

        sheetsAct = settlementSheetService.settlementSheetList(null, null, SettlementStateEnum.CheckFinish, null, null, null)
                .getContent();

        Assert.isTrue(sheetsAct.equals(platformCheckingSheets));

        sheetsAct = settlementSheetService.settlementSheetList(null, null, null, LocalDateTime.of(2016, 8, 1, 0, 0, 0),
                LocalDateTime.of(2016, 10, 1, 0, 0, 0), null)
                .getContent();

        Assert.isTrue(sheetsAct.equals(dateSheets));

        sheetsAct = settlementSheetService.settlementSheetList(null, null, null, null, null, new PageRequest(1, 10))
                .getContent();

        Assert.isTrue(sheetsAct.equals(pageSheets));


    }


    @Test
    public void countBalanceTest() throws Exception{
        TouristSupplier supplier=new TouristSupplier();
        supplier.setSupplierName("slt");
        supplier=touristSupplierRepository.saveAndFlush(supplier);
        TouristGood touristGood=new TouristGood();
        touristGood.setTouristSupplier(supplier);
        touristGood=touristGoodRepository.saveAndFlush(touristGood);

        settlementSheetService.countBalance(supplier,LocalDateTime.now());
    }

    @Test
    public void countSettledTest() throws Exception{
        TouristSupplier supplier=new TouristSupplier();
        supplier.setSupplierName("slt");
        supplier=touristSupplierRepository.saveAndFlush(supplier);
        BigDecimal settled= settlementSheetService.countSettled(supplier);
        BigDecimal notSettled=settlementSheetService.countNotSettled(supplier);
        BigDecimal withdrawal=settlementSheetService.countWithdrawal(supplier, null);
    }
}
