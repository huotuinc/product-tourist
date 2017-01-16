package com.huotu.tourist.controller.wap;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.entity.PurchaserProductSetting;
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
import java.util.List;

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
    private PurchaserProductSettingRepository purchaserProductSettingRepository;

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
            //成为采购商页面
            return viewWapPath+"buyerApply.html";
        }
        TouristBuyer touristBuyer=touristBuyerRepository.getOne(buyerId);
        if(BuyerCheckStateEnum.Checking.equals(touristBuyer.getCheckState())){
            //审核中，页面
            return viewWapPath+"msg.html";
        }else if(BuyerCheckStateEnum.Frozen.equals(touristBuyer.getCheckState())){
            //该账户已被冻结
            return viewWapPath+"frozen.html";
        }else if(BuyerPayStateEnum.NotPay.equals(touristBuyer.getPayState())){
            //已审核未付钱，去付钱页面
            PurchaserProductSetting setting=new PurchaserProductSetting();
            List<PurchaserProductSetting> settings=purchaserProductSettingRepository.findAll();
            if(settings!=null&&!settings.isEmpty()){
                setting=settings.get(0);
            }
            model.addAttribute("PurchaserProductSetting",setting);
            model.addAttribute("buyerId",touristBuyer.getId());
            return viewWapPath+"submission.html";
        }

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
     * 显示某采购商的订单列表页面
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
