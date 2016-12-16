package com.huotu.tourist.entity;

import com.huotu.tourist.common.CollectionAccountTypeEnum;

/**
 * 收款账户
 * Created by lhx on 2016/12/16.
 */

public class CollectionAccount extends BaseModel {

    /**
     * 账户类型
     */
    private CollectionAccountTypeEnum accountType;

    /**
     * 身份证
     */
    private String IDCard;

    /**
     * 支付宝姓名
     */
    private String alipayName;

    /**
     * 支付宝账户
     */
    private String alipayAccount;


    /**
     * 银行卡户名
     */
    private String accountName;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 开户支行
     */
    private String bankBranch;

    /**
     * 银行卡号
     */
    private String bankCard;

}
