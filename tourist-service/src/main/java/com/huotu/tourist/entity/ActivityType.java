/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 活动类型
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "activity_type")
@Getter
@Setter
public class ActivityType extends BaseModel {

    /**
     * 活动名称
     */
    @Column(length = 200)
    private String activityName;

}
