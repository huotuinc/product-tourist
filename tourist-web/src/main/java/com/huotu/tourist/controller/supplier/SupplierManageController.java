/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.common.CollectionAccountTypeEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.controller.BaseController;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.CollectionAccount;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.model.PageAndSelection;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.CollectionAccountRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TouristTypeRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.CollectionAccountService;
import com.huotu.tourist.service.TouristGoodService;
import com.huotu.tourist.service.TouristOrderService;
import com.huotu.tourist.service.TouristRouteService;
import com.huotu.tourist.service.TouristSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商逻辑
 * Created by slt on 2016/12/17.
 */
@Controller
@RequestMapping("/supplier")
public class SupplierManageController extends BaseController {

    @Autowired
    private TouristSupplierRepository touristSupplierRepository;

    @Autowired
    private TouristOrderService touristOrderService;

    @Autowired
    private TouristOrderRepository touristOrderRepository;

    @Autowired
    private TouristRouteRepository touristRouteRepository;

    @Autowired
    private TouristRouteService touristRouteService;

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private TouristGoodService touristGoodService;

    @Autowired
    private TouristGoodRepository touristGoodRepository;

    @Autowired
    private TouristTypeRepository touristTypeRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private TouristSupplierService touristSupplierService;

    @Autowired
    private CollectionAccountRepository collectionAccountRepository;

    @Autowired
    private CollectionAccountService collectionAccountService;


    /**
     * 打开订单列表页面
     *
     * @return
     */
    @RequestMapping("/")
    public String showSupplierMain() {
        return "/view/manage/supplier/main.html";
    }

    /**
     * 打开订单列表页面
     *
     * @return
     */
    @RequestMapping("/showOrderList")
    public String showOrderList(Model model) {
        return "";
    }



    /**
     * 根据某个供应商的订单列表
     *
     * @param userInfo       当前用户信息(必须)
     * @param pageable       分页信息(必须)
     * @param orderId        订单ID
     * @param name           路线名称
     * @param buyer          购买人
     * @param tel            购买人电话
     * @param payTypeEnum    付款状态
     * @param orderDate      下单时间
     * @param endOrderDate   结束下单时间
     * @param payDate        支付时间
     * @param endPayDate     结束支付时间
     * @param touristDate    出行时间
     * @param endTouristDate 结束出行时间
     * @param orderStateEnum 结算状态
     * @return
     * @throws IOException
     */
    @RequestMapping("/orderList")
//    @ResponseBody AuthenticationPrincipal需要当前供应商
    public PageAndSelection<TouristOrder> orderList(@AuthenticationPrincipal SystemUser userInfo
            , Pageable pageable
            , String orderId, String name, String buyer, String tel, PayTypeEnum payTypeEnum, LocalDateTime orderDate
            , LocalDateTime endOrderDate, LocalDateTime payDate, LocalDateTime endPayDate, LocalDateTime touristDate
            ,LocalDateTime endTouristDate, OrderStateEnum orderStateEnum) throws IOException {

        TouristSupplier supplier=(TouristSupplier) userInfo;

        Page<TouristOrder> orders = touristOrderService.touristOrders(supplier, null, orderId, name, buyer, tel,
                payTypeEnum
                , orderDate, endOrderDate, payDate, endPayDate
                , touristDate, orderStateEnum, pageable);

        List<Selection<TouristOrder, ?>> selections = new ArrayList<>();


        //出行时间特殊处理
        Selection<TouristOrder, LocalDateTime> touristDateSelection = new Selection<TouristOrder, LocalDateTime>() {
            @Override
            public String getName() {
                return "touristDate";
            }

            @Override
            public LocalDateTime apply(TouristOrder order) {
                Traveler traveler = travelerRepository.findByOrder_Id(order.getId()).get(0);
                return traveler.getRoute().getFromDate();
            }
        };

        //人数处理
        Selection<TouristOrder, Long> peopleNumberSelection = new Selection<TouristOrder, Long>() {
            @Override
            public String getName() {
                return "peopleNumber";
            }

            @Override
            public Long apply(TouristOrder order) {
                return travelerRepository.countByOrder_Id(order.getId());
            }
        };

        Selection<TouristOrder,Long> touristRouteIdSelection=new Selection<TouristOrder, Long>() {
            @Override
            public String getName() {
                return "touristRouteId";
            }

            @Override
            public Long apply(TouristOrder order) {
                return travelerRepository.findByOrder_Id(order.getId()).get(0).getId();
            }
        };

        selections.add(touristDateSelection);
        selections.add(peopleNumberSelection);
        selections.addAll(TouristOrder.htmlSelections);
        return new PageAndSelection<>(orders, selections);
    }

