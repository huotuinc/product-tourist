package com.huotu.tourist.model;

import com.huotu.tourist.entity.TouristGood;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *  线路行程模型
 * Created by slt on 2016/12/19.
 */
@Getter
@Setter
@EqualsAndHashCode
public class TouristRouteModel {

    private long id;

    private String routeNo;

    private TouristGood good;

    private String fromDate;

    private String toDate;

    private int maxPeople;

    /**
     * 剩余人数
     */
    private int remainPeople;

    /**
     * 是否已经出售
     */
    private boolean sold;
}
