package com.huotu.tourist.service;

import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * 提现服务
 * Created by lhx on 2016/12/17.
 */

public interface PresentRecordService extends BaseService<PresentRecord, Long> {


    /**
     * 提现列表
     *  @param supplierName      供应商名称 可以为null
     * @param touristSupplier   供应商
     * @param presentState      提现状态
     * @param createTime        大于的创建时间
     * @param endCreateTime     小于的创建时间
     * @param pageable  @return 提现列表
     */
    Page<PresentRecord> presentRecordList(String supplierName, TouristSupplier touristSupplier
            , PresentStateEnum presentState, LocalDateTime createTime, LocalDateTime endCreateTime, Pageable pageable);


}
