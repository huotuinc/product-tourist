package com.huotu.tourist.service;

import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.entity.TouristSupplier;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 提现测试
 * Created by slt on 2017/2/6.
 */
public class PresentRecordTest extends ServiceBaseTest {

    @Autowired
    PresentRecordService presentRecordService;

    @Test
    public void presentRecordListTest() {
        //供应商名称查询
        TouristSupplier sltSupplier = createTouristSupplier("slt");

        TouristSupplier wySupplier = createTouristSupplier("wy");

        List<PresentRecord> sltRecords = new ArrayList<>();
        List<PresentRecord> wyRecords = new ArrayList<>();
        List<PresentRecord> presentStateRecords = new ArrayList<>();
        List<PresentRecord> dateRecords = new ArrayList<>();
        List<PresentRecord> pageRecords = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            PresentRecord record = new PresentRecord();
            int supplier = random.nextInt(3);
            int state = random.nextInt(2);
            int date = random.nextInt(2);
            if (supplier == 1) {
                record.setTouristSupplier(sltSupplier);
            } else {
                record.setTouristSupplier(wySupplier);
            }
            if (state == 1) {
                record.setPresentState(PresentStateEnum.AlreadyPaid);
            } else {
                record.setPresentState(PresentStateEnum.CheckFinish);
            }
            if (date == 1) {
                record.setCreateTime(LocalDateTime.of(2016, 3, 1, 0, 0, 0, 0));
            }
            PresentRecord recordAct = createPresentRecord(record.getRecordNo(), record.getTouristSupplier()
                    , record.getAmountOfMoney(), record.getPresentState(), record.getCreateTime());

            if (supplier == 1) {
                sltRecords.add(recordAct);
            } else {
                wyRecords.add(recordAct);
            }
            if (state == 1) {
                presentStateRecords.add(recordAct);
            }
            if (date == 1) {
                dateRecords.add(recordAct);
            }
            if (i > 4) {
                pageRecords.add(recordAct);
            }
        }
        List<PresentRecord> recordsAct = presentRecordService.presentRecordList("sl", null, null, null, null, null)
                .getContent();
        //根据供应商名字查询测试
        Assert.isTrue(recordsAct.equals(sltRecords));
        recordsAct = presentRecordService.presentRecordList(null, wySupplier, null, null, null, null).getContent();
        //根据供应商查询测试
        Assert.isTrue(recordsAct.equals(wyRecords));
        recordsAct = presentRecordService.presentRecordList(null, null, PresentStateEnum.AlreadyPaid, null, null, null)
                .getContent();
        //根据状态查询测试
        Assert.isTrue(recordsAct.equals(presentStateRecords));

        recordsAct = presentRecordService.presentRecordList(null, null, null, LocalDateTime.of(2016, 3, 1, 0, 0, 0)
                , LocalDateTime.of(2016, 3, 3, 0, 0, 0), null).getContent();
        //根据日期查询测试
        Assert.isTrue(recordsAct.equals(dateRecords));

        recordsAct = presentRecordService.presentRecordList(null, null, null, null, null, new PageRequest(1, 5))
                .getContent();
        //分页测试
        Assert.isTrue(recordsAct.equals(pageRecords));
    }

}
