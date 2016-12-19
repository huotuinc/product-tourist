package com.huotu.tourist.service;

import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 提现服务
 * Created by lhx on 2016/12/17.
 */

public interface PresentRecordService extends BaseService<SettlementSheet, Long> {


    /**
     * 提现列表
     *
     * @param supplierName 供应商名称 可以为null
     * @param presentState 提现状态
     * @param pageable
     * @return 提现列表
     */
    Page<SettlementSheet> presentRecordList(String supplierName, PresentStateEnum presentState, Pageable pageable);


}
