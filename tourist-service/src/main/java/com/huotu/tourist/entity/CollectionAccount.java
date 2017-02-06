package com.huotu.tourist.entity;

import com.huotu.tourist.common.CollectionAccountTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

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
    @Column
    private CollectionAccountTypeEnum accountType;

    /**
     * 身份证
     */
    @Column(length = 18)
    private String IDCard;

    /**
     * 支付宝姓名
     */
    @Column(length = 50)
    private String aliPayName;

    /**
     * 支付宝账户
     */
    @Column(length = 20)
    private String aliPayAccount;


    /**
     * 银行卡户名
     */
    @Column(length = 50)
    private String accountName;

    /**
     * 开户银行
     */
    @Column(length = 100)
    private String bank;

    /**
     * 开户支行
     */
    @Column(length = 100)
    private String bankBranch;

    /**
     * 银行卡号
     */
    @Column(length = 20)
    private String bankCard;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionAccount account = (CollectionAccount) o;
        return super.equals(o) &&
                Objects.equals(isDeleted(), account.isDeleted()) &&
                Objects.equals(accountType, account.accountType) &&
                Objects.equals(IDCard, account.IDCard) &&
                Objects.equals(aliPayName, account.aliPayName) &&
                Objects.equals(aliPayAccount, account.aliPayAccount) &&
                Objects.equals(accountName, account.accountName) &&
                Objects.equals(bank, account.bank) &&
                Objects.equals(bankBranch, account.bankBranch) &&
                Objects.equals(bankCard, account.bankCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType, IDCard, aliPayName, aliPayAccount, accountName, bank, bankBranch, bankCard);
    }


}
