package com.huotu.tourist.entity;

import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算单
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Settlement_Sheet")
@Getter
@Setter
public class SettlementSheet extends BaseModel {

    public static final List<Selection<SettlementSheet, ?>> selections = Arrays.asList(
            new SimpleSelection<SettlementSheet, String>("id", "id")
            , new SimpleSelection<SettlementSheet, String>("settlementNo", "settlementNo")
            , new SimpleSelection<SettlementSheet, String>("receivableAccount", "receivableAccount")
            , new SimpleSelection<SettlementSheet, String>("selfChecking.value", "selfChecking")
            , new Selection<SettlementSheet, String>() {
                @Override
                public String apply(SettlementSheet settlementSheet) {

                    return settlementSheet.getTouristSupplier() != null ? settlementSheet.getTouristSupplier()
                            .getSupplierName() : null;
                }

                @Override
                public String getName() {
                    return "supplierName";
                }
            }
            , new SimpleSelection<SettlementSheet, String>("selfChecking.code", "selfCheckingCode")
            , new Selection<SettlementSheet, Map>() {
                @Override
                public Map apply(SettlementSheet settlementSheet) {
                    Map map = new HashMap();
                    map.put("code", settlementSheet.getPlatformChecking().getCode());
                    map.put("value", settlementSheet.getPlatformChecking().getValue());
                    return map;
                }

                @Override
                public String getName() {
                    return "platformChecking";
                }
            }
            , new Selection<SettlementSheet, String>() {
                @Override
                public String apply(SettlementSheet settlementSheet) {
                    return LocalDateTimeFormatter.toStr(settlementSheet.getCreateTime());
                }

                @Override
                public String getName() {
                    return "createTime";
                }
            }
    );

    /**
     * 结算单号
     */
    @Column(length = 50)
    private String settlementNo;

    /**
     * 所属供应商
     */
    @ManyToOne
    private TouristSupplier touristSupplier;

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