    /**
     * 获取所有与他相同的线路订单的出行时间以及人数
     *
     * @param id 线路订单ID
     * @return
     * @throws IOException
     */
    @RequestMapping("/getAllOrderTouristDate")
    @ResponseBody
    public ModelMap getAllOrderTouristDate(@RequestParam Long id) throws IOException {

        TouristOrder order = touristOrderRepository.findOne(id);

        List<TouristRoute> routes = touristRouteRepository.findByGood(order.getTouristGood());

        List<TouristRouteModel> touristRouteModels = new ArrayList<>();

        for (TouristRoute route : routes) {
            //排除自己的线路订单
            if (route.getId().equals(id)) {
                continue;
            }
            TouristRouteModel model = new TouristRouteModel();
            model.setId(route.getId());
            model.setFromDate(route.getFromDate());
            model.setRemainPeople(touristRouteService.getRemainPeopleByRoute(route));
            touristRouteModels.add(model);
        }

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("data", touristRouteModels);
        return modelMap;
    }



    /**
     * 返回某个订单的视图信息
     *
     * @param id 线路订单ID
     * @return 线路订单视图
     * @throws IOException
     */
    @RequestMapping("/showOrder")
    public String showOrder(@AuthenticationPrincipal SystemUser userInfo
            ,@RequestParam Long id, Model model) throws IOException {

        TouristOrder order = touristOrderRepository.findOne(id);

        model.addAttribute("order", order);

        List<Traveler> travelers = travelerRepository.findByOrder_Id(id);

        model.addAttribute("route", travelers.get(0).getRoute());

        List<OrderStateEnum> orderStates=touristOrderService.getModifyState(userInfo,order);

        model.addAttribute("orderStates",orderStates);

        model.addAttribute("travelers", travelers);

        return "";

    }

//    /**
//     * 显示某供应商的线路商品信息
//     *
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/TouristGoodList")
//    public PageAndSelection<TouristGood> touristGoodList(@RequestParam Long supplierId, String touristName
//            , String supplierName, TouristType touristType, ActivityType activityType
//            , TouristCheckStateEnum touristCheckState, Pageable pageable) throws Exception {
//        TouristSupplier supplier = touristSupplierRepository.getOne(supplierId);
//        Page<TouristGood> goods = touristGoodService.touristGoodList(supplier, touristName, supplierName,
//                touristType,  activityType, touristCheckState, pageable);
//
//        return new PageAndSelection<>(goods, TouristGood.selections);
//    }


    /**
     * 显示线路商品
     *
     * @param id    商品ID
     * @param model 返回的model
     * @return
     * @throws IOException
     */
    @RequestMapping("/showTouristGood")
    public String showTouristGood(@RequestParam Long id, Model model) throws IOException {
        TouristGood touristGood = touristGoodRepository.findOne(id);
        List<TouristRoute> routes = touristRouteRepository.findByGood(touristGood);

        List<TouristType> touristTypes=touristTypeRepository.findAll();

        List<ActivityType> activityTypes=activityTypeRepository.findAll();

        TouristCheckStateEnum[] checkStates=TouristCheckStateEnum.values();


        List<TouristRouteModel> touristRouteModels=new ArrayList<>();
        routes.forEach(route->{
            TouristRouteModel routeModel=new TouristRouteModel();
            routeModel.setId(route.getId());
            routeModel.setFromDate(route.getFromDate());
            routeModel.setSold(touristRouteService.judgeRouteIsSold(route));
            touristRouteModels.add(routeModel);
        });

        model.addAttribute("touristTypes",touristTypes);
        model.addAttribute("activityTypes",activityTypes);
        model.addAttribute("checkStates",checkStates);

        model.addAttribute("routes", touristRouteModels);

        model.addAttribute("good", touristGood);
        return "manage/supplier/goodsDetails";
    }



