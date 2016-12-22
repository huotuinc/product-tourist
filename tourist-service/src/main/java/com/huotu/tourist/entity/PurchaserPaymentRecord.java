/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购商支付记录（流水）
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Purchaser_Payment_Record")
@Getter
@Setter
public class PurchaserPaymentRecord extends BaseModel {
    public static final List<Selection<PurchaserPaymentRecord, ?>> selections = Arrays.asList(
            new SimpleSelection<PurchaserPaymentRecord, String>("id", "id")
            , new SimpleSelection<PurchaserPaymentRecord, String>("money", "money")
            , new SimpleSelection<PurchaserPaymentRecord, String>("payDate", "payDate")
            , new Selection<PurchaserPaymentRecord, String>() {
                @Override
                public String apply(PurchaserPaymentRecord purchaserPaymentRecord) {
                    return purchaserPaymentRecord.getTouristBuyer().getBuyerName();
                }

                @Override
                public String getName() {
                    return "buyerName";
                }
            }
            , new Selection<PurchaserPaymentRecord, String>() {
                @Override
                public String apply(PurchaserPaymentRecord purchaserPaymentRecord) {
                    return purchaserPaymentRecord.getTouristBuyer().getBuyerDirector();
                }

                @Override
                public String getName() {
                    return "buyerDirector";
                }
            }
            , new Selection<PurchaserPaymentRecord, String>() {
                @Override
                public String apply(PurchaserPaymentRecord purchaserPaymentRecord) {
                    return purchaserPaymentRecord.getTouristBuyer().getTelPhone();
                }

                @Override
                public String getName() {
                    return "telPhone";
                }
            }
            , new Selection<PurchaserPaymentRecord, String>() {
                @Override
                public String apply(PurchaserPaymentRecord purchaserPaymentRecord) {
                    return purchaserPaymentRecord.getTouristBuyer().getBuyerId();
                }

                @Override
                public String getName() {
                    return "buyerId";
                }
            }
            , new Selection<PurchaserPaymentRecord, String>() {
                @Override
                public String apply(PurchaserPaymentRecord purchaserPaymentRecord) {
                    return purchaserPaymentRecord.getTouristBuyer().getNickname();
                }

                @Override
                public String getName() {
                    return "nickname";
                }
            }
            , new Selection<PurchaserPaymentRecord, Map>() {
                @Override
                public String getName() {
                    return "payState";
                }

                @Override
                public Map apply(PurchaserPaymentRecord purchaserPaymentRecord) {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", purchaserPaymentRecord.getTouristBuyer().getPayState().getCode().toString());
                    map.put("value", purchaserPaymentRecord.getTouristBuyer().getPayState().getValue().toString());
                    return map;
                }
            }

    );
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
