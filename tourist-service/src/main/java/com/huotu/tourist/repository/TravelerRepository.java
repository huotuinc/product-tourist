package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 游客持久层
 * Created by slt on 2016/12/19.
 */
public interface TravelerRepository extends JpaRepository<Traveler,Long> {


    List<Traveler> findByRoute_Id(Long routeId);

    @Query("update Traveler as t set t.route=?1 where t.route=?2 ")
    @Modifying
    @Transactional
    int modifyRouteIdByRouteId(Long laterId,Long formerId);

    List<Traveler> findByOrder_Id(Long orderId);

    Long countByOrder_Id(Long orderId);

    long countByOrder_TouristGood(TouristGood touristGood);
}
