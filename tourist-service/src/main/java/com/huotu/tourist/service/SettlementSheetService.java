package com.huotu.tourist.service;

import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算服务
 * Created by lhx on 2016/12/17.
 */

public interface SettlementSheetService extends BaseService<SettlementSheet, Long> {


    /**
     * 结算列表
     *
     * @param supplierName     供应商名称 可以为null
     * @param platformChecking 平台审查状态
     * @param createTime   创建时间
     * @param pageable
     * @return 结算列表
     */
    Page<SettlementSheet> settlementSheetList(String supplierName, SettlementStateEnum platformChecking, LocalDateTime createTime, Pageable pageable);


    /**
     * 计算已结算的钱
     * @param supplier
     * @return
     * @throws IOException
     */
    BigDecimal countSettled(TouristSupplier supplier) throws IOException;

    /**
     * 计算未结算的钱
     * @param supplier
     * @return
     * @throws IOException
     */
    BigDecimal countNotSettled(TouristSupplier supplier) throws IOException;

    /**
     * 计算已提现的钱
     * @param supplier
     * @return
     * @throws IOException
     */
    BigDecimal countWithdrawal(TouristSupplier supplier) throws IOException;

    /**
     * 计算余额
     * @param supplier          供应商
     * @param endCountDate      小于该结算日期的余额
     * @return
     * @throws IOException
     */
    BigDecimal countBalance(TouristSupplier supplier, LocalDateTime endCountDate) throws IOException;

}
