/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.repository;

import com.huotu.tourist.entity.ActivityType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lhx on 2016/12/20.
 */
@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long>
        , JpaSpecificationExecutor<ActivityType> {

    List<ActivityType> findByActivityName(String activityName);

    /**
     * 查找没有删除的活动类型
     *
     * @param pageRequest
     * @return
     */
    List<ActivityType> findByDeletedIsFalse(PageRequest pageRequest);
}
