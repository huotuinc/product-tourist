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
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * 线路行程
 * 行程可以被自由的添加
 *
 * @author CJ
 */
@Entity
@Setter
@Getter
public class TouristRoute {

    @Column(unique = true, length = 100)
    private String routeNo;

    @ManyToOne
    private TouristGood good;

    @Column(columnDefinition = "date")
    private LocalDate fromDate;

    @Column(columnDefinition = "date")
    private LocalDate toDate;

    private int maxPeople;


}
