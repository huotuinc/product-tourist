package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;

/**
 * 后台分销商(管理员)服务
 * Created by slt on 2016/12/17.
 */
public interface DistributorsService {

    /**
     * 获取供应商列表
     * @return 带分页信息的供应商列表
     */
    Page<TouristSupplier> getSupplierList();

}
