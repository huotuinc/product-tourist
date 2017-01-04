package com.huotu.tourist.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 商品销售排行model
 * Created by slt on 2016/12/19.
 */
@Getter
@Setter
@EqualsAndHashCode
public class TouristGoodsSalesRankModel {

    /**
     * 商品ID
     */
    private long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 佣金比例
     */
    private BigDecimal rebate;

    /**
     * 购买次数
     */
    private Long buyNumber;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 总佣金
     */
    private BigDecimal totalCommission;

}
