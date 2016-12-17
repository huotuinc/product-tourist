package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 采购商产品设置
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Purchaser_Product_Setting")
@Getter
@Setter
public class PurchaserProductSetting {

    /**
     * 名称
     */
    @Column(length = 100)
    private String name;

    /**
     * banner图
     */
    @Column(length = 200)
    private String bannerUri;

    /**
     * 价格
     */
    @Column
    private BigDecimal price;

    /**
     * 说明文字
     */
    @Column(length = 200)
    private String explain;

    /**
     * 协议
     */
    @Lob
    @Column
    private String agreement;
}
