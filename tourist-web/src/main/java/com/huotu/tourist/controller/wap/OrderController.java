package com.huotu.tourist.controller.wap;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.repository.TouristBuyerRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.TouristOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单逻辑
 * Created by slt on 2017/1/12.
 */
@Controller
@RequestMapping(value = "/wap/")
public class OrderController {
    private static final Log log = LogFactory.getLog(OrderController.class);
    @Autowired
    TouristBuyerRepository touristBuyerRepository;
    @Autowired
    private TouristOrderRepository touristOrderRepository;
    @Autowired
    private TouristOrderService touristOrderService;
    @Autowired
    private TravelerRepository travelerRepository;
    @Autowired
    private ConnectMallService connectMallService;
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


    /**
     * 采购商资格订单支付
     * <p>
     *
     * @param payType 支付方式
     * @return
     */
    @RequestMapping(value = {"/buyerOrderPay"}, method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map buyerOrderPay(@AuthenticationPrincipal SystemUser user, @RequestParam PayTypeEnum
            payType) {
        Map map = new HashMap();
        if (user.isBuyer()) {
            try {
                //采购商资格支付订单
                TouristBuyer buyer = (TouristBuyer) user;
                buyer.setPayType(payType);
                String mallOrderId = connectMallService.pushBuyerOrderToMall(buyer);
                buyer.setMallOrderNo(mallOrderId);
                touristBuyerRepository.saveAndFlush(buyer);
                map.put("code", 200);
                map.put("orderId", mallOrderId);
                map.put("customerId", connectMallService.getMerchant().getId());
                map.put("msg", "success");
                return map;
            } catch (IOException e) {
                log.error(e.getMessage());
                map.put("code", 500);
                map.put("msg", e.getMessage());
                return map;
            }
        } else {
            map.put("code", 500);
            map.put("msg", "用户不是采购商");
        }
        return map;
    }

    /**
     * 线路订单支付
     * <p>
     *
     * @param orderId 订单id
     * @param payType 支付方式
     * @return
     */
    @RequestMapping(value = {"/orderPay"}, method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map orderPay(@AuthenticationPrincipal SystemUser user, @RequestParam Long orderId, @RequestParam PayTypeEnum
            payType) {
        Map map = new HashMap();
        if (user.isBuyer()) {
            try {
                TouristOrder order = touristOrderRepository.getOne(orderId);
                String mallOrderNo;
                order.setPayType(payType);
                mallOrderNo = connectMallService.pushOrderToMall(order);
                order.setMallOrderNo(mallOrderNo);
                map.put("code", 200);
                map.put("msg", "success");
                return map;
            } catch (IOException e) {
                log.error(e.getMessage());
                map.put("code", 500);
                map.put("msg", e.getMessage());
                return map;
            }
        } else {
            map.put("code", 500);
            map.put("msg", "用户不是采购商");
        }
        return map;
    }


    /**
     * 商场订单支付回调
     *
     * @param mallOrderNo 商城订单号
     * @param pay         是否支付成功
     * @param payType     支付类型
     * @param orderType   订单类型 0 线路订单，1 采购商订单
     * @param model
     * @return
     */
    @RequestMapping(value = {"/orderPayCallback"})
    @Transactional
    public String orderPayCallback(@AuthenticationPrincipal SystemUser user, @RequestParam String mallOrderNo,
                                   @RequestParam PayTypeEnum payType, @RequestParam boolean pay, int orderType, Model
                                           model) {
        if (user.isBuyer()) {
            TouristBuyer buyer = (TouristBuyer) user;
            if (orderType == 0) {
                TouristOrder touristOrder = touristOrderRepository.findByMallOrderNo(mallOrderNo);
                if (pay && touristOrder.getTouristBuyer().getId().equals(buyer.getId()) && touristOrder.getOrderState()
                        .equals(OrderStateEnum.NotPay)) {
                    touristOrder.setPayType(payType);
                    touristOrder.setPayTime(LocalDateTime.now());
                    model.addAttribute("mallOrderNo", mallOrderNo);
                    return "view/wap/paySuccess.html";
                }
                model.addAttribute("errorMsg", "当前采购商与订单采购商不匹配或订单状态异常");
            } else {
                // TODO: 2017/1/19  采购商支付开通
            }
        } else {
            model.addAttribute("errorMsg", "警告非法的用户访问，以记录下IP");
        }
        return "wap/errorMsg.html";
    }
}
