package com.huotu.tourist.service;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.entity.TouristBuyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 线路采购商相关服务
 * Created by lhx on 2016/12/19.
 */

public interface TouristBuyerService extends BaseService<TouristBuyer, Long> {
    /**
     * 采购商列表
     *
     * @param buyerName     采购商名称 可以为null
     * @param buyerDirector 负责人 可以为null
     * @param telPhone      手机号 可以为null
     * @param checkState    审核状态 可以为null
     * @param pageable
     * @return 分页数据
     */
    Page<TouristBuyer> buyerList(String buyerName, String buyerDirector, String telPhone, BuyerCheckStateEnum checkState
            , Pageable pageable);



}
