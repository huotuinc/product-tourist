package com.huotu.tourist.controller.wap;

import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.TouristOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @RequestMapping("/modifyOrderState")
//    @ResponseBody
//    public void modifyOrderState(Long orderId,)
}
