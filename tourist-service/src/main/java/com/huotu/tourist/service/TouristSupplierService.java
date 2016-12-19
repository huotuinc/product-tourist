package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 供应商相关服务
 * Created by lhx on 2016/12/17.
 */

public interface TouristSupplierService {

    /**
     * 供应商列表
     *
     * @param name     供应商名称 可以为null
     * @param pageable
     * @return 供应商列表
     */
    Page<TouristSupplier> supplierList(String name, Pageable pageable);

    /**
     * 创建一个供应商
     *
     * @param supplier 供应商 not null
     * @return 返回新增的供应商
     */
    TouristSupplier createSupplier(TouristSupplier supplier);

    /**
     * 修改供应商
     *
     * @param supplier 供应商 not null
     * @return 返回修改后的供应商
     */
    TouristSupplier updateSupplier(TouristSupplier supplier);


}
