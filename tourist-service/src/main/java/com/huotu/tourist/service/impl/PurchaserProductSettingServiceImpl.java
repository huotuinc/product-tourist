package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.PurchaserProductSetting;
import com.huotu.tourist.repository.PurchaserProductSettingRepository;
import com.huotu.tourist.service.PurchaserProductSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;

/**
 * Created by lhx on 2017/1/4.
 */
@Service
public class PurchaserProductSettingServiceImpl implements PurchaserProductSettingService {
    @Autowired
    PurchaserProductSettingRepository purchaserProductSettingRepository;

    @Override
    public PurchaserProductSetting save(PurchaserProductSetting data) {
        return purchaserProductSettingRepository.saveAndFlush(data);
    }

    @Override
    public PurchaserProductSetting getOne(Long aLong) {
        return purchaserProductSettingRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        purchaserProductSettingRepository.delete(aLong);
    }

    @Override
    public Page<PurchaserProductSetting> purchaserProductSettingList(String name, Pageable pageable) {
        return purchaserProductSettingRepository.findAll((root, query, cb) -> {
            Predicate predicate = null;
            if (!StringUtils.isEmpty(name)) {
                predicate = cb.and(predicate, cb.like(root.get("name").as(String.class),
                        name));
            }
            return predicate;
        }, pageable);
    }
}
