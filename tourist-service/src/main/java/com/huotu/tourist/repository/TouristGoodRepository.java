package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 线路商品持久层
 * Created by slt on 2016/12/19.
 */
public interface TouristGoodRepository extends JpaRepository<TouristGood,Long> {

}
