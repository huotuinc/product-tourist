package com.huotu.tourist.entity;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;

/**
 * 线路采购商
 * Created by lhx on 2016/12/16.
 */

public class TouristBuyer extends BaseModel{
    /**
     * 采购商名称
     */
    private String buyerName;

    /**
     * 采购商负责人
     */
    private String buyerDirector;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 营业执照
     */
    private String businessLicencesUri;

    /**
     * 身份证正面图
     */
    private String IDelevationsUri;

    /**
     * 身份证反面图
     */
    private String IDinverseUri;

    /**
     * 采购商ID
     */
    private String buyerId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 审核状态
     */
    private BuyerCheckStateEnum checkState;

    /**
     * 支付状态
     */
    private BuyerPayStateEnum payState;



}
