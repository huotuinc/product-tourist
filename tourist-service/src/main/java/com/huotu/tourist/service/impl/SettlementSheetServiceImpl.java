package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.SettlementSheetRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.SettlementSheetService;
import com.huotu.tourist.service.TouristOrderService;
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
    @Autowired
    private TouristOrderService touristOrderService;

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
        BigDecimal settled=touristOrderRepository.countSupplierSettled(supplier, OrderStateEnum.Finish);
        return settled==null?new BigDecimal(0):settled.setScale(2,RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal countNotSettled(TouristSupplier supplier) throws IOException {
        BigDecimal notSettled=touristOrderRepository.countSupplierNotSettled(supplier, OrderStateEnum.Finish);
        return notSettled==null?new BigDecimal(0):notSettled.setScale(2,RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal countWithdrawal(TouristSupplier supplier, LocalDateTime endCountDate) throws IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = cb.createQuery(Number.class);
        Root<PresentRecord> root  = criteriaQuery.from(PresentRecord.class);
        Predicate predicate=cb.isTrue(cb.literal(true));
        if(supplier!=null){
            cb.and(predicate,cb.equal(root.get("touristSupplier"),supplier));
        }


        if(endCountDate!=null){
            predicate=cb.and(predicate,cb.lessThanOrEqualTo(root.get("createTime"),
                    endCountDate));
        }
        criteriaQuery=criteriaQuery.where(predicate);
        criteriaQuery  = criteriaQuery.select(
                cb.sum(root.get("amountOfMoney"))
        );
        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
        BigDecimal countWithdrawalTotal=BigDecimal.valueOf(query.getSingleResult()==null?0:query.getSingleResult().doubleValue());

        return countWithdrawalTotal;

    }


    @Override
    public BigDecimal countBalance(TouristSupplier supplier, LocalDateTime endCountDate) throws IOException {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        //计算所有已确认结算单总额
//        CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
//        Root<TouristOrder> root  = criteriaQuery.from(TouristOrder.class);
//        Predicate predicate=criteriaBuilder.and(criteriaBuilder.equal(root.get("touristGood").get("touristSupplier"),
//                supplier),criteriaBuilder.isNotNull(root.get("settlement")));
//        if(endCountDate!=null){
//            predicate=criteriaBuilder.and(predicate,criteriaBuilder.lessThanOrEqualTo(root.get("createTime"),
//                    endCountDate));
//        }
//        criteriaQuery=criteriaQuery.where(predicate);
//        criteriaQuery  = criteriaQuery.select(
//                criteriaBuilder.sum(root.get("orderMoney"))
//        );
//        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
//        BigDecimal settledTotal=BigDecimal.valueOf(query.getSingleResult()==null?0:query.getSingleResult().doubleValue());
//
//
//        //计算已经提现的钱
//        CriteriaQuery<Number> criteriaQuery2 = criteriaBuilder.createQuery(Number.class);
//        Root<PresentRecord> root2  = criteriaQuery.from(PresentRecord.class);
//        criteriaQuery2.where()

        BigDecimal orderTotal=touristOrderService.countOrderTotalMoney(supplier,OrderStateEnum.Finish,endCountDate,null,true,
                null,null);
        BigDecimal countWithdrawalTotal=countWithdrawal(supplier,endCountDate);

        return orderTotal.subtract(countWithdrawalTotal).setScale(2,RoundingMode.HALF_UP);
    }
}
