package com.huotu.tourist.service;

import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;

/**
 * 后台分销商(管理员)服务
 * Created by slt on 2016/12/17.
 */
public interface DistributorsService {

    /**
     * 创建一个供应商
     * @param supplier 供应商 todo 参数暂定
     * @return  返回新增的供应商
     */
    TouristSupplier createSupplier(TouristSupplier supplier);

    /**
     * 修改一个供应商
     * @param supplier 供应商 todo 参数暂定
     * @return  返回修改的供应商
     */
    TouristSupplier modifySupplier(TouristSupplier supplier);

    /**
     * 冻结一个供应商
     * @param id    供应商ID
     */
    void freezeSupplier(Long id);

    /**
     * 获取供应商列表 todo 需要一个查询model对象传入
     * @return 带分页信息的供应商列表
     */
    Page<TouristSupplier> getSupplierList();

    /**
     * 获取线路采购商的列表 todo 需要一个查询model对象传入
     * @return 返回带分页信息的采购商列表
     */
    Page<TouristBuyer>getBuyerList();

    /**
     * 获取采购商支付流水列表 todo 需要一个查询model对象传入
     * @return  返回带分页信息的流水列表
     */
    Page<PurchaserPaymentRecord> getPurchaserPaymentRecord();

}