    /**
     * 保存线路商品信息
     * @param id                    线路商品ID
     * @param touristName           线路名称
     * @param activityTypeId        活动类型ID
     * @param touristTypeId         线路类型ID
     * @param touristFeatures       线路特色
     * @param destination           目的地
     * @param placeOfDeparture      出发地
     * @param travelledAddress      途经地
     * @param price                 单价
     * @param childrenDiscount      儿童折扣
     * @param rebate                返利比例
     * @param receptionPerson       地接人
     * @param receptionTelephone    地接人电话
     * @param eventDetails          活动详情
     * @param beCareful             注意事项
     * @param touristImgUri         商品图片
     * @param touristRoutes         出行线路列表
     * @param maxPeople             最大人数
     * @return
     * @throws IOException
     */
    @RequestMapping("/saveTouristGood")
    @ResponseBody
    public void saveTouristGood(Long id,String touristName,Long activityTypeId,Long touristTypeId
            ,String touristFeatures,Address destination,Address placeOfDeparture,Address travelledAddress
            ,BigDecimal price,BigDecimal childrenDiscount,BigDecimal rebate,String receptionPerson
            ,String receptionTelephone,String eventDetails,String beCareful,String touristImgUri
            ,Integer maxPeople,TouristRoute[] touristRoutes)
            throws IOException{

        ActivityType activityType=activityTypeRepository.getOne(activityTypeId);
        TouristType touristType=touristTypeRepository.getOne(touristTypeId);
        //保存商品
        TouristGood good = touristGoodService.saveTouristGood(id, touristName, activityType, touristType, touristFeatures
                ,destination,placeOfDeparture,travelledAddress,price,childrenDiscount,rebate
                ,receptionPerson,receptionTelephone,eventDetails,beCareful,touristImgUri,maxPeople);

        //保存所有线路
        for (TouristRoute t:touristRoutes){
            touristRouteService.saveTouristRoute(t.getId(), t.getRouteNo(), good, t.getFromDate(), t.getToDate(),
                    maxPeople);
        }
    }


    /**
     * 结算列表显示 todo 目前需求还不确定
     *
     * @param supplierId       供应商ID
     * @param platformChecking
     * @param createTime
     * @param pageable
     * @return
     * @throws IOException
     */
    public PageAndSelection<SettlementSheet> settlementSheetList(Long supplierId
            , SettlementStateEnum platformChecking, LocalDate createTime, Pageable pageable) throws IOException {
        return null;
    }




    /**
     * 销售统计页面
     *
     * @param userInfo 当前用户信息
     * @param model    返回的数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/showSaleStatistics")
    public String showSaleStatistics(@AuthenticationPrincipal SystemUser userInfo, Model model) throws IOException {
        TouristSupplier supplier=(TouristSupplier)userInfo;
        model.addAttribute("moneyTotal", touristOrderService.countMoneyTotal(supplier.getId()));
        model.addAttribute("commissionTotal", touristOrderService.countCommissionTotal(supplier.getId()));
        model.addAttribute("refundTotal", touristOrderService.countRefundTotal(supplier.getId()));
        model.addAttribute("orderTotal", touristOrderService.countOrderTotal(supplier.getId()));
        return "/view/manage/supplier/salesStatistics";

    }

    /**
     * 销售统计里面订单详情列表
     * @param userInfo      当前用户
     * @param pageable      分页信息
     * @param orderDate     下单时间
     * @param endOrderDate  结束下单时间
     * @param payDate       支付时间
     * @param endPayDate    结束支付时间
     * @return              订单列表
     * @throws IOException
     */
    @RequestMapping("/orderDetailsList")
    @ResponseBody
    public PageAndSelection<TouristOrder> orderDetailsList(@AuthenticationPrincipal SystemUser userInfo
            , @RequestParam Pageable pageable, LocalDateTime orderDate, LocalDateTime endOrderDate
            , LocalDateTime payDate, LocalDateTime endPayDate) throws IOException {
        TouristSupplier supplier =(TouristSupplier)userInfo;

        Page<TouristOrder> orders = touristOrderService.touristOrders(supplier, null, null, null, null, null, null,
                orderDate, endOrderDate, payDate, endPayDate, null, null, pageable
        );

        List<Selection<TouristOrder, ?>> selections = new ArrayList<>();

        //人数处理
        Selection<TouristOrder, Long> peopleNumberSelection = new Selection<TouristOrder, Long>() {
            @Override
            public String getName() {
                return "peopleNumber";
            }

            @Override
            public Long apply(TouristOrder order) {
                return travelerRepository.countByOrder_Id(order.getId());
            }
        };
        //佣金处理
        Selection<TouristOrder, BigDecimal> commissionSelection = new Selection<TouristOrder, BigDecimal>() {
            @Override
            public String getName() {
                return "commission";
            }

            @Override
            public BigDecimal apply(TouristOrder order) {
                BigDecimal commission = order.getTouristGood().getRebate().multiply(
                        order.getTouristGood().getPrice());
                return commission;
            }
        };
        selections.add(peopleNumberSelection);
        selections.add(commissionSelection);
        selections.addAll(TouristOrder.htmlSelections);
        return new PageAndSelection<>(orders, selections);
    }

