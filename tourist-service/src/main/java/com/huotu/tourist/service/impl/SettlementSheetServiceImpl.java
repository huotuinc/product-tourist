package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.SettlementSheetRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.SettlementSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class SettlementSheetServiceImpl implements SettlementSheetService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    SettlementSheetRepository settlementSheetRepository;
    @Autowired
    private TouristOrderRepository touristOrderRepository;

    @Override
    public SettlementSheet save(SettlementSheet data) {
        return settlementSheetRepository.saveAndFlush(data);
    }

    @Override
    public SettlementSheet getOne(Long aLong) {
        return settlementSheetRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        settlementSheetRepository.delete(aLong);
    }

    @Override
    public Page<SettlementSheet> settlementSheetList(TouristSupplier touristSupplier, String supplierName
            , SettlementStateEnum platformChecking, LocalDateTime createTime, LocalDateTime endCreateTime
            , Pageable pageable) {
        return settlementSheetRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            if(touristSupplier!=null){
                predicate = cb.and(predicate, cb.equal(root.get("touristSupplier").as(TouristSupplier.class),
                        touristSupplier));
            }
            if (!StringUtils.isEmpty(supplierName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristSupplier").get("supplierName").as(String.class),
                        supplierName));
            }
            if (platformChecking != null) {
                predicate = cb.and(predicate, cb.equal(root.get("platformChecking").as(SettlementStateEnum.class),
                        platformChecking));
            }
            if (createTime != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),
                        createTime));
            }
            if (endCreateTime != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),
                        endCreateTime));
            }
            return predicate;
        }, pageable);
    }

    @Override
    public BigDecimal countSettled(TouristSupplier supplier) throws IOException {
        return touristOrderRepository.countSupplierSettled(supplier, OrderStateEnum.Finish);
    }

    @Override
    public BigDecimal countNotSettled(TouristSupplier supplier) throws IOException {
        return touristOrderRepository.countSupplierNotSettled(supplier, OrderStateEnum.Finish);
    }

    @Override
    public BigDecimal countWithdrawal(TouristSupplier supplier) throws IOException {
        return settlementSheetRepository.countWithdrawal(supplier);
    }


    @Override
    public BigDecimal countBalance(TouristSupplier supplier, LocalDateTime endCountDate) throws IOException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);

        Root<TouristOrder> root  = criteriaQuery.from(TouristOrder.class);

        Predicate predicate=criteriaBuilder.and(criteriaBuilder.equal(root.get("touristGood").get("touristSupplier"),
                supplier),criteriaBuilder.isNotNull(root.get("settlement")));

        if(endCountDate!=null){
            predicate=criteriaBuilder.and(predicate,criteriaBuilder.lessThanOrEqualTo(root.get("createTime"),
                    endCountDate));
        }
        criteriaQuery=criteriaQuery.where(predicate);

        criteriaQuery  = criteriaQuery.select(
                criteriaBuilder.sum(root.get("orderMoney"))
        );

        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);

        BigDecimal number=BigDecimal.valueOf(query.getSingleResult()==null?0:query.getSingleResult().doubleValue())
                .setScale(2,RoundingMode.HALF_UP);

        return number;

    }
}
