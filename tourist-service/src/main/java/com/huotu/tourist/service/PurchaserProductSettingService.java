package com.huotu.tourist.service;

import com.huotu.tourist.entity.PurchaserProductSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 线路产品设置
 * Created by lhx on 2016/12/19.
 */

public interface PurchaserProductSettingService extends BaseService<PurchaserProductSetting, Long> {

    /**
     * 线路产品设置
     *
     * @param name     线路产品名称可以为null
     * @param pageable
     * @return
     */
    Page<PurchaserProductSetting> purchaserProductSettingList(String name, Pageable pageable);

}
