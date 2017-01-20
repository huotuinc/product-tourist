package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.SettlementSheetRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.SettlementSheetService;
import com.huotu.tourist.service.TouristOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ConnectMallService connectMallService;

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
    @Scheduled(cron = "0 0 3 * * ?")
    public void settlementSheetTask() throws IOException {
        settleOrder();
    }

    @Override
    public void settleOrder() throws IOException {
        long days=connectMallService.getServiceDays();

        //订单的下单时间需要小于的时间
        LocalDateTime endTime= LocalDate.now().atStartOfDay().plusDays(-(days+1));

        //符合要求的订单列表
        List<TouristOrder> orders=touristOrderRepository.getsatisfactorySettlementOrders(OrderStateEnum.Finish,endTime);

        Map<Long,SettlementSheet> sheetMap=new HashMap<>();
        String localDateStr= LocalDate.now().toString().replace("-","");
        for(int i=0,size=orders.size();i<size;i++){
            TouristOrder order=orders.get(i);
            if(order.getTouristGood()==null){
                continue;
            }
            TouristSupplier supplier=order.getTouristGood().getTouristSupplier();
            SettlementSheet settlementSheet=sheetMap.get(supplier.getId());
            if(settlementSheet==null){
                settlementSheet=new SettlementSheet();
                settlementSheet.setSelfChecking(SettlementStateEnum.NotChecking);
                settlementSheet.setTouristSupplier(supplier);
                settlementSheet.setPlatformChecking(SettlementStateEnum.NotChecking);
                settlementSheet.setReceivableAccount(order.getOrderMoney());
                String str = String.format("%04d", i);
                //todo 结算单号生成规则前面八位日期后面四位是当天第几张结算单，
                //todo 不足补0:  当天第一张：201701010001，当天第1235张：201701011235
                settlementSheet.setSettlementNo(localDateStr+str);
                sheetMap.put(supplier.getId(),settlementSheet);
            }else {
                settlementSheet.setReceivableAccount(settlementSheet.getReceivableAccount().add(order.getOrderMoney()));
                sheetMap.put(supplier.getId(),settlementSheet);
            }
            order.setSettlement(settlementSheet);
        }
        for(Map.Entry<Long,SettlementSheet> s:sheetMap.entrySet()){
            settlementSheetRepository.save(s.getValue());
        }

        touristOrderRepository.save(orders);

    }

    @Override
    public SettlementSheet createSettlement(TouristSupplier supplier, BigDecimal receivableAccount) throws IOException {
        return null;
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
