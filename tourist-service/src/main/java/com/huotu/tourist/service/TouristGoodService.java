package com.huotu.tourist.service;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * 线路相关服务
 * Created by lhx on 2016/12/17.
 */

public interface TouristGoodService extends BaseService<TouristGood, Long> {

    /**
     * 线路列表
     *
     * @param supplier        供应商ID
     * @param touristName       线路名称 可以为null
     * @param supplierName      供应商名称
     * @param touristType       路线类型
     * @param activityType      活动类型
     * @param touristCheckState 线路审核状态
     * @param pageable
     * @return
     */
    Page<TouristGood> touristGoodList(TouristSupplier supplier, String touristName, String supplierName, TouristType touristType
            , ActivityType activityType, TouristCheckStateEnum touristCheckState, Pageable pageable);

    /**
     * 获取推荐商品列表
     *
     * @param pageable
     * @return
     */
    Page<TouristGood> recommendTouristGoodList(String touristName, String supplierName, TouristType touristType
            , ActivityType activityType, TouristCheckStateEnum touristCheckState, Boolean recommend, Pageable pageable);


    /**
     * 商品销售排行，默认降序
     * @param supplierId    供应商ID
     * @return              商品列表
     */
    Page<TouristGood> salesRanking(Long supplierId,Pageable pageable);


    /**
     * 保存一个线路商品(包含新增和修改)
     * @param id                商品ID，有：是修改，无：新增
     * @param touristName           线路名称(必须)
     * @param activityType        活动类型(必须)
     * @param touristType         线路类型(必须)
     * @param touristFeatures       线路特色(必须)
     * @param destination           目的地(必须)
     * @param placeOfDeparture      出发地(必须)
     * @param travelledAddress      途经地(必须)
     * @param price                 单价(必须)
     * @param childrenDiscount      儿童折扣
     * @param rebate                返利比例(必须)
     * @param receptionPerson       地接人(必须)
     * @param receptionTelephone    地接人电话(必须)
     * @param eventDetails          活动详情(必须)
     * @param beCareful             注意事项(必须)
     * @param touristImgUri         商品图片(必须)
     * @param maxPeople             最大人数(必须)
     * @return                      新增或修改的线路商品
     */
    TouristGood saveTouristGood(Long id, String touristName, ActivityType activityType, TouristType touristType
            , String touristFeatures, Address destination, Address placeOfDeparture, Address travelledAddress
            , BigDecimal price, BigDecimal childrenDiscount, BigDecimal rebate, String receptionPerson
            , String receptionTelephone, String eventDetails, String beCareful, String touristImgUri
            , int maxPeople);

    /**
     * 将本地商品与远程商品同步
     * 并设置当前商品的商城商品id
     *
     * @param touristGood 本地商品
     */
    void pushGoodToMall(TouristGood touristGood);
}
