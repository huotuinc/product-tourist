package com.huotu.tourist.service;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
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
            int touristSupplierRan = random.nextInt(2);
            int dateRan = random.nextInt(2);


            SettlementSheet sheetAct = createSettlementSheet(sheet.getSettlementNo(), sheet.getTouristSupplier()
                    , sheet.getReceivableAccount(), sheet.getSelfChecking(), sheet.getPlatformChecking()
                    , sheet.getCreateTime());


            if (i > 9) {
                pageSheets.add(sheetAct);
            }

        }


        settlementSheetService.settlementSheetList(null, null, null, null, null, null);

    }
}
