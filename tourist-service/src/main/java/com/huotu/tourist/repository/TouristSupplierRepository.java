/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * Created by slt on 2016/12/19.
 */
public interface TouristSupplierRepository extends JpaRepository<TouristSupplier, Long>, JpaSpecificationExecutor<TouristSupplier> {

    TouristSupplier findByLoginName(String name);

    Page<TouristSupplier> findByAuthSupplierAndDeleted(TouristSupplier supplier,boolean deleted,Pageable pageable);
}
