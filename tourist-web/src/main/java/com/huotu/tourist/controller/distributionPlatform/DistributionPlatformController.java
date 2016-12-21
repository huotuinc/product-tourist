/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.distributionPlatform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.controller.BaseController;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.PurchaserProductSetting;
import com.huotu.tourist.entity.SettlementSheet;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import com.huotu.tourist.model.PageAndSelection;
import com.huotu.tourist.service.ActivityTypeService;
import com.huotu.tourist.service.PresentRecordService;
import com.huotu.tourist.service.PurchaserPaymentRecordService;
import com.huotu.tourist.service.PurchaserProductSettingService;
import com.huotu.tourist.service.SettlementSheetService;
import com.huotu.tourist.service.TouristBuyerService;
import com.huotu.tourist.service.TouristGoodService;
import com.huotu.tourist.service.TouristOrderService;
import com.huotu.tourist.service.TouristSupplierService;
import com.huotu.tourist.service.TouristTypeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
public class DistributionPlatformController extends BaseController {
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
     *
     * @return
     */
    @RequestMapping(value = "toSupplierList", method = RequestMethod.GET)
    public String toSupplierList(HttpServletRequest request, Model model) {
        model.addAttribute("", 1000);
        //todo
        return "index.html";
    }

    /**
     * 供应商列表
     *
     * @param name     供应商名称
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @return
     */
    @RequestMapping(value = "supplierList", method = RequestMethod.GET)
    public PageAndSelection supplierList(String name, int pageSize, int pageNo, HttpServletRequest request) {
        Page<TouristSupplier> page = touristSupplierService.supplierList(name, new PageRequest(pageNo, pageSize));
        return new PageAndSelection<>(page, TouristSupplier.selections);
    }

