package com.huotu.tourist.entity;

import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现记录（提现流水）
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "present_record")
@Getter
@Setter
public class PresentRecord extends BaseModel{

    public static final List<Selection<PresentRecord, ?>> selections = Arrays.asList(
            new SimpleSelection<PresentRecord, String>("id", "id"),
            new SimpleSelection<PresentRecord, String>("amountOfMoney", "amountOfMoney")
            , new SimpleSelection<PresentRecord, String>("recordNo", "recordNo")
//            , new SimpleSelection<PresentRecord, String>("accountBalance", "accountBalance")
            , new SimpleSelection<PresentRecord, String>("createTime", "createTime")
            , new SimpleSelection<PresentRecord, String>("touristSupplier.supplierName", "supplierName")
            , new Selection<PresentRecord, Map>() {
                @Override
                public Map apply(PresentRecord presentRecord) {
                    Map map = new HashMap();
                    map.put("code", presentRecord.getPresentState().getCode().toString());
                    map.put("value", presentRecord.getPresentState().getValue().toString());
                    return map;
                }

                @Override
                public String getName() {
                    return "presentState";
                }
            }
            , new Selection<PresentRecord, String>() {
                @Override
                public String apply(PresentRecord presentRecord) {
                    return LocalDateTimeFormatter.toStr(presentRecord.getCreateTime());
                }

                @Override
                public String getName() {
                    return "createTime";
                }
            }

    );

    /**
     * 提现编号
     */
    private String recordNo;

    /**
     * 所属供应商
     */
    @ManyToOne
    private TouristSupplier touristSupplier;


    /**
     * 提现金额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal amountOfMoney;

//    /**
//     * 账户余额
//     */
//    @Column(precision = 10, scale = 2)
//    private BigDecimal accountBalance;

    /**
     * 提现状态
     */
    @Column
    private PresentStateEnum presentState;
}
