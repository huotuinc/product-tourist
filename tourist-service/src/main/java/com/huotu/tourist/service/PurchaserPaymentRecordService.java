package com.huotu.tourist.service;

import com.huotu.tourist.entity.PurchaserPaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 采购商支付记录
 * Created by lhx on 2016/12/19.
 */
public interface PurchaserPaymentRecordService extends BaseService<PurchaserPaymentRecord, Long> {

    /**
     * 采购商支付记录列表，支付时间区间匹配，其他条件模糊匹配
     *
     * @param startPayTime  开始的支付时间
     * @param endPayTime    截止的支付时间
     * @param buyerName     采购商名称
     * @param buyerDirector 采购商的负责人
     * @param telPhone      电话
     * @param pageable
     * @return 返回支付记录列表
     */
    Page<PurchaserPaymentRecord> purchaserPaymentRecord(String startPayTime, String endPayTime, String buyerName
            , String buyerDirector, String telPhone, Pageable pageable);

}
