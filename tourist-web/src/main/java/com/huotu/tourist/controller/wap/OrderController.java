package com.huotu.tourist.controller.wap;

import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.TouristOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * 订单逻辑
 * Created by slt on 2017/1/12.
 */
@Controller
@RequestMapping(value = "/wap/")
public class OrderController {

    @Autowired
    private TouristOrderRepository touristOrderRepository;

    @Autowired
    private TouristOrderService touristOrderService;

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private TouristSupplierRepository touristSupplierRepository;

    private String viewWapPath="/view/wap/";

    /**
     *  获取某采购商的订单列表
     * @param lastId        最后一条ID
     * @param states        状态
     * @param buyerId       采购商ID
     * @return
     * @throws IOException
     */
    @RequestMapping("/newBuyerOrderList")
    public String newBuyerOrderList(Long lastId,Long buyerId, String states, Model model) throws IOException{

        List<TouristOrder> orders=touristOrderService.getBuyerOrders(buyerId,lastId,states);
        model.addAttribute("list",orders);
        model.addAttribute("states",states);
        model.addAttribute("buyerId",buyerId);
        return viewWapPath+"newOrder.html";
    }

    /**
     * 前台查看某个订单的信息
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping("/showOrderInfo")
    public String showOrderInfo(@RequestParam Long orderId,Model model) throws IOException{
        TouristOrder order = touristOrderRepository.findOne(orderId);

        model.addAttribute("order", order);

        List<Traveler> travelers = travelerRepository.findByOrder_Id(orderId);

        model.addAttribute("route", travelers.isEmpty()?new TouristRoute():travelers.get(0).getRoute());

        model.addAttribute("travelers", travelers);

        return viewWapPath+"orderInfo.html";

    }
}
