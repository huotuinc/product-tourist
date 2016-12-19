package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 供应商逻辑
 * Created by slt on 2016/12/17.
 */
@Controller("/supplier")
public class SupplierManageController {

    /**
     * 根据某个供应商的订单列表，
     * @param supplierId    供应商(必须)
     * @param pageNo        第几页(必须)
     * @param pageSize      每页几条信息(必须)
     * @param orderId       订单ID
     * @param name          路线名称
     * @param buyer         购买人
     * @param tel           购买人电话
     * @param payTypeEnum   付款状态
     * @param orderDate     下单时间
     * @param payDate       支付时间
     * @param touristDate   出行时间
     * @return
     * @throws IOException
     */
    @RequestMapping("/orderList")
    public String orderList(Long supplierId, Integer pageNo, Integer pageSize, String orderId, String name
            , String buyer, String tel, PayTypeEnum payTypeEnum, LocalDate orderDate, LocalDate payDate
            , LocalDate touristDate, Model model) throws IOException{

        return "";

    }

}
