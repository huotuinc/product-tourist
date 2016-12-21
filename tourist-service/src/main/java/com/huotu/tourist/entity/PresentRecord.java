package com.huotu.tourist.entity;

import com.huotu.tourist.common.PresentStateEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 提现记录（提现流水）
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "present_record")
@Getter
@Setter
public class PresentRecord extends BaseModel{

    /**
     * 供应商
     */
    @ManyToOne
    @JoinColumn
    private SettlementSheet settlementSheet;

    /**
     * 提现金额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal amountOfMoney;

    /**
     * 账户余额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal accountBalance;

    /**
     * 提现状态
     */
    @Column
    private PresentStateEnum presentState;
}