    /**
     * 商品销售排行
     * @param userInfo  当前用户
     * @return
     * @throws IOException
     */
    @RequestMapping("/goodsSalesRanking")
    @ResponseBody
    public PageAndSelection<TouristGood> goodsSalesRanking(@AuthenticationPrincipal SystemUser userInfo,Pageable pageable)
            throws IOException{
        TouristSupplier supplier =(TouristSupplier)userInfo;
        Page<TouristGood> touristGoods=touristGoodService.salesRanking(supplier.getId(),pageable);

        //购买次数处理
        Selection<TouristGood,Long> buyTotal=new Selection<TouristGood, Long>() {
            @Override
            public String getName() {
                return "buyTotal";
            }

            @Override
            public Long apply(TouristGood good) {
                return touristOrderRepository.countByTouristGood(good);
            }
        };

        //总金额处理
        Selection<TouristGood,BigDecimal> moneyTotal=new Selection<TouristGood, BigDecimal>() {
            @Override
            public String getName() {
                return "moneyTotal";
            }

            @Override
            public BigDecimal apply(TouristGood good) {
                return touristOrderRepository.countOrderMoney(good);
            }
        };

        //总佣金处理
        Selection<TouristGood,BigDecimal> commissionTotal=new Selection<TouristGood, BigDecimal>() {
            @Override
            public String getName() {
                return "commissionTotal";
            }

            @Override
            public BigDecimal apply(TouristGood good) {
                return touristOrderRepository.countOrderMoney(good).multiply(good.getRebate());
            }
        };

        List<Selection<TouristGood, ?>> selections = new ArrayList<>();

        selections.add(buyTotal);
        selections.add(moneyTotal);
        selections.add(commissionTotal);
        selections.addAll(TouristGood.selections);

        return new PageAndSelection<>(touristGoods,selections);

    }




    /**
     * 修改供应商信息
     * @param id                    供应商ID
     * @param address               地址
     * @param contacts              联系人
     * @param contactNumber         联系电话
     * @param businessLicenseUri    营业执照uri
     * @param remarks               备注
     * @return                      视图
     * @throws Exception
     */
    @RequestMapping("/modifySupplierInfo")
    @ResponseBody
    public void modifySupplierInfo(Long id,Address address,String contacts,String contactNumber
            ,String businessLicenseUri,String remarks)throws Exception{

        touristSupplierService.modifySupplier(id,address,contacts,contactNumber,businessLicenseUri,remarks);

    }


    /**
     * 显示供应商的收款账户信息
     * @param userInfo      当前用户
     * @return              视图
     * @throws IOException
     */
    @RequestMapping("/showCollectionAccount")
    public String showCollectionAccount(@AuthenticationPrincipal SystemUser userInfo,Model model) throws IOException{
        TouristSupplier supplier =(TouristSupplier)userInfo;
        CollectionAccount collectionAccount=collectionAccountRepository.findOne(supplier.getId());
        model.addAttribute("data",collectionAccount);
        return "";
    }

    /**
     * 保存收款账户(包括新增，和修改)
     * @param id                收款账户ID(null:新增，有数据：修改)
     * @param accountType       账户类型
     * @param IDCard            身份证号
     * @param aliPayName        支付宝姓名
     * @param aliPayAccount     支付宝账号
     * @param accountName       银行卡户名
     * @param bank              开户银行
     * @param bankBranch        卡户银行
     * @param bankCard          银行卡号
     */
    @RequestMapping(value = "/saveCollectionAccount",method = RequestMethod.POST)
    @ResponseBody
    public void saveCollectionAccount(Long id, CollectionAccountTypeEnum accountType,String IDCard,String aliPayName
            ,String aliPayAccount,String accountName,String bank,String bankBranch,String bankCard){
        collectionAccountService.saveCollectionAccount(id,accountType,IDCard,aliPayName,aliPayAccount
                ,accountName,bank,bankBranch,bankCard);
    }


}
