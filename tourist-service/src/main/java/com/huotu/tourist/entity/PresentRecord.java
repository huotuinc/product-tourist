package com.huotu.tourist.entity;

import com.huotu.tourist.common.PresentStateEnum;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * 提现记录（提现流水）
 * Created by lhx on 2016/12/16.
 */

public class PresentRecord extends BaseModel{

    /**
     * 供应商
     */
    @ManyToOne
    @JoinColumn(name = "touristSupplierId")
    private TouristSupplier touristSupplier;

    /**
     * 提现金额
     */
    @Column(name = "amountOfMoney")
    private BigDecimal amountOfMoney;

    /**
     * 账户余额
     */
    @Column(name = "accountBalance")
    private BigDecimal accountBalance;

    /**
     * 提现状态
     */
    @Column(name = "presentState")
    private PresentStateEnum presentState;
}
