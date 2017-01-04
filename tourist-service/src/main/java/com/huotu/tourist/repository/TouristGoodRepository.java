package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristGood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 线路商品持久层
 * Created by slt on 2016/12/19.
 */
public interface TouristGoodRepository extends JpaRepository<TouristGood, Long>, JpaSpecificationExecutor<TouristGood> {

}
