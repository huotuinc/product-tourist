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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 游客
 * 应当是属于订单的
 * 已经声明了行程的
 * 姓名，身份证号码，性别，手机号，备注
 *
 * @author CJ
 */
@Entity
@Setter
@Getter
public class Traveler {

    /**
     * 行程会被调整,但是行程的线路是不变的
     */
    @ManyToOne
    private TouristRoute route;
    @ManyToOne
    private TouristOrder order;
}
