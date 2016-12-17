package com.huotu.tourist.entity;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 线路采购商
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Tourist_Buyer")
@Getter
@Setter
public class TouristBuyer extends BaseModel{

    /**
     * 采购商名称
     */
    @Column(name = "buyerName", length = 50)
    private String buyerName;

    /**
     * 采购商负责人
     */
    @Column(length = 50)
    private String buyerDirector;

    /**
     * 手机号
     */
    @Column(length = 15)
    private String telPhone;

    /**
     * 营业执照
     */
    @Column(length = 200)
    private String businessLicencesUri;

    /**
     * 身份证正面图
     */
    @Column(length = 200)
    private String IDElevationsUri;

    /**
     * 身份证反面图
     */
    @Column(length = 200)
    private String IDInverseUri;

    /**
     * 采购商ID
     */
    @Column(length = 100, unique = true)
    private String buyerId;

    /**
     * 昵称
     */
    @Column(length = 100)
    private String nickname;

    /**
     * 审核状态
     */
    @Column
    private BuyerCheckStateEnum checkState;

    /**
     * 支付状态
     */
    @Column
    private BuyerPayStateEnum payState;



}