    /**
     * 跳转到采购商列表页面
     *
     * @return
     */
    @RequestMapping(value = "toBuyerList", method = RequestMethod.GET)
    public String toBuyerList(HttpServletRequest request, Model model) {
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
     * @return
     */
    @RequestMapping(value = "buyerList", method = RequestMethod.GET)
    public PageAndSelection<TouristBuyer> buyerList(String buyerName, String buyerDirector, String telPhone, BuyerCheckStateEnum buyerCheckState
            , int pageSize, int pageNo, HttpServletRequest request) {
        Page<TouristBuyer> page = touristBuyerService.buyerList(buyerName, buyerDirector, telPhone, buyerCheckState
                , new PageRequest(pageNo, pageSize));
        return new PageAndSelection<>(page, TouristBuyer.selections);
    }


    /**
     * 跳转到 采购商支付记录列表页面
     *
     * @return
     */
    @RequestMapping(value = "toPurchaserPaymentRecordList", method = RequestMethod.GET)
    public String toPurchaserPaymentRecordList(HttpServletRequest request, Model model) {
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
     */
    @RequestMapping(value = "purchaserPaymentRecordList", method = RequestMethod.GET)
    public PageAndSelection<PurchaserPaymentRecord> purchaserPaymentRecordList(String startPayDate, String endPayDate
            , String buyerName, String buyerDirector, String telPhone, int pageSize, int pageNo,
                                                                               HttpServletRequest request) {
        Page<PurchaserPaymentRecord> page = purchaserPaymentRecordService.purchaserPaymentRecordList(startPayDate, endPayDate
                , buyerName, buyerDirector, telPhone, new PageRequest(pageNo, pageSize));
        return new PageAndSelection<>(page, PurchaserPaymentRecord.selections);
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
    public ResponseEntity exportPurchaserPaymentRecord(String startPayDate, String endPayDate, String buyerName,
                                                       String buyerDirector, String telPhone
            , int pageSize, int pageNo, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        List<PurchaserPaymentRecord> list = purchaserPaymentRecordService.purchaserPaymentRecordList(startPayDate, endPayDate
                , buyerName, buyerDirector, telPhone);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "采购商支付记录.csv");
        StringBuffer sb = new StringBuffer();
        sb.append("采购商,").append("采购商负责人,").append("采购商电话,").append("采购商id,").append("用户昵称,")
                .append("支付状态,").append("支付金额,").append("支付时间/n");
        for (PurchaserPaymentRecord paymentRecord : list) {
            sb.append(paymentRecord.getTouristBuyer().getBuyerName()).append(",")
                    .append(paymentRecord.getTouristBuyer().getBuyerDirector()).append(",")
                    .append(paymentRecord.getTouristBuyer().getTelPhone()).append(",")
                    .append(paymentRecord.getTouristBuyer().getBuyerId()).append(",")
                    .append(paymentRecord.getTouristBuyer().getNickname()).append(",")
                    .append(paymentRecord.getTouristBuyer().getPayState().getValue()).append(",")
                    .append(paymentRecord.getMoney()).append(",")
                    .append(LocalDateTimeFormatter.toStr(paymentRecord.getPayDate())).append("/n");
        }
        return new ResponseEntity<>(sb.toString().getBytes("utf-8"), headers, HttpStatus.CREATED);
    }

    /**
     * 跳转到采购商产品设置列表页面
     *
     * @return
     */
    @RequestMapping(value = "toPurchaserProductSettingList", method = RequestMethod.GET)
    public String toPurchaserProductSettingList(HttpServletRequest request, Model model) {
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
     * @return
     */
    @RequestMapping(value = "purchaserProductSettingList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity purchaserProductSettingList(String name, int pageSize, int pageNo
            , HttpServletRequest request) throws JsonProcessingException {
        Page<PurchaserProductSetting> page = purchaserProductSettingService.purchaserProductSettingList(name
                , new PageRequest(pageNo, pageSize));
        Map<String, Object> map = new HashMap<>();
        map.put(TOTAL, page.getTotalPages());
        map.put(ROWS, page.getContent());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(map);
        return ResponseEntity.ok(objectMapper.writeValueAsString(map));
    }

    /**
     * 跳转到线路列表页面
     *
     * @return
     */
    @RequestMapping(value = "toTouristGoodList", method = RequestMethod.GET)
    public String toTouristGoodList(HttpServletRequest request, Model model) {
        //todo
        return "";
    }

    /**
     * 线路列表/推荐线路列表通过是否推荐属性进行判断是否推荐
     *
     * @param touristName       线路名称
     * @param supplierName      供应商名称
     * @param touristTypeId     线路类型ID
     * @param activityTypeId    活动ID
     * @param touristCheckState 线路审核状态
     * @param recommend         是否推荐 null全部，true推荐列表
     * @param pageSize          每页显示条数
     * @param pageNo            页码
     * @param request
     */
    @RequestMapping(value = "touristGoodList", method = RequestMethod.GET)
    public PageAndSelection<TouristGood> touristGoodList(String touristName, String supplierName, Long touristTypeId
            , Long activityTypeId, TouristCheckStateEnum touristCheckState, Boolean recommend, int pageSize, int
                                                                 pageNo
            , HttpServletRequest request) {
        ActivityType activityType = null;
        TouristType touristType = null;
        if (touristTypeId != null) {
            touristType = touristTypeService.getOne(touristTypeId);
        }
        if (activityTypeId != null) {
            activityType = activityTypeService.getOne(touristTypeId);
        }
        Page<TouristGood> page;
        if (recommend) {
            page = touristGoodService.recommendTouristGoodList(touristName, supplierName, touristType, activityType
                    , touristCheckState, recommend, new PageRequest(pageNo, pageSize));
        } else {
            page = touristGoodService.touristGoodList(null, touristName, supplierName, touristType, activityType
                    , touristCheckState, new PageRequest(pageNo, pageSize));
        }
        return new PageAndSelection<>(page, TouristGood.selections);
    }


    /**
     * 跳转到活动类型列表页面
     *
     * @return
     */
    @RequestMapping(value = "toActivityTypeList", method = RequestMethod.GET)
    public String toActivityTypeList(HttpServletRequest request, Model model) {
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
    @RequestMapping(value = "activityTypeList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity activityTypeList(String name, int pageSize, int pageNo, HttpServletRequest request, Model model)
            throws JsonProcessingException {
        Page<ActivityType> page = activityTypeService.activityTypeList(name, new PageRequest(pageNo, pageSize));
        Map<String, Object> map = new HashMap();
        map.put(TOTAL, page.getTotalPages());
        map.put(ROWS, page.getContent());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(map);
        return ResponseEntity.ok(objectMapper.writeValueAsString(map));
    }

    /**
     * 跳转到线路类型列表页面
     *
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
    @RequestMapping(value = "touristTypeList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity touristTypeList(String name, int pageSize, int pageNo, HttpServletRequest request, Model model)
            throws JsonProcessingException {
        Page<TouristType> page = touristTypeService.touristTypeList(name, new PageRequest(pageNo, pageSize));
        Map<String, Object> map = new HashMap();
        map.put(TOTAL, page.getTotalPages());
        map.put(ROWS, page.getContent());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(map);
        return ResponseEntity.ok(objectMapper.writeValueAsString(map));
    }

    /**
     * 跳转到订单列表页面
     *
     * @return
     */
    @RequestMapping(value = "toSupplierOrders", method = RequestMethod.GET)
    public String toSupplierOrders(HttpServletRequest request, Model model) {
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
    public PageAndSelection supplierOrders(String orderNo, String touristName, String buyerName, String tel,
                                           PayTypeEnum payType, LocalDate orderDate, LocalDate endOrderDate, LocalDate payDate
            , LocalDate endPayDate, LocalDate touristDate, OrderStateEnum orderState
            , int pageSize, int pageNo, HttpServletRequest request, Model model) throws IOException {
        Page<TouristOrder> page = touristOrderService.supplierOrders(new PageRequest(pageNo, pageSize), orderNo
                , touristName, buyerName, tel, payType, orderDate, endOrderDate, payDate, endPayDate, touristDate, orderState);
        return new PageAndSelection<>(page, TouristOrder.htmlSelections);
    }

    /**
     * 跳转到结算单列表页面
     *
     * @return
     */
    @RequestMapping(value = "toSettlementSheetList", method = RequestMethod.GET)
    public String toSettlementSheetList(HttpServletRequest request, Model model) {
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
    public PageAndSelection settlementSheetList(String supplierName, SettlementStateEnum platformChecking, LocalDate createTime,
                                                int pageSize, int pageNo, HttpServletRequest request, Model model) {
        Page<SettlementSheet> page = settlementSheetService.settlementSheetList(supplierName, platformChecking, createTime
                , new PageRequest(pageNo, pageSize));
        return new PageAndSelection<>(page, SettlementSheet.selections);
    }

    /**
     * 跳转到提现单列表页面
     *
     * @return
     */
    @RequestMapping(value = "toPresentRecordList", method = RequestMethod.GET)
    public String toPresentRecordList(HttpServletRequest request, Model model) {
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
     * 跳转至新增供应商页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "toAddTouristSupplier", method = RequestMethod.GET)
    public String toAddTouristSupplier(HttpServletRequest request, Model model) {

        return "";
    }

    /**
     * 新增供应商 和 修改供应商
     *
     * @param id                 供应商id 为null 代表添加，不为null代表修改
     * @param supplierName       供应商名称  必须
     * @param adminAccount       登录名        必须
     * @param adminPassword      登录密码   必须
     * @param businessLicenseUri 营业执照uri 必须
     * @param contacts           联系人    必须
     * @param contactNumber      联系电话   必须
     * @param address            所在地    必须
     * @param remarks            备注
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = {"addSupplier", "updateSupplier"}, method = RequestMethod.POST)
    public String addTouristSupplier(Long id, @RequestParam String supplierName, @RequestParam String adminAccount,
                                     @RequestParam String adminPassword, @RequestParam String businessLicenseUri
            , @RequestParam String contacts, @RequestParam String contactNumber, @RequestParam Address address, String remarks,
                                     HttpServletRequest request, Model model) {
        TouristSupplier touristSupplier;
        if (id == null) {
            touristSupplier = new TouristSupplier();
        } else {
            touristSupplier = touristSupplierService.getOne(id);
        }
        touristSupplier.setCreateTime(LocalDateTime.now());
        touristSupplier.setAdminAccount(adminAccount);
        touristSupplier.setAdminPassword(adminPassword);
        touristSupplier.setBusinessLicenseUri(businessLicenseUri);
        touristSupplier.setRemarks(remarks);
        touristSupplier.setSupplierName(supplierName);
        touristSupplier.setContacts(contacts);
        touristSupplier.setContactNumber(contactNumber);
        touristSupplier.setAddress(address);

        touristSupplierService.save(touristSupplier);
        // TODO: 2016/12/21
        return "";
    }


    /**
     * 冻结供应商
     *
     * @param id     供应商id not null
     * @param frozen 是否冻结 not null
     * @return
     */
    @RequestMapping(value = {"frozenSupplier", "unFrozenSupplier"}, method = RequestMethod.POST)
    public String frozenSupplierOrUnFrozenSupplier(@RequestParam Long id, @RequestParam boolean frozen) {
        TouristSupplier touristSupplier = touristSupplierService.getOne(id);
        touristSupplier.setFrozen(frozen);
        touristSupplierService.save(touristSupplier);
        // TODO: 2016/12/21
        return "";
    }


    /**
     * 新增采购产品设置
     *
     * @param id        id 为null 代表添加，不为null代表修改
     * @param name      名称 not null
     * @param bannerUri 图片 not null
     * @param price     价格 not null
     * @param explain   说明 not null
     * @param agreement 协议 not null
     * @return
     */
    @RequestMapping(value = {"savePurchaserProductSetting", "updatePurchaserProductSetting"}, method = RequestMethod.POST)
    public String savePurchaserProductSetting(Long id, @RequestParam String name, @RequestParam String bannerUri,
                                              @RequestParam BigDecimal price, @RequestParam String explain
            , @RequestParam String agreement) {
        PurchaserProductSetting purchaserProductSetting;
        if (id == null) {
            purchaserProductSetting = new PurchaserProductSetting();
            purchaserProductSetting.setCreateTime(LocalDateTime.now());
        } else {
            purchaserProductSetting = purchaserProductSettingService.getOne(id);
            purchaserProductSetting.setUpdateTime(LocalDateTime.now());
        }
        purchaserProductSetting.setName(name);
        purchaserProductSetting.setBannerUri(bannerUri);
        purchaserProductSetting.setPrice(price);
        purchaserProductSetting.setExplain(explain);
        purchaserProductSetting.setAgreement(agreement);
        purchaserProductSettingService.save(purchaserProductSetting);
        // TODO: 2016/12/21
        return "";
    }

    /**
     * 推荐商品 和 取消推荐
     *
     * @param id        id   not null
     * @param recommend 推荐  not null
     * @return
     */
    @RequestMapping(value = {"recommendTouristGood", "unRecommendTouristGood"}, method = RequestMethod.POST)
    public String recommendGoodOrUnRecommendGood(@RequestParam Long id, @RequestParam boolean recommend) {
        TouristGood touristGood = touristGoodService.getOne(id);
        touristGood.setRecommend(recommend);
        touristGoodService.save(touristGood);
        // TODO: 2016/12/21
        return "";
    }

    /**
     * 审核通过 和 未通过审核
     *
     * @param id                id   not null
     * @param touristCheckState 审核状态  not null
     * @return
     */
    @RequestMapping(value = {"notCheck", "FinishCheck"}, method = RequestMethod.POST)
    public String notCheckOrFinishCheck(@RequestParam Long id, @RequestParam TouristCheckStateEnum
            touristCheckState) {
        TouristGood touristGood = touristGoodService.getOne(id);
        touristGood.setTouristCheckState(touristCheckState);
        touristGoodService.save(touristGood);
        // TODO: 2016/12/21
        return "";
    }

    /**
     * 查看线路
     *
     * @param id    id   not null
     * @param model
     * @return
     */
    @RequestMapping(value = "seeTouristGood", method = RequestMethod.GET)
    public String seeTouristGood(@RequestParam Long id, Model model) {
        TouristGood touristGood = touristGoodService.getOne(id);
        model.addAttribute("touristGood", touristGood);
        // TODO: 2016/12/21
        return "";
    }


    /**
     * 添加或修改活动类型
     *
     * @param id
     * @param activityName
     * @return
     */
    @RequestMapping(value = {"saveActivityType", "updateActivityType"}, method = RequestMethod.POST)
    public String saveOrUpdateActivityType(Long id, @RequestParam String activityName) {
        ActivityType activityType;
        if (id == null) {
            activityType = new ActivityType();
            activityType.setCreateTime(LocalDateTime.now());
        } else {
            activityType = activityTypeService.getOne(id);
            activityType.setUpdateTime(LocalDateTime.now());
        }
        activityType.setActivityName(activityName);
        activityTypeService.save(activityType);
        // TODO: 2016/12/21
        return "";
    }

    /**
     * 添加或修改线路类型
     *
     * @param id
     * @param typeName
     * @return
     */
    @RequestMapping(value = {"saveTouristType", "updateTouristType"}, method = RequestMethod.POST)
    public String saveOrUpdateTouristType(Long id, @RequestParam String typeName) {
        TouristType touristType;
        if (id == null) {
            touristType = new TouristType();
            touristType.setCreateTime(LocalDateTime.now());
        } else {
            touristType = touristTypeService.getOne(id);
            touristType.setUpdateTime(LocalDateTime.now());
        }
        touristType.setTypeName(typeName);
        touristTypeService.save(touristType);
        // TODO: 2016/12/21
        return "";
    }


}
