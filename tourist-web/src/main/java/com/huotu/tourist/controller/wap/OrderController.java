package com.huotu.tourist.controller.wap;

import com.huotu.huobanplus.common.entity.Product;
import com.huotu.huobanplus.sdk.common.repository.ProductRestRepository;
import com.huotu.tourist.common.BuyerPayStateEnum;
import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.PurchaserPaymentRecord;
import com.huotu.tourist.entity.PurchaserProductSetting;
import com.huotu.tourist.entity.SystemString;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.repository.PurchaserPaymentRecordRepository;
import com.huotu.tourist.repository.PurchaserProductSettingRepository;
import com.huotu.tourist.repository.SystemStringRepository;
import com.huotu.tourist.repository.TouristBuyerRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.ConnectMallService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单逻辑
 * Created by slt on 2017/1/12.
 */
@Controller
@RequestMapping(value = "/wap/")
public class OrderController {
    private static final Log log = LogFactory.getLog(OrderController.class);
    @Autowired
    TouristBuyerRepository touristBuyerRepository;
    @Autowired
    SystemStringRepository systemStringRepository;
    @Autowired
    ProductRestRepository productRestRepository;
    @Autowired
    PurchaserProductSettingRepository purchaserProductSettingRepository;
    @Autowired
    PurchaserPaymentRecordRepository purchaserPaymentRecordRepository;
    @Autowired
    private TouristOrderRepository touristOrderRepository;
    @Autowired
    private ConnectMallService connectMallService;

    /**
     * 采购商资格订单支付
     * <p>
     *
     * @param payType 支付方式
     * @return
     */
    @RequestMapping(value = {"/buyerOrderPay"}, method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map buyerOrderPay(@AuthenticationPrincipal SystemUser user, @RequestParam PayTypeEnum
            payType) {
        Map map = new HashMap();
        if (user.isBuyer()) {
            try {
                //采购商资格支付订单
                TouristBuyer buyer = (TouristBuyer) user;
                buyer.setPayType(payType);
                String mallOrderId = connectMallService.pushBuyerOrderToMall(buyer);
                buyer.setMallOrderNo(mallOrderId);
                touristBuyerRepository.saveAndFlush(buyer);
                map.put("code", 200);
                map.put("orderId", mallOrderId);
                map.put("customerId", connectMallService.getMerchant().getId());
                map.put("msg", "success");
                return map;
            } catch (IOException e) {
                log.error(e.getMessage());
                map.put("code", 500);
                map.put("msg", e.getMessage());
                return map;
            }
        } else {
            map.put("code", 500);
            map.put("msg", "用户不是采购商");
        }
        return map;
    }

    /**
     * 线路订单支付
     * <p>
     *
     * @param orderId 订单id
     * @param payType 支付方式
     * @return
     */
    @RequestMapping(value = {"/orderPay"}, method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map orderPay(@AuthenticationPrincipal SystemUser user, @RequestParam Long orderId, @RequestParam PayTypeEnum
            payType) {
        Map map = new HashMap();
        if (user.isBuyer()) {
            try {
                TouristOrder order = touristOrderRepository.getOne(orderId);
                String mallOrderNo;
                order.setPayType(payType);
                mallOrderNo = connectMallService.pushOrderToMall(order);
                order.setMallOrderNo(mallOrderNo);
                map.put("code", 200);
                map.put("msg", "success");
                return map;
            } catch (IOException e) {
                log.error(e.getMessage());
                map.put("code", 500);
                map.put("msg", e.getMessage());
                return map;
            }
        } else {
            map.put("code", 500);
            map.put("msg", "用户不是采购商");
        }
        return map;
    }

    /**
     * 商场订单支付回调
     *
     * @param mallOrderNo 商城订单号 必须
     * @param pay         是否支付成功 必须
     * @param payType     支付类型 必须
     * @param orderType   订单类型 0 线路订单，1 采购商订单
     * @return
     */
    @RequestMapping(value = {"/orderPayCallback"}, method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void orderPayCallback(@RequestParam String mallOrderNo,
                                 @RequestParam PayTypeEnum payType, @RequestParam boolean pay, int orderType,
                                 HttpServletRequest request, Model model) {
        log.info("======== pay:" + pay + " == mallOrderNo:" + mallOrderNo + " == payType:" + payType + " == " +
                "orderType:" + orderType + " ========");
        //线路订单
        if (orderType == 0) {
            TouristOrder touristOrder = touristOrderRepository.findByMallOrderNo(mallOrderNo);
            if (pay && touristOrder.getOrderState()
                    .equals(OrderStateEnum.NotPay)) {
                touristOrder.setPayType(payType);
                touristOrder.setPayTime(LocalDateTime.now());
                model.addAttribute("mallOrderNo", mallOrderNo);
            }
            log.error("当前采购商与订单采购商不匹配或订单状态异常");
        } else {
            if (pay) {
                TouristBuyer buyer = touristBuyerRepository.findByMallOrderNo(mallOrderNo);
                log.info("======== buyerId:" + buyer.getId() + "========");
                buyer.setPayState(BuyerPayStateEnum.PayFinish);
                PurchaserPaymentRecord purchaserPaymentRecord = new PurchaserPaymentRecord();
                purchaserPaymentRecord.setPayDate(LocalDateTime.now());
                purchaserPaymentRecord.setTouristBuyer(buyer);
                SystemString qualificationsProductIdSystem = systemStringRepository.findOne("QualificationsProductId");
                try {
                    Product product = productRestRepository.getOne(qualificationsProductIdSystem.getValue());
                    purchaserPaymentRecord.setMoney(new BigDecimal(product.getPrice()));
                } catch (IOException e) {
                    List<PurchaserProductSetting> productSettings = purchaserProductSettingRepository.findAll();
                    purchaserPaymentRecord.setMoney(productSettings.get(0).getPrice());
                }
                purchaserPaymentRecordRepository.saveAndFlush(purchaserPaymentRecord);
                touristBuyerRepository.saveAndFlush(buyer);
            }
            log.error(pay ? "支付成功，订单号与当前采购商单号不一致或当前采购商以支付" : "商城支付失败，请重试");
        }

    }
}
