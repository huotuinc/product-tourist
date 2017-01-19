package com.huotu.tourist.service;

import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算服务
 * Created by lhx on 2016/12/17.
 */

public interface SettlementSheetService extends BaseService<SettlementSheet, Long> {


    /**
     * 每天凌晨3点，结算订单，生成结算单
     * @throws IOException
     */
    void settlementSheetTask() throws IOException;

    /**
     * 根据（T+1）规则创建结算单
     * @throws IOException
     */
    @Transactional
    void settleOrder() throws IOException;

    /**
     * 创建一个结算单
     * @param supplier              供应商
     * @param receivableAccount     应收款
     * @return                      创建完成的结算单
     * @throws IOException
     */
    SettlementSheet createSettlement(TouristSupplier supplier,BigDecimal receivableAccount) throws IOException;

    /**
     * 结算列表
     *
     *
     * @param touristSupplier   供应商
     * @param supplierName      供应商名称 可以为null
     * @param platformChecking  平台审查状态
     * @param createTime        大于的创建时间
     * @param endCreateTime     小于的创建时间
     *@param pageable  @return  结算列表
     */
    Page<SettlementSheet> settlementSheetList(TouristSupplier touristSupplier, String supplierName, SettlementStateEnum platformChecking, LocalDateTime createTime, LocalDateTime endCreateTime, Pageable pageable);


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
     * @param supplier      供应商
     * @param endCountDate    小于该结算日期的余额
     * @return
     * @throws IOException
     */
    @Transactional(readOnly = true)
    BigDecimal countWithdrawal(TouristSupplier supplier, LocalDateTime endCountDate) throws IOException;

    /**
     * 计算余额
     * 所有已确认结算单总额-所有未被拒绝的提现订单总额
     * @param supplier          供应商
     * @param endCountDate      小于该结算日期的余额
     * @return
     * @throws IOException
     */
    @Transactional(readOnly = true)
    BigDecimal countBalance(TouristSupplier supplier, LocalDateTime endCountDate) throws IOException;

}
