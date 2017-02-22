package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.repository.TouristBuyerRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.TouristBuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class TouristBuyerServiceImpl implements TouristBuyerService {
    @Autowired
    TouristBuyerRepository touristBuyerRepository;

    @Autowired
    ConnectMallService connectMallService;

    @Override
    public TouristBuyer save(TouristBuyer data) {
        return touristBuyerRepository.saveAndFlush(data);
    }

    @Override
    public TouristBuyer getOne(Long aLong) {
        return touristBuyerRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        touristBuyerRepository.delete(aLong);
    }

    @Override
    public Page<TouristBuyer> buyerList(String buyerName, String buyerDirector, String telPhone, BuyerCheckStateEnum checkState, Pageable pageable) {
        return touristBuyerRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            predicate = cb.and(predicate, cb.isNotNull(root.get("buyerName")));
            predicate = cb.and(predicate, cb.isNotNull(root.get("buyerDirector")));
            predicate = cb.and(predicate, cb.isNotNull(root.get("telPhone")));

            if (!StringUtils.isEmpty(buyerName)) {
                predicate = cb.and(predicate, cb.like(root.get("buyerName").as(String.class),
                        buyerName));
            }
            if (!StringUtils.isEmpty(buyerDirector)) {
                predicate = cb.and(predicate, cb.like(root.get("buyerDirector").as(String.class),
                        buyerDirector));
            }
            if (!StringUtils.isEmpty(telPhone)) {
                predicate = cb.and(predicate, cb.like(root.get("telPhone").as(String.class),
                        telPhone));
            }
            if (checkState != null) {
                predicate = cb.and(predicate, cb.equal(root.get("checkState").as(BuyerCheckStateEnum.class),
                        checkState));
            }
            return predicate;
        }, pageable);
    }

    @Override
    public void chargeMoney(TouristOrder order) throws IOException {
        BigDecimal commission = order.getOrderMoney().multiply(order.getTouristGood().getRebate())
                .divide(BigDecimal.valueOf(100));


//        connectMallService.changeUserBalance(order.getTouristBuyer().getId(),commission);
//
//        connectMallService.saveBuyerMallAdvanceLogs()



    }
}
