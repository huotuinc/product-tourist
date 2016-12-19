package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购商支付记录（流水）
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Purchaser_Payment_Record")
@Getter
@Setter
public class PurchaserPaymentRecord extends BaseModel {
    /**
     * 采购商
     */
    @ManyToOne
    @JoinColumn(name = "touristBuyerId")
    private TouristBuyer touristBuyer;

    @Column(precision = 10, scale = 2)
    private BigDecimal money;

    @Column
    private LocalDateTime payDate;


}
