package com.huotu.tourist.entity;

import com.huotu.tourist.common.PresentStateEnum;

import java.math.BigDecimal;

/**
 * 提现记录（提现流水）
 * Created by lhx on 2016/12/16.
 */

public class PresentRecord extends BaseModel{

    /**
     * 供应商
     */
    private TouristSupplier touristSupplier;

    /**
     * 提现金额
     */
    private BigDecimal AmountOfMoney;

    /**
     * 账户余额
     */
    private BigDecimal AccountBalance;

    /**
     * 提现状态
     */
    private PresentStateEnum presentState;
}
