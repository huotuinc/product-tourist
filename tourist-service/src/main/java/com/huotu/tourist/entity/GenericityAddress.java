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
 * 线路供应商
 * Created by lhx on 2016/12/16.
 */
@Getter
@Setter
@Embeddable
public class GenericityAddress {
    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String town;

    /**
     * 区
     */
    private String district;

    /**
     * 联系人
     */
    @Column(length = 20)
    private String contacts;
    /**
     * 联系电话
     */
    @Column(length = 15)
    private String contactNumber;
}
