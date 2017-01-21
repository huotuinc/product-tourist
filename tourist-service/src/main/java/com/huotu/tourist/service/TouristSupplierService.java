package com.huotu.tourist.service;

import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应商相关服务
 * Created by lhx on 2016/12/17.
 */

public interface TouristSupplierService extends BaseService<TouristSupplier, Long> {

    /**
     * 供应商列表
     *
     * @param name     供应商名称 可以为null
     * @param pageable
     * @return 供应商列表
     */
    Page<TouristSupplier> supplierList(String name, Pageable pageable);

    /**
     * 修改供应商信息
     * @param
     * @param id                    供应商ID
     * @param address               供应商地址
     * @param contacts              联系人
     * @param contactNumber         联系电话
     * @param businessLicenseUri    营业执照uri
     * @param remarks               备注
     * @param detailedAddress       详细地址
     * @param supplierName          供应商名称
     */
    @Transactional
    void modifySupplier(Long id, Address address, String contacts, String contactNumber
            , String businessLicenseUri, String remarks, String detailedAddress, String supplierName);


    /**
     * 返回某个供应商的所有操作员
     * @param authSupplier      实际供应商
     * @param pageable          分页
     * @return
     */
    Page<TouristSupplier> getJurisdictionList(TouristSupplier authSupplier,Pageable pageable);

}
