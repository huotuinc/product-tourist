package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.repository.PurchaserPaymentRecordRepository;
import com.huotu.tourist.service.PurchaserPaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

/**
 * Created by lhx on 2017/1/4.
 */
@Service
public class PurchaserPaymentRecordServiceImpl implements PurchaserPaymentRecordService {
    @Autowired
    PurchaserPaymentRecordRepository purchaserPaymentRecordRepository;


    @Override
    public PurchaserPaymentRecord save(PurchaserPaymentRecord data) {
        return purchaserPaymentRecordRepository.saveAndFlush(data);
    }

    @Override
    public PurchaserPaymentRecord getOne(Long aLong) {
        return purchaserPaymentRecordRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        purchaserPaymentRecordRepository.delete(aLong);
    }

    @Override
    public Page<PurchaserPaymentRecord> purchaserPaymentRecordList(LocalDateTime startPayTime, LocalDateTime endPayTime, String buyerName
            , String buyerDirector, String telPhone, Pageable pageable) {
        return purchaserPaymentRecordRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            if (!StringUtils.isEmpty(buyerName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristBuyer").get("buyerName").as(String.class),
                        buyerName));
            }
            if (!StringUtils.isEmpty(buyerDirector)) {
                predicate = cb.and(predicate, cb.like(root.get("touristBuyer").get("buyerDirector").as(String.class),
                        buyerDirector));
            }
            if (!StringUtils.isEmpty(telPhone)) {
                predicate = cb.and(predicate, cb.like(root.get("touristBuyer").get("telPhone").as(String.class),
                        telPhone));
            }
            if (!StringUtils.isEmpty(startPayTime)) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("payDate").as(LocalDateTime.class),
                        startPayTime));
            }
            if (!StringUtils.isEmpty(endPayTime)) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("payDate").as(LocalDateTime.class),
                        endPayTime));
            }
            return predicate;
        }, pageable);
    }
}
