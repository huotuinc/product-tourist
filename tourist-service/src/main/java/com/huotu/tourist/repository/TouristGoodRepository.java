package com.huotu.tourist.repository;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.TouristGood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * @param touristCheckState
     * @return
     */
    Page<TouristGood> findByRecommendTrueAndDeletedFalseAndTouristCheckState(Pageable pageRequest, TouristCheckStateEnum
            touristCheckState);

    /**
     * 查找指定活动id且没有删除的商品列表
     *
     * @param activityTypeId
     * @param pageRequest
     * @param touristCheckState
     * @return
     */
    Page<TouristGood> findByActivityType_IdAndDeletedFalseAndTouristCheckState(Long activityTypeId, Pageable pageRequest,
                                                                               TouristCheckStateEnum touristCheckState);

    /**
     * 统计线路供应商
     *
     * @param touristSupplierId
     * @param touristCheckState
     * @return
     */
    int countByTouristSupplier_IdAndDeletedFalseAndTouristCheckState(Long touristSupplierId, TouristCheckStateEnum touristCheckState);

    /**
     * 供应商线路列表
     *
     * @param supplierId
     * @param pageRequest
     * @param checkFinish
     * @return
     */
    List<TouristGood> findByTouristSupplier_IdAndDeletedFalseAndTouristCheckState(Long supplierId, PageRequest pageRequest, TouristCheckStateEnum checkFinish);

    /**
     * 根据目的地省份信息查询商品列表
     *
     * @param value
     * @param pageRequest
     * @param checkFinish
     * @return
     */
    List<TouristGood> findByDestination_ProvinceAndDeletedFalseAndTouristCheckState(String value, PageRequest pageRequest, TouristCheckStateEnum checkFinish);

    /**
     * 根据目的地市信息查询商品列表
     *
     * @param value
     * @param pageRequest
     * @param checkFinish
     * @return
     */
    List<TouristGood> findByDestination_TownAndDeletedFalseAndTouristCheckState(String value, PageRequest pageRequest, TouristCheckStateEnum checkFinish);

}
