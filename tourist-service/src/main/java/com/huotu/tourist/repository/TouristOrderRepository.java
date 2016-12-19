package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 线路订单持久层
 * Created by slt on 2016/12/19.
 */
public interface TouristOrderRepository extends JpaRepository<TouristOrder,Long> {
}
