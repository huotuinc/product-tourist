/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.distributionPlatform;

import com.huotu.tourist.common.*;
import com.huotu.tourist.entity.*;
import com.huotu.tourist.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 2016/12/17.
 */
@Controller
@RequestMapping("/distributionPlatform/")
public class DistributionPlatformController {
    public static final String ROWS = "rows";
    public static final String TOTAL = "total";
    private static final Log log = LogFactory.getLog(DistributionPlatformController.class);
    @Autowired
    TouristSupplierService touristSupplierService;

    @Autowired
    TouristBuyerService touristBuyerService;

    @Autowired
    PurchaserPaymentRecordService purchaserPaymentRecordService;

    @Autowired
    PurchaserProductSettingService purchaserProductSettingService;

    @Autowired
    TouristGoodService touristGoodService;

    @Autowired
    ActivityTypeService activityTypeService;

    @Autowired
    TouristTypeService touristTypeService;

    @Autowired
    TouristOrderService touristOrderService;

    @Autowired
    SettlementSheetService settlementSheetService;

    @Autowired
    PresentRecordService presentRecordService;

    /**
     * 跳转到供应商列表页面
     * @return
     */
    @RequestMapping(value = "toSupplierList", method = RequestMethod.GET)
    public String toSupplierList() {
        //todo
        return "";
    }

    /**
     * 供应商列表
     *
     * @param name     供应商名称
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "supplierList", method = RequestMethod.GET)
    public ResponseEntity supplierList(String name, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<TouristSupplier> page = touristSupplierService.supplierList(name, new PageRequest(pageNo, pageSize));
        Map map = new HashMap();
        return null;
    }

    /**
     * 跳转到采购商列表页面
     * @return
     */
    @RequestMapping(value = "toBuyerList", method = RequestMethod.GET)
    public String toBuyerList() {
        //todo
        return "";
    }

    /**
     * 采购商列表
     *
     * @param buyerName       采购商名称
     * @param buyerDirector   采购负责人
     * @param telPhone        采购负责人电话
     * @param buyerCheckState 采购状态
     * @param pageSize        每页显示条数
     * @param pageNo          页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "buyerList", method = RequestMethod.GET)
    public ResponseEntity buyerList(String buyerName, String buyerDirector, String telPhone, BuyerCheckStateEnum buyerCheckState
            , int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<TouristBuyer> page = touristBuyerService.buyerList(buyerName, buyerDirector, telPhone, buyerCheckState
                , new PageRequest(pageNo, pageSize));
        return null;
    }


    /**
     * 跳转到 采购商支付记录列表页面
     * @return
     */
    @RequestMapping(value = "toPurchaserPaymentRecordList", method = RequestMethod.GET)
    public String toPurchaserPaymentRecordList() {
        //todo
        return "";
    }

    /**
     * 采购商支付记录列表
     *
     * @param startPayDate  支付的开始时间
     * @param endPayDate    支付结束时间
     * @param buyerName     采购商名称
     * @param buyerDirector 负责人
     * @param telPhone      负责人电话
     * @param pageSize      每页显示条数
     * @param pageNo        页码
     * @param request
     * @param model         @return
     */
    @RequestMapping(value = "purchaserPaymentRecordList", method = RequestMethod.GET)
    public ResponseEntity purchaserPaymentRecordList(String startPayDate, String endPayDate, String buyerName,
                                                     String buyerDirector, String telPhone, int pageSize, int pageNo,
                                                     HttpServletRequest request, Model model) {
        Page<PurchaserPaymentRecord> page = purchaserPaymentRecordService.purchaserPaymentRecordList(startPayDate, endPayDate
                , buyerName, buyerDirector, telPhone, new PageRequest(pageNo, pageSize));

        return null;
    }

    /**
     * 导出采购商支付记录列表
     *
     * @param startPayDate  开始支付时间
     * @param endPayDate    结束支付时间
     * @param buyerName     采购商名称
     * @param buyerDirector 采购商负责人
     * @param telPhone      采购商负责人电话
     * @param pageSize      每页显示条数
     * @param pageNo        页码
     * @param request
     * @param model
     */
    @RequestMapping(value = "exportPurchaserPaymentRecord", method = RequestMethod.GET)
    public void exportPurchaserPaymentRecord(String startPayDate, String endPayDate, String buyerName,
                                             String buyerDirector, String telPhone
            , int pageSize, int pageNo, HttpServletRequest request, Model model) {
        List<PurchaserPaymentRecord> page = purchaserPaymentRecordService.purchaserPaymentRecordList(startPayDate, endPayDate
                , buyerName, buyerDirector, telPhone);

    }

