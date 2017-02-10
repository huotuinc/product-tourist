package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.Address;
import com.huotu.tourist.service.TouristGoodService;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by lhx on 2017/2/10.
 */
public class TouristGoodServiceImplTest extends ServiceBaseTest {
    @Autowired
    TouristGoodService touristGoodService;

    @Test
    public void findByDestinationTown() throws Exception {
        createTouristGood(null, null, null, null, null, new Address("安徽省", "合肥市", "瑶海"));
        createTouristGood(null, null, null, null, null, new Address("安徽省", "合肥市", "庐江"));
        createTouristGood(null, null, null, null, null, new Address("安徽省", "芜湖", "sss"));
        createTouristGood(null, null, null, null, null, new Address("安徽省", "芜湖", "ddd"));
        List<Address> list = touristGoodService.findByDestinationTown();
    }

}