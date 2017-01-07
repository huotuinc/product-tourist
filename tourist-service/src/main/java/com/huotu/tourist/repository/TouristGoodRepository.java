package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristGood;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 线路商品持久层
 * Created by slt on 2016/12/19.
 */
@Repository
public interface TouristGoodRepository extends JpaRepository<TouristGood, Long>, JpaSpecificationExecutor<TouristGood> {


    /**
     * 查找推荐且没有被删除的商品列表
     *
     * @param pageRequest
     * @return
     */
    List<TouristGood> findByRecommendIsTrueAndDeleteIsFalse(PageRequest pageRequest);

    /**
     * 查找指定活动id且没有删除的商品列表
     *
     * @param activityTypeId
     * @param pageRequest
     * @return
     */
    List<TouristGood> findByActivityType_IdAndDeleteIsFalse(Long activityTypeId, PageRequest pageRequest);
}
