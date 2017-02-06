package com.huotu.tourist.entity;

import com.huotu.tourist.common.PresentStateEnum;
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
import java.util.*;

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
            , new SimpleSelection<PresentRecord, String>("createTime", "createTime")
            , new SimpleSelection<PresentRecord, String>("touristSupplier.supplierName", "supplierName")
            ,
            new Selection<PresentRecord, Map>() {
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

    /**
     * 提现状态
     */
    @Column
    private PresentStateEnum presentState;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PresentRecord record = (PresentRecord) o;
        return super.equals(o) &&
                Objects.equals(recordNo, record.recordNo) &&
                Objects.equals(touristSupplier, record.touristSupplier) &&
                Objects.equals(amountOfMoney, record.amountOfMoney) &&
                Objects.equals(presentState, record.presentState);
    }

    @Override
    public int hashCode() {
//        if (getId() != null)
//            return Objects.hash(getId());
        return Objects.hash(recordNo, touristSupplier, amountOfMoney, presentState);
    }
}
