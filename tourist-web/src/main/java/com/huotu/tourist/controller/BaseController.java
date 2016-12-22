package com.huotu.tourist.controller;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.common.SexEnum;
import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.Traveler;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.TouristGoodService;
import com.huotu.tourist.service.TouristOrderService;
import com.huotu.tourist.service.TouristRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 提供公共的controller 方法
 * Created by lhx on 2016/12/21.
 */

public class BaseController {
    @Autowired
    public TravelerRepository travelerRepository;
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
    private TouristGoodService touristGoodService;

    @Autowired
    private TouristGoodRepository touristGoodRepository;


    /**
     * 导出订单列表
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
    @RequestMapping(value = "exportSupplierOrders", method = RequestMethod.GET)
    public ResponseEntity exportSupplierOrders(String orderNo, String touristName, String buyerName, String tel,
                                               PayTypeEnum payType, LocalDate orderDate, LocalDate endOrderDate, LocalDate payDate
            , LocalDate endPayDate, LocalDate touristDate, OrderStateEnum orderState
            , int pageSize, int pageNo, HttpServletRequest request, Model model) throws IOException {
        Page<TouristOrder> page = touristOrderService.supplierOrders(new PageRequest(pageNo, pageSize), orderNo
                , touristName, buyerName, tel, payType, orderDate, endOrderDate, payDate, endPayDate, touristDate, orderState);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "订单记录.csv");
        StringBuffer sb = new StringBuffer();
        sb.append("订单号,").append("下单时间,").append("支付时间,").append("支付方式,").append("供应商,").append("线路名称,")
                .append("金额,").append("购买人,").append("支付状态,").append("出行时间,").append("购买数量,").append("备注/n");
        for (TouristOrder touristOrder : page.getContent()) {
            List<Traveler> travelers = travelerRepository.findByOrder_Id(touristOrder.getId());
            sb
                    .append(touristOrder.getOrderNo()).append(",")
                    .append(LocalDateTimeFormatter.toStr(touristOrder.getCreateTime())).append(",")
                    .append(LocalDateTimeFormatter.toStr(touristOrder.getPayTime())).append(",")
                    .append(touristOrder.getPayType().getValue()).append(",")
                    .append(touristOrder.getTouristGood().getTouristSupplier().getSupplierName()).append(",")
                    .append(touristOrder.getTouristGood().getTouristName()).append(",")
                    .append(touristOrder.getOrderMoney()).append(",").append(touristOrder.getTouristBuyer().getBuyerName())
                    .append("/r").append(touristOrder.getTouristBuyer().getTelPhone()).append(",")
                    .append(touristOrder.getOrderState()).append(",")
                    .append(LocalDateTimeFormatter.toStr(travelers.get(0).getRoute().getFromDate())).append(",")
                    .append(travelers.size()).append(",")
                    .append(touristOrder.getRemarks());
        }
        return new ResponseEntity<>(sb.toString().getBytes("utf-8"), headers, HttpStatus.CREATED);
    }

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
