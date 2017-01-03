package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.TouristOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class TouristOrderServiceImpl implements TouristOrderService {
    @Autowired
    TouristOrderRepository touristOrderRepository;

    @Override
    public URL startOrder(TouristGood good, Consumer<TouristOrder> success, Consumer<String> failed) {
        return null;
    }

    @Override
    public Page<TouristOrder> touristOrders(TouristSupplier supplier, String supplierName, String orderId, String touristName, String buyerName, String tel, PayTypeEnum payTypeEnum, LocalDate orderDate, LocalDate endOrderDate, LocalDate payDate, LocalDate endPayDate, LocalDate touristDate, OrderStateEnum orderStateEnum, Pageable pageable) throws IOException {
        return null;
    }

    @Override
    public long countMoneyTotal(Long supplierId) throws IOException {
        return 0;
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
        return touristOrderRepository.sumRefundTotal(supplierId, OrderStateEnum.RefundsFinish);
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
        return false;
    }

    @Override
    public List<OrderStateEnum> getModifyState(SystemUser user, TouristOrder touristOrder) {
        return null;
    }
}
