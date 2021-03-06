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
import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.model.PageAndSelection;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.*;
import com.huotu.tourist.service.*;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 供应商逻辑
 * Created by slt on 2016/12/17.
 */
@Controller
@RequestMapping("/supplier")
public class SupplierManageController {

    @Autowired
    ResourceService resourceService;
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

//    @Autowired
//    private SupplierOperatorRepository supplierOperatorRepository;
    @Autowired
    private CollectionAccountService collectionAccountService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private SettlementSheetService settlementSheetService;
    @Autowired
    private PresentRecordService presentRecordService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SupplierOperatorService supplierOperatorService;
    private String viewSupplierPath="/view/manage/supplier/";
    private String viewCommonPath="/view/manage/common/";

    /**
     * 打开供应商后台页面
     *
     * @return
     */
    @RequestMapping("/")
    public String showSupplierMain() {
        return viewSupplierPath+"main.html";
    }

    //=============================================订单列表

    /**
     * 打开订单列表页面
     *
     * @return
     */
    @RequestMapping(value = "/showOrderList")
    @PreAuthorize("hasAnyRole({'ROLE_ORDER','ROLE_SUPPLIER'})")
    public String showOrderList(Model model) {
        return viewCommonPath+"orderList.html";
    }

    /**
     * 获取所有与他相同的线路订单的出行时间以及人数
     *
     * @param orderId 线路订单ID
     * @param routeId 该订单的行程ID
     * @return
     * @throws IOException
     */
    @RequestMapping("/getAllOrderTouristDate")
    @ResponseBody
    public ModelMap getAllOrderTouristDate(@RequestParam Long orderId,@RequestParam Long routeId) throws IOException {

        TouristOrder order = touristOrderRepository.findOne(orderId);

        List<TouristRoute> routes = touristRouteRepository.findByGood(order.getTouristGood());

        List<TouristRouteModel> touristRouteModels = new ArrayList<>();

        for (TouristRoute route : routes) {
            //排除自己的线路订单
            if (route.getId().equals(routeId)) {
                continue;
            }
            TouristRouteModel model = new TouristRouteModel();
            model.setId(route.getId());
            model.setFromDate(LocalDateTimeFormatter.toStr(route.getFromDate()));
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

        model.addAttribute("route", travelers.isEmpty()?new TouristRoute():travelers.get(0).getRoute());
//
//        List<OrderStateEnum> orderStates=touristOrderService.getModifyState(userInfo,order);
//
//        model.addAttribute("orderStates",orderStates);

        model.addAttribute("travelers", travelers);

        return viewCommonPath+"orderDetails.html";

    }


    //=============================================线路商品


    /**
     * 显示线路商品
     *
     * @param id    商品ID
     * @param model 返回的model
     * @return
     * @throws IOException
     */
    @RequestMapping("/showTouristGood")
    @PreAuthorize("hasAnyRole({'ROLE_GOODS','ROLE_SUPPLIER'})")
    public String showTouristGood(Long id, Model model) throws IOException {
        TouristGood touristGood;
        if(id==null){
            touristGood=new TouristGood();
        }else {
           touristGood = touristGoodRepository.findOne(id);
        }
        List<TouristRoute> routes = touristRouteRepository.findByGood(touristGood);

        List<TouristType> touristTypes=touristTypeRepository.findAll();

        List<ActivityType> activityTypes=activityTypeRepository.findAll();

        TouristCheckStateEnum[] checkStates=TouristCheckStateEnum.values();


        List<TouristRouteModel> touristRouteModels=new ArrayList<>();
        routes.forEach(route->{
            TouristRouteModel routeModel=new TouristRouteModel();
            routeModel.setId(route.getId());
            routeModel.setFromDate(LocalDateTimeFormatter.toStr(route.getFromDate()));
            routeModel.setSold(touristRouteService.judgeRouteIsSold(route));
            touristRouteModels.add(routeModel);
        });

        model.addAttribute("touristTypes",touristTypes);
        model.addAttribute("activityTypes",activityTypes);
        model.addAttribute("checkStates",checkStates);

        model.addAttribute("routes", touristRouteModels);

        model.addAttribute("good", touristGood);
        return viewSupplierPath+"goodsDetailsH+.html";
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
     * @param maxPeople             最大人数
     * @param touristRoutes         出行线路列表
     * @param mallGoodsId
     * @return
     * @throws IOException
     */
    @RequestMapping("/saveTouristGood")
    @ResponseBody
    @Transactional
    public void saveTouristGood(@AuthenticationPrincipal SystemUser userInfo
            ,Long id, String touristName, Long activityTypeId, Long touristTypeId
            , String touristFeatures, @RequestParam Address destination, @RequestParam Address placeOfDeparture
            , @RequestParam Address travelledAddress, BigDecimal price, BigDecimal childrenDiscount, BigDecimal rebate
            , String receptionPerson, String receptionTelephone, String eventDetails, String beCareful
            , String touristImgUri, int maxPeople, TouristRoute[] touristRoutes
            , Long mallGoodsId,String[] photos,TouristCheckStateEnum checkState) throws IOException{
        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();
        ActivityType activityType=activityTypeId==null?null:activityTypeRepository.getOne(activityTypeId);
        TouristType touristType=touristTypeId==null?null:touristTypeRepository.getOne(touristTypeId);
        List<String> images=photos==null?null:Arrays.asList(photos);
        //保存商品
        TouristGood good = touristGoodService.saveTouristGood(supplier, id, touristName, activityType, touristType
                ,touristFeatures,destination,placeOfDeparture,travelledAddress,price,childrenDiscount
                ,rebate,receptionPerson,receptionTelephone,eventDetails,beCareful,touristImgUri,maxPeople
                ,mallGoodsId, images, checkState);

        //保存所有线路
        List<TouristRoute> newRoutes=new ArrayList<>();
        for (TouristRoute t:touristRoutes){
            String routeNo=t.getRouteNo()==null? LocalDateTime.now().toString():t.getRouteNo();
            TouristRoute newRoute=touristRouteService.saveTouristRoute(t.getId(), routeNo, good
                    , t.getFromDate(), t.getToDate(), maxPeople);
            newRoutes.add(newRoute);
        }
        good.setTouristRoutes(newRoutes);

    }

    /**
     * 删除行程
     * @param userInfo  当前用户
     * @param id        删除行程的ID
     * @throws IOException
     */
    @RequestMapping(value = "/delRoutes",method = RequestMethod.POST)
    @ResponseBody
    public void delRoutes(@AuthenticationPrincipal SystemUser userInfo,@RequestParam Long id) throws IOException{
        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();
        touristRouteRepository.delete(id);
    }


    //=============================================结算账户

    /**
     * 显示结算页面
     * @param userInfo      当前登录用户(供应商)
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/showSettlement")
    @PreAuthorize("hasAnyRole({'ROLE_SETTLEMENT','ROLE_SUPPLIER'})")
    public String showSettlement(@AuthenticationPrincipal SystemUser userInfo,Model model) throws IOException{
        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();

        BigDecimal balance=settlementSheetService.countBalance(supplier, null);
        BigDecimal Settled=settlementSheetService.countSettled(supplier);
        BigDecimal notSettled=settlementSheetService.countNotSettled(supplier);
        BigDecimal withdrawal = settlementSheetService.countWithdrawal(supplier, null, null, PresentStateEnum.AlreadyPaid);
        model.addAttribute("settled",Settled);
        model.addAttribute("notSettled",notSettled);
        model.addAttribute("withdrawal",withdrawal);
        model.addAttribute("balance",balance);
        return viewSupplierPath+"settlementAccount.html";
    }


    /**
     * 结算列表显示
     *
     * @param pageable
     * @return
     * @throws IOException
     */
    @RequestMapping("/settledList")
    public PageAndSelection<SettlementSheet> settledList(@AuthenticationPrincipal SystemUser userInfo
            ,@RequestParam(required = false)LocalDateTime createDate
            ,@RequestParam(required = false)LocalDateTime endCreateDate
            , Pageable pageable) throws IOException {

        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();
        Page<SettlementSheet> sheets=settlementSheetService.settlementSheetList(supplier,null,null,createDate
                , endCreateDate,pageable);

        return new PageAndSelection<>(sheets,SettlementSheet.selections);
    }

    /**
     * 未结算的订单列表
     *
     * @param pageable
     * @return
     * @throws IOException
     */
    @RequestMapping("/notSettledList")
    public PageAndSelection<SettlementSheet> notSettledList(@AuthenticationPrincipal SystemUser userInfo
            ,@RequestParam(required = false)LocalDateTime createDate
            ,@RequestParam(required = false)LocalDateTime endCreateDate
            , Pageable pageable) throws IOException {

        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();
        Page<SettlementSheet> sheets=settlementSheetService.settlementSheetList(supplier,null,null,createDate
                , endCreateDate,pageable);

        return new PageAndSelection<>(sheets,SettlementSheet.selections);
    }






//    /**
//     * 订单列表
//     * @param userInfo          当前用户
//     * @param createDate        大于的时间
//     * @param endCreateDate     小于的时间
//     * @param pageable          分页
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping("/notSettledList")
//    public PageAndSelection<TouristOrder> notSettledList(@AuthenticationPrincipal SystemUser userInfo
//            ,Boolean Settlement
//            ,@RequestParam(required = false)LocalDateTime createDate
//            ,@RequestParam(required = false)LocalDateTime endCreateDate
//            , Pageable pageable) throws IOException {
//        TouristSupplier supplier=(TouristSupplier)userInfo;
//        Page<TouristOrder> page = touristOrderService.touristOrders(supplier, null, null, null, null
//                , null, null, createDate, endCreateDate, null, null, null, null, OrderStateEnum.Finish
//                , false, pageable);
//        List<Selection<TouristOrder, ?>> selections = new ArrayList<>();
//
//        //人数处理
//        Selection<TouristOrder, Long> peopleNumberSelection = new Selection<TouristOrder, Long>() {
//            @Override
//            public String getName() {
//                return "peopleNumber";
//            }
//
//            @Override
//            public Long apply(TouristOrder order) {
//                return travelerRepository.countByOrder_Id(order.getId());
//            }
//        };
//
//        selections.add(peopleNumberSelection);
//        selections.addAll(TouristOrder.htmlSelections);
//        return new PageAndSelection<>(page, selections);
//    }

    /**
     * 提现列表
     * @param userInfo
     * @param pageable
     * @return
     * @throws IOException
     */
    @RequestMapping("/withdrawalList")
    public PageAndSelection<PresentRecord> withdrawalList(@AuthenticationPrincipal SystemUser userInfo
            ,@RequestParam(required = false)LocalDateTime createDate
            ,@RequestParam(required = false)LocalDateTime endCreateDate
            , Pageable pageable) throws IOException {
        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();
        Page<PresentRecord> records=presentRecordService.presentRecordList(null,supplier,null,createDate,endCreateDate
                ,pageable);

        List<Selection<PresentRecord, ?>> selections = new ArrayList<>();

        //当前余额处理
        Selection<PresentRecord, BigDecimal> currentAccountBalanceSelection = new Selection<PresentRecord, BigDecimal>() {
            @Override
            public BigDecimal apply(PresentRecord presentRecord) {
                try {
                    return settlementSheetService.countBalance(supplier, presentRecord.getCreateTime());
                } catch (IOException e) {
                    return new BigDecimal(0);
                }
            }

            @Override
            public String getName() {
                return "currentAccountBalance";
            }
        };

        selections.add(currentAccountBalanceSelection);
        selections.addAll(PresentRecord.selections);

        return new PageAndSelection<>(records,selections);
//        presentRecordService.presentRecordList()
    }


    /**
     * 创建提现流水
     * @param userInfo      当前用户
     * @param money         提现的金额
     * @throws IOException
     */
    @RequestMapping(value = "/createWithdrawal",method = RequestMethod.POST)
    @ResponseBody
    public ModelMap createWithdrawal(@AuthenticationPrincipal SystemUser userInfo,BigDecimal money) throws IOException{
        ModelMap modelMap=new ModelMap();
        TouristSupplier supplier=(TouristSupplier)userInfo;

        BigDecimal balance=settlementSheetService.countBalance(supplier,null);
        if(money.compareTo(new BigDecimal(0))<1){
            modelMap.addAttribute("data",500);
            modelMap.addAttribute("msg","提现金额不符合");
            return modelMap;
        }
        if(money.compareTo(balance)==1){
            modelMap.addAttribute("data",500);
            modelMap.addAttribute("msg","提现金额超过余额");
            return modelMap;
        }

        PresentRecord presentRecord=new PresentRecord();
        presentRecord.setTouristSupplier(supplier);
        presentRecord.setAmountOfMoney(money);
        presentRecord.setCreateTime(LocalDateTime.now());
        presentRecord.setPresentState(PresentStateEnum.NotChecking);
        String localDateStr= LocalDate.now().toString().replace("-","");
        presentRecord.setRecordNo(localDateStr+System.currentTimeMillis());
        presentRecordService.save(presentRecord);
        modelMap.addAttribute("data",200);
        return modelMap;
    }






    //=============================================销售统计

    /**
     * 销售统计页面
     *
     * @param userInfo 当前用户信息
     * @param model    返回的数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/showSaleStatistics")
    @PreAuthorize("hasAnyRole({'ROLE_STATISTICS','ROLE_SUPPLIER'})")
    public String showSaleStatistics(@AuthenticationPrincipal SystemUser userInfo, Model model) throws IOException {
        TouristSupplier supplier=((TouristSupplier)userInfo).getAuthSupplier();
        model.addAttribute("moneyTotal", touristOrderService.countMoneyPayFinish(supplier.getId()));
        model.addAttribute("commissionTotal", touristOrderService.countCommissionPayFinish(supplier.getId()));
        model.addAttribute("refundTotal", touristOrderService.countRefundTotal(supplier.getId()));
        model.addAttribute("orderTotal", touristOrderService.countOrderTotal(supplier.getId()));
        return viewSupplierPath+"salesStatistics.html";

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
    public PageAndSelection orderDetailsList(@AuthenticationPrincipal SystemUser userInfo
            , Pageable pageable
            , @RequestParam(required = false) LocalDateTime orderDate
            , @RequestParam(required = false) LocalDateTime endOrderDate
            , @RequestParam(required = false) LocalDateTime payDate
            , @RequestParam(required = false) LocalDateTime endPayDate) throws IOException {
        TouristSupplier supplier =((TouristSupplier)userInfo).getAuthSupplier();

        Page<TouristOrder> orders = touristOrderService.touristOrders(supplier, null, null, null, null, null, null,
                orderDate, endOrderDate, payDate, endPayDate, null, null, null, null,
                pageable,null);

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
                        order.getTouristGood().getPrice().multiply(BigDecimal.valueOf(100)));
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
    public PageAndSelection goodsSalesRanking(@AuthenticationPrincipal SystemUser userInfo
            , Pageable pageable
            , @RequestParam(required = false)LocalDateTime orderDate
            , @RequestParam(required = false) LocalDateTime endOrderDate)
            throws IOException{
        TouristSupplier supplier =((TouristSupplier)userInfo).getAuthSupplier();
        Page<TouristGood> touristGoods = touristGoodService.touristGoodList(supplier, null, null, null, null, null, null, pageable
                , null);

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
                List<OrderStateEnum> states = Arrays.asList(OrderStateEnum.Finish, OrderStateEnum.PayFinish,
                        OrderStateEnum.NotFinish, OrderStateEnum.Refunds);
                try {
                    BigDecimal money = touristOrderService.countOrderTotalMoney(null, null, null, null, null, good, null, states);
                    return money.setScale(2, RoundingMode.HALF_UP);
                } catch (IOException e) {
                    return BigDecimal.valueOf(0);
                }
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
                List<OrderStateEnum> states = Arrays.asList(OrderStateEnum.Finish, OrderStateEnum.PayFinish,
                        OrderStateEnum.NotFinish, OrderStateEnum.Refunds);
                try {
                    BigDecimal money = touristOrderService.countOrderTotalcommission(null, null, null, null, null, good, null, states);
                    return money.setScale(2, RoundingMode.HALF_UP);
                } catch (IOException e) {
                    return BigDecimal.valueOf(0);
                }
            }
        };

        List<Selection<TouristGood, ?>> selections = new ArrayList<>();

        selections.add(buyTotal);
        selections.add(moneyTotal);
        selections.add(commissionTotal);
        selections.addAll(TouristGood.getSelections(resourceService));

        return new PageAndSelection<>(touristGoods,selections);

    }

    //=============================================供应商信息和收款账户

    /**
     * 打开供应商信息页面
     * @param userInfo
     * @return
     * @throws IOException
     */
    @RequestMapping("/showSupplierInfo")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public String showSupplierInfo(@AuthenticationPrincipal SystemUser userInfo,Model model) throws IOException{
        TouristSupplier authSupplier=((TouristSupplier)userInfo).getAuthSupplier();
        TouristSupplier supplier=touristSupplierService.getOne(authSupplier.getId());
        model.addAttribute("supplier",supplier);
        return viewSupplierPath+"supplierDetailsH+.html";
    }

    /**
     * 修改供应商信息
     * @param id                    供应商ID
     * @param address               地址
     * @param contacts              联系人
     * @param contactNumber         联系电话
     * @param businessLicenseUri    营业执照uri
     * @param remarks               备注
     * @param supplierName
     * @return                      视图
     * @throws Exception
     */
    @RequestMapping("/modifySupplierInfo")
    @ResponseBody
    public void modifySupplierInfo(@RequestParam Long id, Address address, String detailedAddress
            , String contacts, String contactNumber, String businessLicenseUri, String remarks, String supplierName)throws Exception{
        touristSupplierService.modifySupplier(id,address,contacts,contactNumber,businessLicenseUri
                ,remarks, detailedAddress, supplierName);
    }


    /**
     * 显示供应商的收款账户信息
     * @param userInfo      当前用户
     * @return              视图
     * @throws IOException
     */
    @RequestMapping("/showCollectionAccount")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public String showCollectionAccount(@AuthenticationPrincipal SystemUser userInfo ,Model model) throws IOException{
        TouristSupplier supplier =((TouristSupplier)userInfo).getAuthSupplier();
        CollectionAccount collectionAccount=collectionAccountRepository.findOne(supplier.getId());
        if(collectionAccount==null){
            collectionAccount=new CollectionAccount();
        }
        model.addAttribute("account",collectionAccount);
        return viewSupplierPath+"payeeAccountDetailsH+.html";
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
//    @ResponseBody
    public String saveCollectionAccount(Long id, CollectionAccountTypeEnum accountType,String IDCard,String aliPayName
            ,String aliPayAccount,String accountName,String bank,String bankBranch,String bankCard,Model model){
        CollectionAccount collectionAccount=collectionAccountService.saveCollectionAccount(
                id,accountType,IDCard, aliPayName,aliPayAccount,accountName,bank,bankBranch,bankCard);
        model.addAttribute("account",collectionAccount);
        return viewSupplierPath+"payeeAccountDetailsH+.html";
    }


    //=============================================供应商操作员
    /**
     * 打开供应商管理员列表
     * @return
     */
    @RequestMapping("/showJurisdiction")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public String showJurisdiction(){
        return viewSupplierPath+"jurisdictionList.html";
    }

    /**
     * 返回供应商操作员列表
     * @param pageable      分页信息
     * @return
     * @throws IOException
     */
    @RequestMapping("/getJurisdictionList")
    public PageAndSelection<TouristSupplier> getJurisdictionList(@AuthenticationPrincipal SystemUser userInfo
            , Pageable pageable) throws IOException {
        TouristSupplier supplier=(TouristSupplier)userInfo;

        Page<TouristSupplier> supplierManagers=touristSupplierService.getJurisdictionList(supplier,pageable);
        return new PageAndSelection<>(supplierManagers,TouristSupplier.selections);
    }

    /**
     * 返回某个供应商操作员的详情页
     * @param id        操作员ID
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/getJurisdiction")
    public String getJurisdiction(Long id, Model model) throws IOException{
        TouristSupplier operator;
        if(id==null){
            operator=new TouristSupplier();
        }else {
            operator=touristSupplierService.getOne(id);
        }
        model.addAttribute("operator",operator);
        return viewSupplierPath+"jurisdictionDeails.html";
    }

    /**
     * 显示修改密码的界面
     * @param userInfo  当前登录用户
     * @param model     返回的model
     * @return
     */
    @RequestMapping("/showSupplierResetPassword")
    public String showSupplierResetPassword(@AuthenticationPrincipal SystemUser userInfo,Model model){
        TouristSupplier supplier=(TouristSupplier)userInfo;
        model.addAttribute("id",supplier.getId());
        model.addAttribute("loginName",supplier.getLoginName());
        return viewSupplierPath+"resetSupplierPassword.html";
    }

    /**
     * 修改供应商密码
     * @param userInfo      当前登录用户
     * @param oldPassword   旧密码
     * @param newPassword   新密码
     * @throws IOException
     */
    @RequestMapping(value = "/resetSupplierPassword", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap resetSupplierPassword(@AuthenticationPrincipal SystemUser userInfo,String oldPassword
            ,String newPassword) throws IOException{
        ModelMap modelMap=new ModelMap();
        TouristSupplier supplier=(TouristSupplier)userInfo;
        if(!passwordEncoder.matches(oldPassword,supplier.getPassword())){
            modelMap.addAttribute("data",500);
            modelMap.addAttribute("message","密码错误！");
            return modelMap;
        }
        loginService.updatePassword(supplier,newPassword);
        modelMap.addAttribute("data",200);
        return modelMap;
    }

    /**
     * 保存供应商操作员
     * @param id            操作员ID
     * @param loginName     用户名
     * @param password      密码
     * @param tel           联系电话
     * @param name          姓名
     * @param authorityList 权限列表
     * @throws IOException
     */
    @RequestMapping(value = "/saveSupplierOperatorInfo",method = RequestMethod.POST)
    @ResponseBody
    public void saveSupplierOperatorInfo(@AuthenticationPrincipal SystemUser userInfo,Long id
            ,@RequestParam String loginName,@RequestParam String password,String tel,String name
            ,@RequestParam String[] authorityList) throws IOException{
        TouristSupplier supplier=(TouristSupplier)userInfo;
        supplierOperatorService.saveOperator(id,supplier,loginName,password,tel,name,authorityList);
    }

    @RequestMapping(value = "/delSupplierOperator",method = RequestMethod.POST)
    @ResponseBody
    public void delSupplierOperator(@RequestParam Long id) throws IOException{
        loginService.delLogin(id);
    }

}
