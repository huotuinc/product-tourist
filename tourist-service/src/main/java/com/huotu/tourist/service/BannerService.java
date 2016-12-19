package com.huotu.tourist.service;

import com.huotu.tourist.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Banner服务
 * Created by lhx on 2016/12/17.
 */

public interface BannerService extends BaseService<Banner, Long> {


    /**
     * Banenr列表
     *
     * @param pageable
     * @return 结算列表
     */
    Page<Banner> bannerList(Pageable pageable);


}
