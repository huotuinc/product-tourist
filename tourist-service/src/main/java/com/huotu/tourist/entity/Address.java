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
import javax.persistence.Embeddable;

/**
 * 省市区
 * Created by lhx on 2016/12/16.
 */
@Getter
@Setter
@Embeddable
public class Address {

    /**
     * 省
     */
    @Column(length = 10)
    private String province;

    /**
     * 市
     */
    @Column(length = 10)
    private String town;

    /**
     * 区
     */
    @Column(length = 10)
    private String district;

    @Override
    public String toString() {
        return province + "," + town + "," + district;
    }
}
