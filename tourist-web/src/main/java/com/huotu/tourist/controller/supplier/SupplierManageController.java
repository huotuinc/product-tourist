package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.model.TouristOrderModel;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.service.OrderService;
import com.huotu.tourist.service.TouristRouteService;
import com.huotu.tourist.service.TravelerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商逻辑
 * Created by slt on 2016/12/17.
 */
@Controller("/supplier")
public class SupplierManageController {

    @Autowired
    private TouristSupplierRepository touristSupplierRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TravelerService travelerService;

    @Autowired
    private TouristRouteService touristRouteService;


    /**
     * 打开订单列表页面
     * @return
     */
    @RequestMapping("/showOrderList")
    public String showOrderList(Model model){
        return "";
    }


    /**
     * 根据某个供应商的订单列表
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
     * @param orderStateEnum 结算状态
     * @param touristDate   出行时间
     * @return
     * @throws IOException
     */
    @RequestMapping("/orderList")
    @ResponseBody
    public ModelMap orderList(@RequestParam Long supplierId, @RequestParam Integer pageNo, @RequestParam Integer pageSize
            , String orderId, String name, String buyer, String tel, PayTypeEnum payTypeEnum, LocalDate orderDate
            , OrderStateEnum orderStateEnum, LocalDate payDate, LocalDate touristDate) throws IOException{

        TouristSupplier supplier= touristSupplierRepository.findOne(supplierId);

        Page<TouristOrder> orders=orderService.supplierOrders(supplier,new PageRequest(pageNo+1,pageSize),
                orderId, name, buyer, tel, payTypeEnum, orderDate, null, payDate, null, touristDate, orderStateEnum);

        List<TouristOrderModel> touristOrderModels=new ArrayList<>();

        orders.forEach(order->{
            TouristOrderModel model=new TouristOrderModel();
            model.setId(order.getId());
            model.setBuyerName(order.getTouristBuyer().getBuyerName());
            model.setTouristName(order.getTouristGood().getTouristName());
            model.setOrderMoney(order.getOrderMoney().doubleValue());
            model.setOrderState(order.getOrderState().getDescription());
            model.setTouristDate("");//todo 计算
            model.setPeopleNumber(0);//todo 计算
            model.setRemarks(order.getRemarks());
            touristOrderModels.add(model);
        });

        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("rows",touristOrderModels);
        modelMap.addAttribute("total",orders.getTotalElements());
        return modelMap;
    }

    /**
     * 获取所有与他相同的线路订单的出行时间以及人数
     * @param id    线路订单ID
     * @return
     * @throws IOException
     */
    @RequestMapping("/getAllOrderTouristDate")
    @ResponseBody
    public ModelMap getAllOrderTouristDate(@RequestParam Long id) throws IOException{





        return null;
    }




}
