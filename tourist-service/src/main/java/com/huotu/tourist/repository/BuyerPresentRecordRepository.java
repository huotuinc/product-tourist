package com.huotu.tourist.repository;

import com.huotu.tourist.entity.BuyerPresentRecord;
import com.huotu.tourist.entity.TouristBuyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 采购商提现记录（提现流水）
 * Created by lhx on 2016/12/16.
 */
@Repository
public interface BuyerPresentRecordRepository extends JpaRepository<BuyerPresentRecord,Long>{

    @Query("select sum(b.amountOfMoney) from BuyerPresentRecord as b where b.touristBuyer=?1")
    BigDecimal countBuyerPresentRecord(TouristBuyer buyer);


}
