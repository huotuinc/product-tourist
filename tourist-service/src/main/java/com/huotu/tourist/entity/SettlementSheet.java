package com.huotu.tourist.entity;

import com.huotu.tourist.common.SettlementStateEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 结算单
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Settlement_Sheet")
@Getter
@Setter
public class SettlementSheet extends BaseModel{
    /**
     * 线路订单
     */
    @ManyToOne
    @JoinColumn(name = "touristOrderID")
    private TouristOrder touristOrder;

    /**
     * 应收款
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal receivableAccount;

    /**
     * 自我检查，自审
     */
    @Column
    private SettlementStateEnum selfChecking;

    /**
     * 平台审查状态
     */
    @Column
    private SettlementStateEnum platformChecking;
}
