package com.huotu.tourist.entity;

import com.huotu.tourist.common.PresentStateEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @JoinColumn(name = "touristSupplierId")
    private TouristSupplier touristSupplier;

    /**
     * 提现金额
     */
    @Column
    private BigDecimal amountOfMoney;

    /**
     * 账户余额
     */
    @Column
    private BigDecimal accountBalance;

    /**
     * 提现状态
     */
    @Column
    private PresentStateEnum presentState;
}