    /**
     * 跳转到采购商产品设置列表页面
     * @return
     */
    @RequestMapping(value = "toPurchaserProductSettingList", method = RequestMethod.GET)
    public String toPurchaserProductSettingList() {
        //todo
        return "";
    }

    /**
     * 采购商产品设置列表
     *
     * @param name     产品设置名称
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "purchaserProductSettingList", method = RequestMethod.GET)
    public ResponseEntity purchaserProductSettingList(String name, int pageSize, int pageNo
            , HttpServletRequest request, Model model) {
        Page<PurchaserProductSetting> page = purchaserProductSettingService.purchaserProductSettingList(name
                , new PageRequest(pageNo, pageSize));

        return null;
    }

    /**
     * 跳转到线路列表页面
     * @return
     */
    @RequestMapping(value = "toTouristGoodList", method = RequestMethod.GET)
    public String toTouristGoodList() {
        //todo
        return "";
    }

    /**
     * 线路列表
     *
     * @param touristName       线路名称
     * @param supplierName      供应商名称
     * @param touristTypeId     线路类型ID
     * @param activityTypeId    活动ID
     * @param touristCheckState 线路审核状态
     * @param pageSize          每页显示条数
     * @param pageNo            页码
     * @param request
     * @param model             @return
     */
    @RequestMapping(value = "touristGoodList", method = RequestMethod.GET)
    public ResponseEntity touristGoodList(String touristName, String supplierName, Long touristTypeId, Long activityTypeId
            , TouristCheckStateEnum touristCheckState, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        ActivityType activityType = null;
        TouristType touristType = null;
        if (touristTypeId != null) {
            touristType = touristTypeService.getOne(touristTypeId);
        }
        if (activityTypeId != null) {
            activityType = activityTypeService.getOne(touristTypeId);
        }
        Page<TouristGood> page = touristGoodService.touristGoodList(touristName, supplierName, touristType, activityType
                , touristCheckState, new PageRequest(pageNo, pageSize));

        return null;
    }

    /**
     * 推荐线路列表
     *
     * @param touristName       线路名称
     * @param supplierName      供应商名称
     * @param touristTypeId     线路类型ID
     * @param activityTypeId    活动ID
     * @param touristCheckState 线路审核状态
     * @param pageSize          每页显示条数
     * @param pageNo            页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "recommendTouristGoodList", method = RequestMethod.GET)
    public ResponseEntity recommendTouristGoodList(String touristName, String supplierName, Long touristTypeId, Long activityTypeId
            , TouristCheckStateEnum touristCheckState, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        ActivityType activityType = null;
        TouristType touristType = null;
        if (touristTypeId != null) {
            touristType = touristTypeService.getOne(touristTypeId);
        }
        if (activityTypeId != null) {
            activityType = activityTypeService.getOne(touristTypeId);
        }
        Page<TouristGood> page = touristGoodService.recommendTouristGoodList(touristName, supplierName, touristType, activityType
                , touristCheckState, true, new PageRequest(pageNo, pageSize));

        return null;
    }

    /**
     * 跳转到活动类型列表页面
     * @return
     */
    @RequestMapping(value = "toActivityTypeList", method = RequestMethod.GET)
    public String toActivityTypeList() {
        //todo
        return "";
    }

    /**
     * 活动类型列表
     *
     * @param name     活动名称
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "activityTypeList", method = RequestMethod.GET)
    public ResponseEntity activityTypeList(String name, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<ActivityType> page = activityTypeService.activityTypeList(name, new PageRequest(pageNo, pageSize));
        return null;
    }

    /**
     * 跳转到线路类型列表页面
     * @return
     */
    @RequestMapping(value = "toTouristTypeList", method = RequestMethod.GET)
    public String toTouristTypeList() {
        //todo
        return "";
    }

    /**
     * 线路类型列表
     *
     * @param name     线路名称
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "touristTypeList", method = RequestMethod.GET)
    public ResponseEntity touristTypeList(String name, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<TouristType> page = touristTypeService.touristTypeList(name, new PageRequest(pageNo, pageSize));
        return null;
    }

    /**
     * 跳转到订单列表页面
     *
     * @return
     */
    @RequestMapping(value = "toSupplierOrders", method = RequestMethod.GET)
    public String toSupplierOrders() {
        //todo
        return "";
    }

