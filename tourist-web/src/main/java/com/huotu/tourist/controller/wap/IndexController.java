/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.controller.wap;

import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Banner;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristRoute;
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
        List<Banner> banners = bannerRepository.findByDeleteIsFalse(new PageRequest(0, 5));
        model.addAttribute("banners", banners);
        List<TouristGood> recommendGoods = touristGoodRepository.findByRecommendIsTrueAndDeleteIsFalse(new PageRequest(0, 3));
        model.addAttribute("recommendGoods", recommendGoods);
        List<ActivityType> activityTypes = activityTypeRepository.findByDeleteIsFalse(new PageRequest(0, 9));
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
        return "/view/wap/touristGoodInfo.html";
    }

    /**
     * 打开指定活动类型的商品列表界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/activityTypeGoods"})
    public String activityTypeGoods(@RequestParam Long activityTypeId, Model model) {
        model.addAttribute("activityTypeGoods", touristGoodRepository.findByActivityType_IdAndDeleteIsFalse(activityTypeId, new
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
