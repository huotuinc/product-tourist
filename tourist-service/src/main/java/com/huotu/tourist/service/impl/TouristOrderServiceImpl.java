package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.TravelerTypeEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.model.OrderStateQuery;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.TouristOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class TouristOrderServiceImpl implements TouristOrderService {
    @Autowired
    TouristOrderRepository touristOrderRepository;
    @Autowired
    TouristRouteRepository touristRouteRepository;
    @Autowired
    TravelerRepository travelerRepository;
    @Autowired
    TouristGoodRepository touristGoodRepository;
    @Autowired
    ConnectMallService connectMallService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TouristSupplierRepository touristSupplierRepository;

    @Override
    public URL startOrder(TouristGood good, Consumer<TouristOrder> success, Consumer<String> failed) {
        // TODO: 2017/1/3  
        return null;
    }

    @Override
    public Page<TouristOrder> touristOrders(TouristSupplier supplier, String supplierName, String orderNo
            , String touristName, String buyerName, String tel, PayTypeEnum payTypeEnum, LocalDateTime orderDate
            , LocalDateTime endOrderDate, LocalDateTime payDate, LocalDateTime endPayDate, LocalDateTime touristDate
            , LocalDateTime endTouristDate, OrderStateEnum orderStateEnum, Boolean settlement
            , Pageable pageable, Long settlementId) throws IOException {
        return touristOrderRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            if(settlement!=null){
                if(settlement){
                    predicate=cb.and(predicate,cb.isNotNull(root.get("settlement").as(SettlementSheet.class)));
                }else {
                    predicate=cb.and(predicate,cb.isNull(root.get("settlement").as(SettlementSheet.class)));
                }
            }
            if(settlementId!=null){
                predicate = cb.and(predicate,cb.equal(root.get("settlement").get("id").as(Long.class),
                        settlementId));
            }
            if (supplier != null) {
                predicate = cb.and(predicate,cb.equal(root.get("touristGood").get("touristSupplier").as(TouristSupplier.class),
                        supplier));
            }
            if (!StringUtils.isEmpty(supplierName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristGood").get("touristSupplier").get("supplierName").as(String
                                .class),
                        "%" + supplierName + "%"));
            }
            if (!StringUtils.isEmpty(orderNo)) {
                predicate = cb.and(predicate, cb.like(root.get("orderNo").as(String.class),
                        "%" + orderNo + "%"));
            }
            if (!StringUtils.isEmpty(touristName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristGood").get("touristName").as(String.class),
                        "%" + touristName + "%"));
            }
            if (!StringUtils.isEmpty(buyerName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristBuyer").get("buyerName").as(String.class),
                        "%" + buyerName + "%"));
            }
            if (!StringUtils.isEmpty(tel)) {
                predicate = cb.and(predicate, cb.like(root.get("touristBuyer").get("telPhone").as(String.class),
                        "%" + tel + "%"));
            }
            if (!StringUtils.isEmpty(payTypeEnum)) {
                predicate = cb.and(predicate, cb.equal(root.get("payType").as(PayTypeEnum.class),
                        payTypeEnum));
            }
            if (!StringUtils.isEmpty(orderStateEnum)) {
                predicate = cb.and(predicate, cb.equal(root.get("orderState").as(OrderStateEnum.class),
                        orderStateEnum));
            }
            if (orderDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class), orderDate));
            }

            if (endOrderDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createTime").as(LocalDateTime.class), endOrderDate));
            }

            if (payDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("payTime").as(LocalDateTime.class), payDate));
            }

            if (endPayDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("payTime").as(LocalDateTime.class), endPayDate));
            }
            if (touristDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.joinSet("travelers").get("route").get("fromDate")
                        .as(LocalDateTime.class), touristDate));
            }
            if(endTouristDate!=null){
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.joinSet("travelers").get("route").get("fromDate")
                        .as(LocalDateTime.class), endTouristDate));
            }
            return predicate;
        }, pageable);
    }

    @Override
    public BigDecimal countMoneyTotal(Long supplierId) throws IOException {
        if (supplierId == null) {
            throw new IOException();
        }
        TouristSupplier supplier=touristSupplierRepository.findOne(supplierId);
        return countOrderTotalMoney(supplier,null,null,null,null,null,null);
    }

    @Override
    public BigDecimal countOrderTotalMoney(TouristSupplier supplier, OrderStateEnum orderState, LocalDateTime createDate
            , LocalDateTime endCreateDate, Boolean settlement, TouristGood touristGood, TouristBuyer touristBuyer)
            throws IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = cb.createQuery(Number.class);
        Root<TouristOrder> root  = criteriaQuery.from(TouristOrder.class);
        Predicate predicate=cb.isTrue(cb.literal(true));

        if(supplier!=null){
            predicate=cb.and(predicate,cb.equal(root.get("touristGood").get("touristSupplier"),supplier));
        }

        if(orderState!=null){
            predicate=cb.and(predicate,cb.equal(root.get("orderState"),orderState));
        }

        if(createDate!=null){
            predicate=cb.and(predicate,cb.greaterThanOrEqualTo(root.get("createTime"),createDate));
        }
        if(endCreateDate!=null){
            predicate=cb.and(predicate,cb.lessThanOrEqualTo(root.get("createTime"),endCreateDate));
        }

        if(settlement!=null){
            predicate=cb.and(predicate,cb.isNotNull(root.get("settlement")));
        }

        if(touristGood!=null){
            predicate=cb.and(predicate,cb.equal(root.get("touristGood"),touristGood));
        }

        if(touristBuyer!=null){
            predicate=cb.and(predicate,cb.equal(root.get("touristBuyer"),touristBuyer));
        }

        criteriaQuery=criteriaQuery.where(predicate);

        criteriaQuery  = criteriaQuery.select(
                cb.sum(root.get("orderMoney"))
        );

        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
        Number number=query.getSingleResult();

        BigDecimal orderTotalMoney=BigDecimal.valueOf(number==null?0:number.doubleValue());
        return orderTotalMoney;
    }

    @Override
    public BigDecimal countOrderTotalcommission(TouristSupplier supplier, OrderStateEnum orderState, LocalDateTime createDate, LocalDateTime endCreateDate, Boolean settlement, TouristGood touristGood, TouristBuyer touristBuyer) throws IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = cb.createQuery(Number.class);
        Root<TouristOrder> root  = criteriaQuery.from(TouristOrder.class);
        Predicate predicate=cb.isTrue(cb.literal(true));

        if(supplier!=null){
            predicate=cb.and(predicate,cb.equal(root.get("touristGood").get("touristSupplier"),supplier));
        }

        if(orderState!=null){
            predicate=cb.and(predicate,cb.equal(root.get("orderState"),orderState));
        }

        if(createDate!=null){
            predicate=cb.and(predicate,cb.greaterThanOrEqualTo(root.get("createTime"),createDate));
        }
        if(endCreateDate!=null){
            predicate=cb.and(predicate,cb.lessThanOrEqualTo(root.get("createTime"),endCreateDate));
        }

        if(settlement!=null){
            predicate=cb.and(predicate,cb.isNotNull(root.get("settlement")));
        }

        if(touristGood!=null){
            predicate=cb.and(predicate,cb.equal(root.get("touristGood"),touristGood));
        }

        if(touristBuyer!=null){
            predicate=cb.and(predicate,cb.equal(root.get("touristBuyer"),touristBuyer));
        }

        criteriaQuery=criteriaQuery.where(predicate);


        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
        BigDecimal orderTotalMoney=BigDecimal.valueOf(query.getSingleResult()==null?0:query.getSingleResult()
                .doubleValue());
        return orderTotalMoney;
    }

    @Override
    public BigDecimal countCommissionTotal(Long supplierId) throws IOException {
        if (supplierId == null) {
            throw new IOException();
        }
        return touristOrderRepository.sumCommissionTotal(supplierId);
    }

    @Override
    public BigDecimal countRefundTotal(Long supplierId) throws IOException {
        if (supplierId == null) {
            throw new IOException();
        }
        TouristSupplier supplier = touristSupplierRepository.findOne(supplierId);
        return countOrderTotalMoney(supplier, OrderStateEnum.RefundsFinish, null, null, null, null, null);
//        return touristOrderRepository.sumRefundTotal(supplierId, OrderStateEnum.RefundsFinish);
    }

    @Override
    public long countOrderTotal(Long supplierId) throws IOException {
        if (supplierId == null) {
            throw new IOException();
        }
        return touristOrderRepository.countByTouristGood_TouristSupplier_id(supplierId);
    }

    @Override
    public boolean checkOrderStatusCanBeModified(SystemUser user, OrderStateEnum formerStatus, OrderStateEnum laterStatus) {
        /**
         * 该订单是否可以被修改到相应订单状态
         */
        if(!Arrays.asList(OrderStateQuery.revisability[(int)formerStatus.getCode()]).contains(laterStatus)){
            return false;
        }
        /**
         * 该角色是否拥有修改到相应订单状态的权利
         */
        if(!Arrays.asList(OrderStateQuery.getAuthOrderStates(user)).contains(laterStatus)){
            return false;
        }
        return true;
    }

    @Override
    public List<OrderStateEnum> getModifyState(SystemUser user, TouristOrder touristOrder) {
        //获取该权限可以修改的订单状态
        OrderStateEnum[] orderStates= OrderStateQuery.getAuthOrderStates(user);
        OrderStateEnum orderformerState=touristOrder.getOrderState();
        List<OrderStateEnum> orderlaterStates=new ArrayList<>();
        //筛选可以被修改到之后的订单状态
        for(OrderStateEnum orderlater:orderStates){
            if(Arrays.asList(OrderStateQuery.revisability[(int)orderformerState.getCode()]).contains(orderlater)){
                orderlaterStates.add(orderlater);
            }
        }
        return orderlaterStates;
    }

    @Override
    public TouristOrder addOrderInfo(TouristBuyer user, List<Traveler> travelers, Long goodId, Long routeId
            , Float mallIntegral, Float mallBalance, Float mallCoffers, String remark) throws IOException, IllegalStateException {
        if (travelers != null && travelers.size() > 0) {
            TouristGood good = touristGoodRepository.getOne(goodId);
            TouristRoute route = touristRouteRepository.getOne(routeId);
            int num = travelerRepository.countByRoute(route);
            if (good.getMaxPeople() - num >= travelers.size()) {
                //todo 锁定线路不允许被其他游客同时添加
                Random random = new Random();
                TouristOrder order = new TouristOrder();
                order.setTouristGood(good);
                order.setOrderState(OrderStateEnum.NotPay);
                order.setSettlement(null);
                order.setOrderNo(LocalDateTimeFormatter.toStr2(LocalDateTime.now()) + "" + (random.nextInt(99999) + 10000));
                order.setCreateTime(LocalDateTime.now());
                order.setRemarks(remark);
                BigDecimal adult = good.getPrice().multiply(new BigDecimal(travelers.stream().filter(traveler ->
                        traveler
                                .getTravelerType().equals
                                (TravelerTypeEnum.adult)).count()));

                BigDecimal child = good.getPrice()
                        .multiply(good.getChildrenDiscount().divide(new BigDecimal(10)))
                        .multiply(new BigDecimal(travelers.stream()
                                .filter(traveler -> traveler.getTravelerType() == TravelerTypeEnum.children)
                                .count()
                        ));
                BigDecimal sumNum = adult.add(child);
                order.setMallIntegral(mallIntegral == null ? new BigDecimal(0) : new BigDecimal(mallIntegral));
                order.setMallBalance(mallBalance == null ? new BigDecimal(0) : new BigDecimal(mallBalance));
                order.setMallCoffers(mallCoffers == null ? new BigDecimal(0) : new BigDecimal(mallCoffers));
                //成人+儿童价格
                order.setOrderMoney(sumNum);
                order.setTouristBuyer(user);
                for (Traveler traveler : travelers) {
                    traveler.setCreateTime(LocalDateTime.now());
                    traveler.setRoute(route);
                    traveler.setOrder(order);
                }
                travelerRepository.save(travelers);
                order.setTravelers(travelers);
                order = touristOrderRepository.saveAndFlush(order);
                return order;
            }
            return null;
        } else {
            throw new IOException("游客不能为空");
        }
    }

    @Override
    public List<TouristOrder> getBuyerOrders(Long buyerId, Long lastId, String states) throws IOException {

        return touristOrderRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("touristBuyer").get("id").as(Long.class), buyerId);
            if(lastId!=null){
                predicate=cb.and(predicate,cb.lessThan(root.get("id").as(Long.class),lastId));
            }
            if("going".equals(states)){
                predicate=cb.and(predicate,root.get("orderState").as(OrderStateEnum.class)
                        .in(Arrays.asList(OrderStateEnum.NotPay
                        , OrderStateEnum.PayFinish,OrderStateEnum.NotFinish,OrderStateEnum.Refunds)));
            }else if("finish".equals(states)){
                predicate=cb.and(predicate,root.get("orderState").as(OrderStateEnum.class)
                        .in(Arrays.asList(OrderStateEnum.Finish,OrderStateEnum.RefundsFinish)));
            }else if ("cancel".equals(states)){
                predicate=cb.and(predicate,cb.equal(
                        root.get("orderState").as(OrderStateEnum.class),OrderStateEnum.Invalid));
            }
            return predicate;
        }, new PageRequest(0,20,new Sort(Sort.Direction.DESC,"id"))).getContent();
    }
}
