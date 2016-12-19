package com.huotu.tourist.model;

import com.huotu.tourist.entity.Traveler;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台订单详情model
 * Created by slt on 2016/12/19.
 */
@Getter
@Setter
@EqualsAndHashCode
public class TouristOrderDetailsModel {
    /**
     * 线路订单ID
     */
    private long id;

    /**
     * 订单ID
     */
    private String orderNo;

    /**
     * 下单日期
     */
    private String createTime;

    /**
     * 线路(商品)名称
     */
    private String touristName;

    /**
     * 购买人姓名
     */
    private String buyerName;

    /**
     * 购买人手机号
     */
    private String buyerTel;

    /**
     * 购买人身份证号
     */
    private String buyerIDNo;

    /**
     * 购买人所属供应商名字
     */
    private String supplierName;

    /**
     * 订单总金额
     */
    private double orderMoney;

    /**
     * 单价
     */
    private double unitPrice;

    /**
     * 返佣
     */
    private double returnCommission;

    /**
     * 余额抵用
     */
    private double balanceDeduction;

    /**
     * 小金库抵用
     */
    private double coffersDeduction;

    /**
     * 积分抵用
     */
    private double integralDeduction;

    /**
     * 支付状态
     */
    private String orderState;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 出行时间
     */
    private String touristDate;

    /**
     * 游客人数
     */
    private int peopleNumber;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 游客列表
     */
    private List<Traveler> travelers;



}
