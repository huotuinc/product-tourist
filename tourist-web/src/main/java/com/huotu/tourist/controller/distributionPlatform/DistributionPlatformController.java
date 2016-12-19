package com.huotu.tourist.controller.distributionPlatform;

import com.huotu.tourist.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lhx on 2016/12/17.
 */
@Controller
@RequestMapping("/distributionPlatform/")
public class DistributionPlatformController {

    /**
     * 供应商列表
     *
     * @param limit   每页显示条数
     * @param pageNo  页码
     * @param name
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "supplierList", method = RequestMethod.GET)
    public ResponseEntity supplierList(String name, int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 采购商列表
     *
     * @param limit     每页显示条数
     * @param pageNo    页码
     * @param buyerName
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "buyerList", method = RequestMethod.GET)
    public ResponseEntity buyerList(String buyerName, int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 采购商支付记录列表
     *
     * @param startPayDate
     * @param endPayDate
     * @param data
     * @param limit        每页显示条数
     * @param pageNo       页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "purchaserPaymentRecordList", method = RequestMethod.GET)
    public ResponseEntity purchaserPaymentRecordList(String startPayDate, String endPayDate, PurchaserPaymentRecord data,
                                                     int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 导出采购商支付记录列表
     *
     * @param startPayDate
     * @param endPayDate
     * @param limit        每页显示条数
     * @param pageNo       页码
     * @param data
     * @param request
     * @param model
     */
    @RequestMapping(value = "exportPurchaserPaymentRecord", method = RequestMethod.GET)
    public void exportPurchaserPaymentRecord(String startPayDate, String endPayDate, PurchaserPaymentRecord data
            , int limit, int pageNo, HttpServletRequest request, Model model) {

    }

    /**
     * 采购商产品设置列表
     *
     * @param data
     * @param limit   每页显示条数
     * @param pageNo  页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "purchaserProductSettingList", method = RequestMethod.GET)
    public ResponseEntity purchaserProductSettingList(PurchaserProductSetting data, int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }


    /**
     * 线路列表
     *
     * @param data
     * @param limit   每页显示条数
     * @param pageNo  页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "touristGoodList", method = RequestMethod.GET)
    public ResponseEntity touristGoodList(TouristGood data, int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 推荐线路列表
     *
     * @param limit   每页显示条数
     * @param pageNo  页码
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "recommendTouristGoodList", method = RequestMethod.GET)
    public ResponseEntity recommendTouristGoodList(int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 订单列表
     *
     * @param limit
     * @param pageNo
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "supplierOrders", method = RequestMethod.GET)
    public ResponseEntity supplierOrders(int limit, int pageNo, HttpServletRequest request, Model model) {
        return null;
    }


    /**
     * 结算单列表
     *
     * @param limit
     * @param pageNo
     * @param data
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "settlementSheetList", method = RequestMethod.GET)
    public ResponseEntity settlementSheetList(int limit, int pageNo, SettlementSheet data, HttpServletRequest request, Model model) {
        return null;
    }

    /**
     * 提现单列表
     *
     * @param limit
     * @param pageNo
     * @param data
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "presentRecordList", method = RequestMethod.GET)
    public ResponseEntity presentRecordList(int limit, int pageNo, PresentRecord data, HttpServletRequest request, Model model) {
        return null;
    }



}