    /**
     * 订单列表
     *
     * @param orderNo      订单号
     * @param touristName  线路名称
     * @param buyerName    采购商名称
     * @param tel          采购商电话
     * @param orderState   订单状态
     * @param orderDate    开始订单创建时间
     * @param endOrderDate 结束订单创建时间
     * @param payDate      开始支付时间
     * @param endPayDate   结束支付时间
     * @param payType      支付类型
     * @param touristDate  线路开始时间
     * @param pageSize     每页显示条数
     * @param pageNo       页码
     * @param request
     * @param model        @return
     */
    @RequestMapping(value = "supplierOrders", method = RequestMethod.GET)
    public ResponseEntity supplierOrders(String orderNo, String touristName, String buyerName, String tel,
                                         PayTypeEnum payType, LocalDate orderDate, LocalDate endOrderDate, LocalDate payDate
            , LocalDate endPayDate, LocalDate touristDate, OrderStateEnum orderState
            , int pageSize, int pageNo, HttpServletRequest request, Model model) throws IOException {
        Page<TouristOrder> page = touristOrderService.supplierOrders(new PageRequest(pageNo, pageSize), orderNo, touristName, buyerName, tel, payType,
                orderDate, endOrderDate, payDate, endPayDate, touristDate, orderState);
        return null;
    }

    /**
     * 跳转到结算单列表页面
     *
     * @return
     */
    @RequestMapping(value = "toSettlementSheetList", method = RequestMethod.GET)
    public String toSettlementSheetList() {
        //todo
        return "";
    }


    /**
     * 结算单列表
     *
     * @param supplierName     供应商名称
     * @param platformChecking 结算单审核状态
     * @param createTime       结算单创建时间
     * @param pageSize         每页显示条数
     * @param pageNo           页码
     * @param request
     * @param model            @return
     */
    @RequestMapping(value = "settlementSheetList", method = RequestMethod.GET)
    public ResponseEntity settlementSheetList(String supplierName, SettlementStateEnum platformChecking, LocalDate createTime,
                                              int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<SettlementSheet> page = settlementSheetService.settlementSheetList(supplierName, platformChecking, createTime
                , new PageRequest(pageNo, pageSize));

        return null;
    }

    /**
     * 跳转到提现单列表页面
     *
     * @return
     */
    @RequestMapping(value = "toPresentRecordList", method = RequestMethod.GET)
    public String toPresentRecordList() {
        //todo
        return "";
    }

    /**
     * 提现单列表
     *
     * @param supplierName 供应商名称
     * @param presentState 提现状态
     * @param createTime   提现单创建时间
     * @param pageSize     每页显示条数
     * @param pageNo       页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "presentRecordList", method = RequestMethod.GET)
    public ResponseEntity presentRecordList(String supplierName, PresentStateEnum presentState, LocalDate createTime,
                                            int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<PresentRecord> page = presentRecordService.presentRecordList(supplierName, presentState, createTime
                , new PageRequest(pageNo, pageSize));
        Map<String, Object> data = new HashMap<>();
        data.put("rows", page.getContent());
        data.put("total", page.getTotalElements());
        return ResponseEntity.ok(data);
    }

    /**-------------------下面新增和修改相关-----------------------*/
    /**
     * 新增供应商
     *
     * @param supplierName
     * @param remarks
     * @param adminAccount
     * @param adminPassword
     * @param businessLicenseUri
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "addTouristSupplier", method = RequestMethod.POST)
    public String addTouristSupplier(String supplierName, String remarks, String adminAccount, String adminPassword,
                                     String businessLicenseUri, HttpServletRequest request, Model model) {
        TouristSupplier touristSupplier = new TouristSupplier();
        touristSupplier.setCreateTime(LocalDateTime.now());
        touristSupplier.setAdminAccount(adminAccount);
        touristSupplier.setAdminPassword(adminPassword);
        touristSupplier.setBusinessLicenseUri(businessLicenseUri);
        touristSupplier.setRemarks(remarks);
        touristSupplier.setSupplierName(supplierName);
        //todo 地址相关
        touristSupplier = touristSupplierService.create(touristSupplier);

        return toSupplierList();
    }



}
