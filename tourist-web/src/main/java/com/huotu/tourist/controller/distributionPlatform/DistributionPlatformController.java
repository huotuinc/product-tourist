package com.huotu.tourist.controller.distributionPlatform;

import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.common.SettlementStateEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.service.TouristSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lhx on 2016/12/17.
 */
@Controller
@RequestMapping("/distributionPlatform/")
public class DistributionPlatformController {
    @Autowired
    TouristSupplierService touristSupplierService;

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
     * 采购商列表
     *
     * @param buyerName 采购商名称
     * @param pageSize  每页显示条数
     * @param pageNo    页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "buyerList", method = RequestMethod.GET)
    public ResponseEntity buyerList(String buyerName, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        return null;
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
        return null;
    }


    /**
     * 线路列表
     *
     * @param touristName       线路名称
     * @param touristTypeId     线路类型ID
     * @param activityTypeId    活动ID
     * @param touristCheckState 线路审核状态
     * @param pageSize          每页显示条数
     * @param pageNo            页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "touristGoodList", method = RequestMethod.GET)
    public ResponseEntity touristGoodList(String touristName, Long touristTypeId, Long activityTypeId
            , TouristCheckStateEnum touristCheckState, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 推荐线路列表
     *
     * @param touristName       线路名称
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
    public ResponseEntity recommendTouristGoodList(String touristName, Long touristTypeId, Long activityTypeId
            , TouristCheckStateEnum touristCheckState, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 活动类型列表
     *
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
        return null;
    }

    /**
     * 线路类型列表
     *
     *
     * @param name      线路名称
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "touristTypeList", method = RequestMethod.GET)
    public ResponseEntity touristTypeList(String name, int pageSize, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }


    /**
     * 订单列表
     *
     * @param orderNo         订单号
     * @param touristName     线路名称
     * @param buyerName       采购商名称
     * @param orderState      订单状态
     * @param startCreateTime 开始订单创建时间
     * @param endCreateTime   结束订单创建时间
     * @param startPayTime    开始支付时间
     * @param endPayTime      结束支付时间
     * @param fromDate        线路开始时间
     * @param pageSize        每页显示条数
     * @param pageNo          页码
     * @param request
     * @param model           @return
     */
    @RequestMapping(value = "supplierOrders", method = RequestMethod.GET)
    public ResponseEntity supplierOrders(String orderNo, String touristName, String buyerName, String orderState
            , String startCreateTime, String endCreateTime, String startPayTime, String endPayTime, String fromDate
            , int pageSize, int pageNo, HttpServletRequest request, Model model) {

        return null;
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
    public ResponseEntity settlementSheetList(String supplierName, SettlementStateEnum platformChecking, String createTime,
                                              int pageSize, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 提现单列表
     *
     * @param supplierName     供应商名称
     * @param presentState     提现状态
     * @param createTime       提现单创建时间
     * @param pageSize 每页显示条数
     * @param pageNo   页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "presentRecordList", method = RequestMethod.GET)
    public ResponseEntity presentRecordList(String supplierName, PresentStateEnum presentState, String createTime,
                                            int pageSize, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }


}
