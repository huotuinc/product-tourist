package com.huotu.tourist.service;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 线路商品测试
 * Created by slt on 2017/2/7.
 */
public class TouristGoodServiceTest extends ServiceBaseTest {

    @Test
    public void touristGoodListTest() throws Exception {
        TouristSupplier sltSupplier = createTouristSupplier("slt");
        TouristSupplier wySupplier = createTouristSupplier("wy");
        TouristType touristType = createTouristType("touristType");
        ActivityType activityType = createActivityType("activityType");
        Long lastId = 0L;

        List<TouristGood> supplierList = new ArrayList<>();
        List<TouristGood> touristNameList = new ArrayList<>();
        List<TouristGood> supplierNameList = new ArrayList<>();
        List<TouristGood> touristTypeList = new ArrayList<>();
        List<TouristGood> activityTypeList = new ArrayList<>();
        List<TouristGood> touristCheckStateList = new ArrayList<>();
        List<TouristGood> recommendList = new ArrayList<>();
        List<TouristGood> pageableList = new ArrayList<>();
        List<TouristGood> lastIdList = new ArrayList<>();


        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            boolean supplierRan = random.nextBoolean();
            boolean touristNameRan = random.nextBoolean();
            boolean supplierNameRan = random.nextBoolean();
            boolean touristTypeRan = random.nextBoolean();
            boolean activityTypeRan = random.nextBoolean();
            boolean touristCheckStateRan = random.nextBoolean();
            boolean recommendRan = random.nextBoolean();
            boolean pageableRan = i > 19;
            boolean lastIdRan = i < 15;


            TouristGood good = new TouristGood();

            if (supplierRan) {
                good.setTouristSupplier(sltSupplier);
            } else {
                if (supplierNameRan) {
                    good.setTouristSupplier(wySupplier);
                }
            }

            if (touristNameRan) {
                good.setTouristName("touristName");
            }
            if (touristTypeRan) {
                good.setTouristType(touristType);
            }
            if (activityTypeRan) {
                good.setActivityType(activityType);
            }
            if (touristCheckStateRan) {
                good.setTouristCheckState(TouristCheckStateEnum.CheckFinish);
            } else {
                good.setTouristCheckState(TouristCheckStateEnum.NotChecking);
            }

            if (recommendRan) {
                good.setRecommend(true);
            } else {
                good.setRecommend(false);
            }
            TouristGood goodAct = createTouristGood(good.getTouristName(), good.getActivityType(), good.getTouristType()
                    , good.getTouristCheckState(), good.getTouristSupplier(), null, null, null, null, null, null, null, null
                    , null, null, null, null, null, null, null, good.isRecommend());
            if (i == 15) {
                lastId = goodAct.getId();
            }

            if (supplierRan) {
                supplierList.add(goodAct);
            } else {
                if (supplierNameRan) {
                    supplierNameList.add(goodAct);
                }
            }

            if (touristNameRan) {
                touristNameList.add(goodAct);
            }
            if (touristTypeRan) {
                touristTypeList.add(goodAct);
            }
            if (activityTypeRan) {
                activityTypeList.add(goodAct);
            }
            if (touristCheckStateRan) {
                touristCheckStateList.add(goodAct);
            }

            if (recommendRan) {
                recommendList.add(goodAct);
            }

            if (pageableRan) {
                pageableList.add(goodAct);
            }

            if (lastIdRan) {
                lastIdList.add(goodAct);
            }
        }

        List<TouristGood> goodsAct = touristGoodService.touristGoodList(sltSupplier, null, null, null, null, null, null, null
                , null).getContent();

        Assert.isTrue(goodsAct.equals(supplierList));

        goodsAct = touristGoodService.touristGoodList(null, "touristName", null, null, null, null, null, null, null).getContent();
        Assert.isTrue(goodsAct.equals(touristNameList));

        goodsAct = touristGoodService.touristGoodList(null, null, "wy", null, null, null, null, null, null).getContent();
        Assert.isTrue(goodsAct.equals(supplierNameList));

        goodsAct = touristGoodService.touristGoodList(null, null, null, touristType, null, null, null, null, null).getContent();
        Assert.isTrue(goodsAct.equals(touristTypeList));

        goodsAct = touristGoodService.touristGoodList(null, null, null, null, activityType, null, null, null, null).getContent();
        Assert.isTrue(goodsAct.equals(activityTypeList));

        goodsAct = touristGoodService.touristGoodList(null, null, null, null, null, TouristCheckStateEnum.CheckFinish
                , null, null, null).getContent();
        Assert.isTrue(goodsAct.equals(touristCheckStateList));

        goodsAct = touristGoodService.touristGoodList(null, null, null, null, null, null, true, null, null).getContent();
        Assert.isTrue(goodsAct.equals(recommendList));


        goodsAct = touristGoodService.touristGoodList(null, null, null, null, null, null, null, new PageRequest(2, 10), null)
                .getContent();
        Assert.isTrue(goodsAct.equals(pageableList));

        goodsAct = touristGoodService.touristGoodList(null, null, null, null, null, null, null
                , null, lastId)
                .getContent();
        Assert.isTrue(goodsAct.equals(lastIdList));


    }
}
