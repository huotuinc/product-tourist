package com.huotu.tourist.controller;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.SexEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.repository.*;
import com.huotu.tourist.service.TouristGoodService;
import com.huotu.tourist.service.TouristOrderService;
import com.huotu.tourist.service.TouristRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * 提供公共的controller 方法
 * Created by lhx on 2016/12/21.
 */

public class BaseController {
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
     * 修改订单备注
     *
     * @param id     线路订单号
     * @param remark 新的备注
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/modifyRemarks", method = RequestMethod.POST)
    @ResponseBody
    public void modifyRemarks(@RequestParam Long id, @RequestParam String remark) throws IOException {
        TouristOrder touristOrder = touristOrderRepository.getOne(id);
        touristOrder.setRemarks(remark);
    }


    /**
     * 修改订单状态
     *
     * @param id         订单ID
     * @param orderState 新的订单状态
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/modifyOrderState", method = RequestMethod.POST)
    @ResponseBody
    public void modifyOrderState(@RequestParam Long id, @RequestParam OrderStateEnum orderState) throws IOException {
        touristOrderRepository.modifyOrderState(orderState, id);
        TouristOrder order = touristOrderRepository.getOne(id);
        order.setOrderState(orderState);
    }

    /**
     * 修改游客的个人信息
     *
     * @param id   游客ID(必须)
     * @param name 游客姓名
     * @param sex  性别
     * @param age  年龄
     * @param tel  电话
     * @param IDNo 身份证号
     * @throws IOException
     */
    @RequestMapping(value = "/modifyTravelerBaseInfo", method = RequestMethod.POST)
    public void modifyTravelerBaseInfo(@RequestParam Long id, String name, SexEnum sex, Integer age, String tel, String IDNo)
            throws IOException {
        Traveler traveler = travelerRepository.getOne(id);
        traveler.setName(name);
        traveler.setSex(sex);
        traveler.setAge(age);
        traveler.setTelPhone(tel);
        traveler.setIDNo(IDNo);
//        travelerRepository.save(traveler);
    }

    /**
     * 修改游客的线路行程
     *
     * @param formerId 原先的线路行程ID
     * @param laterId  之后的线路行程ID
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/modifyOrderTouristDate", method = RequestMethod.POST)
    @ResponseBody
    public void modifyOrderTouristDate(@RequestParam Long formerId, @RequestParam Long laterId) throws IOException {
        int modifyNumber = travelerRepository.modifyRouteIdByRouteId(laterId, formerId);
//        ModelMap modelMap=new ModelMap();
//        modelMap.addAttribute("data",modifyNumber);
//        return modelMap;
    }

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
        model.addAttribute("routes", routes);
        model.addAttribute("good", touristGood);
        return "";
    }

    /**
     * 修改线路商品的状态
     *
     * @param id        线路商品ID
     * @param stateEnum 状态
     * @throws IOException
     */
    @RequestMapping("/modifyTouristGoodState")
    @ResponseBody
    public void modifyTouristGoodState(@RequestParam Long id, TouristCheckStateEnum stateEnum) throws IOException {
        TouristGood touristGood = touristGoodRepository.getOne(id);
        touristGood.setTouristCheckState(stateEnum);
//        touristGoodRepository.save(touristGood);
    }
}
