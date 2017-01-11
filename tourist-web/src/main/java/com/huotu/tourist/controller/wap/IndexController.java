/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.wap;

import com.huotu.tourist.TravelerList;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Banner;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.BannerRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TouristTypeRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.ActivityTypeService;
import com.huotu.tourist.service.PurchaserPaymentRecordService;
import com.huotu.tourist.service.TouristGoodService;
import com.huotu.tourist.service.TouristOrderService;
import com.huotu.tourist.service.TouristRouteService;
import com.huotu.tourist.service.TouristTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * index controller 方法
 * Created by lhx on 2016/12/21.
 */
@Controller
@RequestMapping(value = "/wap/")
public class IndexController {

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


    /**
     * 打开index.html
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/", "index/"})
    public String index(Model model) {
        List<Banner> banners = bannerRepository.findByDeletedIsFalse(new PageRequest(0, 5));
        model.addAttribute("banners", banners);
        List<TouristGood> recommendGoods = touristGoodRepository.findByRecommendIsTrueAndDeletedIsFalse(new PageRequest(0, 3));
        model.addAttribute("recommendGoods", recommendGoods);
        List<ActivityType> activityTypes = activityTypeRepository.findByDeletedIsFalse(new PageRequest(0, 9));
        model.addAttribute("activityTypes", activityTypes);
        return "/view/wap/index.html";
    }

    /**
     * 打开商品详细信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/goodInfo"})
    public String goodInfo(@RequestParam Long id, Model model) {
        TouristGood good = touristGoodRepository.getOne(id);
        Map<String, Integer> routeCount = new HashMap<>();
        for (TouristRoute touristRoute : good.getTouristRoutes()) {
            int num = travelerRepository.countByRoute(touristRoute);
            routeCount.put(touristRoute.getId().toString(), touristRoute.getMaxPeople() - num);
        }
        model.addAttribute("good", good);
        model.addAttribute("routeCount", routeCount);
        int count = touristGoodRepository.countByTouristSupplier_IdAndDeletedIsFalse(good.getTouristSupplier().getId());
        model.addAttribute("count", count);
        return "/view/wap/touristGoodInfo.html";
    }

    /**
     * 打开购买商品填写订单信息页面
     *
     * @param goodId
     * @param routeId
     * @param model
     * @return
     */
    @RequestMapping(value = {"/procurementGood"})
    public String procurementGood(@RequestParam Long goodId, Long routeId, Model model) {
        TouristGood good = touristGoodRepository.getOne(goodId);
        int count = travelerRepository.countByRoute(touristRouteRepository.getOne(routeId));
        model.addAttribute("amount", good.getMaxPeople() - count);
        // TODO: 2017/1/10 获取用户账户小金库，积分，余额信息
        model.addAttribute("good", good);
        model.addAttribute("routeId", routeId);
        return "view/wap/procurement.html";
    }

    /**
     * 添加采购信息
     *
     * @param travelers    游客信息
     * @param goodId       商品id
     * @param routeId      行程ID
     * @param buyerMoney   订单总金额
     * @param mallIntegral 商城积分 null代表未使用积分
     * @param mallBalance  商城余额 null代表未使用余额
     * @param mallCoffers  商城小金库 null代表未使用小金库
     * @param model
     * @return
     */
    @RequestMapping(value = {"/addOrderInfo"})
    public void addOrderInfo(@AuthenticationPrincipal TouristBuyer user, @RequestParam(required = false) @TravelerList
            List<Traveler> travelers, @RequestParam Long goodId, @RequestParam Long routeId, Float buyerMoney
            , Float mallIntegral, Float mallBalance, Float mallCoffers, Model model) {
        touristOrderService.addOrderInfo(user, travelers, goodId, routeId, mallIntegral, mallBalance, mallCoffers);
    }

    /**
     * 打开指定活动类型的商品列表界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/activityTypeGoods"})
    public String activityTypeGoods(@RequestParam Long activityTypeId, Model model) {
        model.addAttribute("activityTypeGoods", touristGoodRepository.findByActivityType_IdAndDeletedIsFalse(activityTypeId, new
                PageRequest(0, 6)));
        // TODO: 2017/1/6 页面
        return "";
    }

    /**
     * 最新线路列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/newTouristList"})
    public String newTouristList(int offset, Model model) {
        // TODO: 2017/1/6 页面
//        touristGoodRepository.findByTouristCheckState(TouristCheckStateEnum.CheckFinish, pageable)
        model.addAttribute("list", null);
        model.addAttribute("offset", offset);

        return "view/wap/newTourist.html";
    }


}
