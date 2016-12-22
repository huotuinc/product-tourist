package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.currentUser.CurrentUserInfo;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.model.PageAndSelection;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.*;
import com.huotu.tourist.service.TouristGoodService;
import com.huotu.tourist.service.TouristOrderService;
import com.huotu.tourist.service.TouristRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Controller("/supplier")
public class SupplierManageController {

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
     * @param orderStateEnum 结算状态
     * @return
     * @throws IOException
     */
    @RequestMapping("/orderList")
//    @ResponseBody AuthenticationPrincipal需要当前供应商
    public PageAndSelection<TouristOrder> orderList(@AuthenticationPrincipal CurrentUserInfo userInfo
            , @RequestParam Pageable pageable
            , String orderId, String name, String buyer, String tel, PayTypeEnum payTypeEnum, LocalDateTime orderDate
            , LocalDateTime endOrderDate, LocalDateTime payDate, LocalDateTime endPayDate, LocalDateTime touristDate
            , OrderStateEnum orderStateEnum) throws IOException {

        TouristSupplier supplier = touristSupplierRepository.findOne(userInfo.getSupplierId());

        Page<TouristOrder> orders = touristOrderService.supplierOrders(supplier, pageable,
                orderId, name, buyer, tel, payTypeEnum, orderDate.toLocalDate(), endOrderDate.toLocalDate()
                , payDate.toLocalDate(), endPayDate.toLocalDate(), touristDate.toLocalDate(), orderStateEnum);

        List<Selection<TouristOrder, ?>> selections = new ArrayList<>();


        //出行时间特殊处理
        Selection<TouristOrder, LocalDate> touristDateSelection = new Selection<TouristOrder, LocalDate>() {
            @Override
            public String getName() {
                return "touristDate";
            }

            @Override
            public LocalDate apply(TouristOrder order) {
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
        selections.add(touristDateSelection);
        selections.add(peopleNumberSelection);
        selections.addAll(TouristOrder.htmlSelections);
        return new PageAndSelection<>(orders, selections);

//        List<Selection<TouristOrder, ?>> selectionList;
//        PageAndSelection<TouristOrder> touristOrderModels =new PageAndSelection<>(orders,)

//        List<TouristOrderModel> touristOrderModels = touristOrderService.touristOrderModelConver(orders.getContent());

//        orders.forEach(order->{
//            TouristOrderModel model=new TouristOrderModel();
//            model.setId(order.getId());
//            model.setBuyerName(order.getTouristBuyer().getBuyerName());
//            model.setTouristName(order.getTouristGood().getTouristName());
//            model.setOrderMoney(order.getOrderMoney().doubleValue());
//            model.setOrderState(order.getOrderState().getDescription());
//            model.setTouristDate("");//todo 计算
//            model.setPeopleNumber(0);//todo 计算
//            model.setRemarks(order.getRemarks());
//            touristOrderModels.add(model);
//        });

//        ModelMap modelMap=new ModelMap();
//        modelMap.addAttribute("rows",touristOrderModels);
//        modelMap.addAttribute("total",orders.getTotalElements());
//        return modelMap;
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
    public String showOrder(@RequestParam Long id, Model model) throws IOException {

        TouristOrder order = touristOrderRepository.findOne(id);

        model.addAttribute("order", order);

        List<Traveler> travelers = travelerRepository.findByOrder_Id(id);

        model.addAttribute("route", travelers.get(0).getRoute());

        model.addAttribute("travelers", travelers);

        return "";

    }

    /**
     * 显示某供应商的线路商品信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/TouristGoodList")
    public PageAndSelection<TouristGood> touristGoodList(@RequestParam Long supplierId, String touristName
            , String supplierName, TouristType touristType, ActivityType activityType
            , TouristCheckStateEnum touristCheckState, Pageable pageable) throws Exception {
        Page<TouristGood> goods = touristGoodService.touristGoodList(supplierId, touristName, supplierName,
                touristType,  activityType, touristCheckState, pageable);

        return new PageAndSelection<>(goods, TouristGood.selections);
    }


    /**
     * 保存线路商品信息
     * @return
     * @throws IOException
     */
    @RequestMapping("/saveTouristGood")
    public String saveTouristGood() throws IOException{


        return "";
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
    public String showSaleStatistics(@AuthenticationPrincipal CurrentUserInfo userInfo, Model model) throws IOException {
        model.addAttribute("moneyTotal", touristOrderService.countMoneyTotal(userInfo.getSupplierId()));
        model.addAttribute("commissionTotal", touristOrderService.countCommissionTotal(userInfo.getSupplierId()));
        model.addAttribute("refundTotal", touristOrderService.countRefundTotal(userInfo.getSupplierId()));
        model.addAttribute("orderTotal", touristOrderService.countOrderTotal(userInfo.getSupplierId()));
        return "";

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
    public PageAndSelection<TouristOrder> orderDetailsList(@AuthenticationPrincipal CurrentUserInfo userInfo
            , @RequestParam Pageable pageable, LocalDateTime orderDate, LocalDateTime endOrderDate
            , LocalDateTime payDate, LocalDateTime endPayDate) throws IOException {

        TouristSupplier supplier = touristSupplierRepository.findOne(userInfo.getSupplierId());

        Page<TouristOrder> orders = touristOrderService.supplierOrders(supplier, pageable,
                null, null, null, null, null, orderDate.toLocalDate(), endOrderDate.toLocalDate()
                , payDate.toLocalDate(), endPayDate.toLocalDate(), null, null);

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






}
