package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.Banner;
import com.huotu.tourist.repository.BannerRepository;
import com.huotu.tourist.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    BannerRepository bannerRepository;

    @Override
    public Banner save(Banner data) {
        return bannerRepository.saveAndFlush(data);
    }

    @Override
    public Banner getOne(Long aLong) {
        return bannerRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        bannerRepository.delete(aLong);
    }

    @Override
    public Page<Banner> bannerList(Pageable pageable) {
        return bannerRepository.findAll(pageable);
    }
}
