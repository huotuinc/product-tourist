package com.huotu.tourist.service;

import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

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
    Page<SettlementSheet> settlementSheetList(String supplierName, SettlementStateEnum platformChecking, LocalDate createTime, Pageable pageable);


}
