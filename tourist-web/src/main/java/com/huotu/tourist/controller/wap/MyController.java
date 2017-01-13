package com.huotu.tourist.controller.wap;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.repository.*;
import com.huotu.tourist.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * “我的”页面业务逻辑层
 * Created by slt on 2017/1/11.
 */
@Controller
@RequestMapping(value = "/wap/")
public class MyController {
    @Autowired
    public TravelerRepository travelerRepository;
    @Autowired
    public TouristSupplierRepository touristSupplierRepository;
    @Autowired
    public TouristOrderService touristOrderService;
    @Autowired
    public TouristOrderRepository touristOrderRepository;
    @Autowired
    public TouristRouteRepository touristRouteRepository;
    @Autowired
    public TouristRouteService touristRouteService;
    @Autowired
    public TouristGoodService touristGoodService;
    @Autowired
    public TouristGoodRepository touristGoodRepository;
    @Autowired
    public PurchaserPaymentRecordService purchaserPaymentRecordService;
    @Autowired
    public ActivityTypeService activityTypeService;
    @Autowired
    public TouristTypeService touristTypeService;
    @Autowired
    private TouristTypeRepository touristTypeRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private TouristBuyerRepository touristBuyerRepository;

    @Autowired
    private ConnectMallService connectMallService;

    private String viewWapPath="/view/wap/";


    /**
     * 显示个人中心页面 “我的”页面
     * @param buyerId  采购商
     * @param model
     * @return          视图
     * @throws IOException
     */
    @RequestMapping("/showMyInfo")
    public String showMyInfo(@AuthenticationPrincipal SystemUser user,Long buyerId, Model model) throws IOException{
        if(user==null||!user.isBuyer()){
            return viewWapPath+"成为采购商的页面";
        }
        TouristBuyer touristBuyer=touristBuyerRepository.getOne(buyerId);
        String headUrl=connectMallService.getTouristBuyerHeadUrl(touristBuyer);
        long allNotFinish=touristOrderRepository.countByTouristBuyerAndOrderStates(
                touristBuyer, Arrays.asList(OrderStateEnum.NotPay,OrderStateEnum.PayFinish
                        ,OrderStateEnum.NotFinish,OrderStateEnum.Refunds));
        long allFinish=touristOrderRepository.countByTouristBuyerAndOrderStates(
                touristBuyer, Arrays.asList(OrderStateEnum.Finish,OrderStateEnum.RefundsFinish));
        long allInvalid=touristOrderRepository.countByTouristBuyerAndOrderState(
                touristBuyer,OrderStateEnum.Invalid);
        BigDecimal commission=touristOrderRepository.sumCommissionByBuyer(touristBuyer);

        model.addAttribute("headUrl",headUrl);
        model.addAttribute("buyer",touristBuyer);
        model.addAttribute("allNotFinish",allNotFinish);
        model.addAttribute("allFinish",allFinish);
        model.addAttribute("allInvalid",allInvalid);
        model.addAttribute("commission",commission);
        return viewWapPath+"my.html";
    }



    /**
     * 显示某供应商的订单列表页面
     * @param buyerId    采购商ID
     * @return          列表视图
     * @throws IOException
     */
    @RequestMapping("/showAllOrders")
    public String showAllOrders(@RequestParam Long buyerId,String states,Model model) throws IOException{
        model.addAttribute("states",states);
        model.addAttribute("buyerId",buyerId);
        return viewWapPath+"allOrder.html";
    }





}
