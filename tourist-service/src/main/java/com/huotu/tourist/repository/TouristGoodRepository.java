package com.huotu.tourist.repository;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.TouristGood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 线路商品持久层
 * Created by slt on 2016/12/19.
 */
public interface TouristGoodRepository extends JpaRepository<TouristGood,Long> {

    @Query("update TouristGood as t set t.TouristCheckStateEnum=?1 where t.id=?2")
    @Modifying
    @Transactional
    int modifyTouristGoodState(TouristCheckStateEnum stateEnum,Long id);

}
