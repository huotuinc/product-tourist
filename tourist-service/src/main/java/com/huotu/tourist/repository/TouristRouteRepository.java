package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  线路行程持久层
 * Created by slt on 2016/12/19.
 */
public interface TouristRouteRepository extends JpaRepository<TouristRoute,Long> {

    List<TouristRoute> findByGood(TouristGood good);

}
