package com.huotu.tourist.entity;

import com.huotu.tourist.common.SettlementStateEnum;

import java.math.BigDecimal;

/**
 * 结算单
 * Created by lhx on 2016/12/16.
 */

public class SettlementSheet extends BaseModel{
    /**
     * 线路订单
     */
    private TouristOrder touristOrder;

    /**
     * 应收款
     */
    private BigDecimal receivableAccount;

    /**
     * 自我检查，自审
     */
    private SettlementStateEnum selfChecking;

    /**
     * 平台审查状态
     */
    private SettlementStateEnum platformChecking;
}
