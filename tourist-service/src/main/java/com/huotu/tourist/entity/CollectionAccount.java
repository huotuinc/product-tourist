package com.huotu.tourist.entity;

import com.huotu.tourist.common.CollectionAccountTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 收款账户
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "activity_type")
@Getter
@Setter
public class CollectionAccount extends BaseModel {

    /**
     * 账户类型
     */
    @Column(name = "accountType")
    private CollectionAccountTypeEnum accountType;

    /**
     * 身份证
     */
    @Column(name = "IDCard", length = 18)
    private String IDCard;

    /**
     * 支付宝姓名
     */
    @Column(name = "alipayName", length = 50)
    private String alipayName;

    /**
     * 支付宝账户
     */
    @Column(name = "alipayAccount", length = 20)
    private String alipayAccount;


    /**
     * 银行卡户名
     */
    @Column(name = "accountName", length = 50)
    private String accountName;

    /**
     * 开户银行
     */
    @Column(name = "bank", length = 100)
    private String bank;

    /**
     * 开户支行
     */
    @Column(name = "bankBranch", length = 100)
    private String bankBranch;

    /**
     * 银行卡号
     */
    @Column(name = "bankBranch", length = 20)
    private String bankCard;

}
