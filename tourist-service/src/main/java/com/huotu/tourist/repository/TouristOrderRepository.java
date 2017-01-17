package com.huotu.tourist.repository;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 线路订单持久层
 * Created by slt on 2016/12/19.
 */
public interface TouristOrderRepository extends JpaRepository<TouristOrder, Long>,
        JpaSpecificationExecutor<TouristOrder> {

    @Query("update TouristOrder as o set o.remarks=?1 where o.id=?2")
    @Modifying
    @Transactional
    int modifyRemarks(String remark, Long id);

    @Query("update TouristOrder as o set o.orderState=?1 where o.id=?2")
    @Modifying
    @Transactional
    int modifyOrderState(OrderStateEnum orderState, Long id);

    Long countByTouristGood(TouristGood good);

    @Query("select sum(t.orderMoney) as om from TouristOrder as t where t.touristGood=?1")
    BigDecimal countOrderMoney(TouristGood good);

    /**
     * 某个供应商已结算的钱
     * @param touristSupplier
     * @param state
     * @return
     */
    @Query("select sum(t.orderMoney) as om from TouristOrder as t where t.touristGood.touristSupplier=?1 " +
            "and t.settlement is not null and t.orderState=?2")
    BigDecimal countSupplierSettled(TouristSupplier touristSupplier,OrderStateEnum state);

    /**
     * 某个供应商未结算的钱
     * @param touristSupplier
     * @param state
     * @return
     */
    @Query("select sum(t.orderMoney) as om from TouristOrder as t where t.touristGood.touristSupplier=?1 " +
            "and t.settlement is null and t.orderState=?2")
    BigDecimal countSupplierNotSettled(TouristSupplier touristSupplier,OrderStateEnum state);

    /**
     * 统计当前状态订单的金额
     *
     * @param supplierId
     * @param orderState
     * @return
     */
    @Query("select sum(t.orderMoney) as om from TouristOrder as t where t.touristGood.touristSupplier.id=?1 and t" +
            ".orderState=?2")
    BigDecimal sumRefundTotal(Long supplierId, OrderStateEnum orderState);

    /**
     * 统计供应商订单的总佣金
     *
     * @param supplierId
     * @return
     */
    @Query("select sum(t.orderMoney*t.touristGood.rebate/100) as om from TouristOrder as t where t.touristGood.touristSupplier.id=?1 ")
    BigDecimal sumCommissionTotal(Long supplierId);

    /**
     * 统计供应商所有订单的总金额
     *
     * @param supplierId
     * @return
     */
    @Query("select sum(t.orderMoney) as om from TouristOrder as t where t.touristGood.touristSupplier.id=?1 ")
    BigDecimal sumMoneyTotal(Long supplierId);


    long countByTouristGood_TouristSupplier_id(Long supplierId);


    @Query("select o,count(o.touristGood) as num from TouristOrder as o where o.touristGood.touristSupplier.id=?1 order by num desc")
    Page<Object> goodsSalesRanking(Long supplierId, Pageable pageable);

    @Query("select o,count(o.touristGood) as num from TouristOrder as o where o.touristGood.touristSupplier.id=?1" +
            " and o.createTime>?2 and o.createTime<?3 order by num desc")
    Page<Object> goodsSalesRankingByDate(Long supplierId, LocalDateTime orderDate,LocalDateTime endOrderDate
                                         ,Pageable pageable);

    /**
     * 查找某个采购商的某些订单状态的已结算的数量
     * @param buyer         采购商
     * @param orderStates   订单状态列表
     * @return
     */
    @Query("select count(t) from TouristOrder as t where t.touristBuyer=?1 and t.settlement is not null" +
            " and t.orderState in ?2")
    long countByTouristBuyerAndOrderStates(TouristBuyer buyer,List<OrderStateEnum> orderStates);

    /**
     * 查找某个采购商的某个订单的状态的已结算的数量
     * @param buyer         采购商
     * @param orderState    订单状态
     * @return
     */
    @Query("select count(t) from TouristOrder as t where t.touristBuyer=?1 and t.settlement is not null " +
            "and t.orderState=?2")
    long countByTouristBuyerAndOrderState(TouristBuyer buyer,OrderStateEnum orderState);

    @Query("select sum(t.orderMoney*t.touristGood.rebate/100) from TouristOrder as t where t.touristBuyer=?1")
    BigDecimal sumCommissionByBuyer(TouristBuyer buyer);

    @Query("select t.touristGood.activityType as tp,count(t) as n from TouristOrder t" +
            " group by tp order by n desc")
    Object[] searchActivityTypeGruop();

    TouristOrder findByMallOrderNo(Long mallOrderNo);


//    @Query("select sum(o.orderMoney) from TouristOrder as o where o.touristGood.touristSupplier=?1" +
//            "and o.settlement is not  null")
//    BigDecimal countSettledMoney(TouristSupplier supplier,LocalDateTime endCountDate);


}
