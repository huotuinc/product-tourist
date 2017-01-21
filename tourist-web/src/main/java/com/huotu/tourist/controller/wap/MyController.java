package com.huotu.tourist.controller.wap;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.repository.*;
import com.huotu.tourist.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log log = LogFactory.getLog(MyController.class);

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
    private TouristBuyerRepository touristBuyerRepository;

    @Autowired
    private PurchaserProductSettingRepository purchaserProductSettingRepository;

    @Autowired
    private ConnectMallService connectMallService;

    private String viewWapPath="/view/wap/";


    /**
     * 显示个人中心页面 “我的”页面
     * @param model
     * @return          视图
     * @throws IOException
     */
    @RequestMapping("/showMyInfo")
    public String showMyInfo(@AuthenticationPrincipal SystemUser user, Model model) throws IOException{
        TouristBuyer authTouristBuyer=(TouristBuyer)user;
        TouristBuyer touristBuyer=touristBuyerRepository.getOne(authTouristBuyer.getId());

        //账户是否被冻结
        if(BuyerCheckStateEnum.Frozen.equals(touristBuyer.getCheckState())){
            return viewWapPath+"frozen.html";
        }

        //申请采购商
        if(touristBuyer.getCheckState()==null){
            return "redirect:/wap/toSubmission";
        }

        //该采购商还在审核
        if(BuyerCheckStateEnum.Checking.equals(touristBuyer.getCheckState())){
            return viewWapPath+"msg.html";
        }

        //已审核未付钱，去付钱页面
        if(!BuyerPayStateEnum.PayFinish.equals(touristBuyer.getPayState())){
            PurchaserProductSetting setting=new PurchaserProductSetting();
            List<PurchaserProductSetting> settings=purchaserProductSettingRepository.findAll();
            if(settings!=null&&!settings.isEmpty()){
                setting=settings.get(0);
            }
            model.addAttribute("setting",setting);
            model.addAttribute("buyerId",touristBuyer.getId());
            return viewWapPath+"submission.html";
        }


//        String headUrl=connectMallService.getTouristBuyerHeadUrl(touristBuyer);
        long allNotFinish=touristOrderRepository.countByTouristBuyerAndOrderStates(
                touristBuyer, Arrays.asList(OrderStateEnum.NotPay,OrderStateEnum.PayFinish
                        ,OrderStateEnum.NotFinish,OrderStateEnum.Refunds));
        long allFinish=touristOrderRepository.countByTouristBuyerAndOrderStates(
                touristBuyer, Arrays.asList(OrderStateEnum.Finish,OrderStateEnum.RefundsFinish));
        long allInvalid=touristOrderRepository.countByTouristBuyerAndOrderState(
                touristBuyer,OrderStateEnum.Invalid);
        BigDecimal commission=touristOrderRepository.sumCommissionByBuyer(touristBuyer);

        model.addAttribute("headUrl","");
        model.addAttribute("buyer",touristBuyer);
        model.addAttribute("allNotFinish",allNotFinish);
        model.addAttribute("allFinish",allFinish);
        model.addAttribute("allInvalid",allInvalid);
        model.addAttribute("commission",commission);
        return viewWapPath+"my.html";
    }



    /**
     * 显示某采购商的订单列表页面
     * @return          列表视图
     * @throws IOException
     */
    @RequestMapping("/showAllOrders")
    public String showAllOrders(String states,Model model) throws IOException{
        model.addAttribute("states",states);
        return viewWapPath+"allOrder.html";
    }


    /**
     *  获取某采购商的订单列表
     * @param lastId        最后一条ID
     * @param states        状态
     * @return
     * @throws IOException
     */
    @RequestMapping("/newBuyerOrderList")
    public String newBuyerOrderList(@AuthenticationPrincipal SystemUser user,Long lastId, String states, Model model)
            throws IOException{

        TouristBuyer touristBuyer=(TouristBuyer)user;
        List<TouristOrder> orders=touristOrderService.getBuyerOrders(touristBuyer.getId(),lastId,states);
        model.addAttribute("list",orders);
        model.addAttribute("states",states);
        return viewWapPath+"newOrder.html";
    }

    /**
     * 前台查看某个订单的信息
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping("/showOrderInfo")
    public String showOrderInfo(@RequestParam Long orderId, Model model) throws IOException{
        TouristOrder order = touristOrderRepository.findOne(orderId);

        model.addAttribute("order", order);

        List<Traveler> travelers =order.getTravelers();

        model.addAttribute("route", travelers.isEmpty()?new TouristRoute():travelers.get(0).getRoute());

        model.addAttribute("travelers", travelers);

        return viewWapPath+"orderInfo.html";

    }


    /**
     * 采购商采购资格支付界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/toSubmission"})
    public String toSubmission(@AuthenticationPrincipal SystemUser user, Model model) {
//        List<PurchaserProductSetting> purchaserProductSettingList = purchaserProductSettingRepository.findAll();
//        PurchaserProductSetting setting;
//        if(purchaserProductSettingList.isEmpty()){
//            setting=new PurchaserProductSetting();
//        }else {
//            setting=purchaserProductSettingList.get(0);
//        }
//        model.addAttribute("purchaserProductSetting",setting);
        return viewWapPath+"buyerApply.html";
    }

//    /**
//     * 采购商申请支付 todo 跳转的页面
//     *
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = {"/buyerPay"})
//    @Transactional
//    public String buyerPay(@AuthenticationPrincipal SystemUser user, @RequestParam PayTypeEnum payType, Model model)
//            throws IOException{
//        if (user.isBuyer()) {
//            TouristBuyer touristBuyer=(TouristBuyer)user;
//            try {
//                touristBuyer.setPayType(payType);
//                String mallOrderNo=connectMallService.pushBuyerOrderToMall(touristBuyer);
//                touristBuyer.setLastQualificationMallTradeId(mallOrderNo);
//                // TODO: 2017/1/17 跳转至商场支付
//                return "redirect:";
//            } catch (IOException e) {
//                log.error(e.getMessage());
//                model.addAttribute("errorMsg", e.getMessage());
//            }
//        } else {
//            model.addAttribute("errorMsg", "当前用户不是采购商");
//        }
//        return viewWapPath+"errorMsg.html";
//    }





}
