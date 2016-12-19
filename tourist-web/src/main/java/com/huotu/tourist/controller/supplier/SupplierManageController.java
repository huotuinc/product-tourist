package com.huotu.tourist.controller.supplier;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.model.TouristOrderDetailsModel;
import com.huotu.tourist.model.TouristOrderModel;
import com.huotu.tourist.model.TouristRouteModel;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.OrderService;
import com.huotu.tourist.service.TouristRouteService;
import com.huotu.tourist.service.TravelerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDate;
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
    private OrderService orderService;

    @Autowired
    private TouristOrderRepository touristOrderRepository;

    @Autowired
    private TouristRouteRepository touristRouteRepository;

    @Autowired
    private TravelerService travelerService;

    @Autowired
    private TouristRouteService touristRouteService;

    @Autowired
    private TravelerRepository travelerRepository;


    /**
     * 打开订单列表页面
     * @return
     */
    @RequestMapping("/showOrderList")
    public String showOrderList(Model model){
        return "";
    }

    /**
     * 根据某个供应商的订单列表
     * @param supplierId    供应商(必须)
     * @param pageNo        第几页(必须)
     * @param pageSize      每页几条信息(必须)
     * @param orderId       订单ID
     * @param name          路线名称
     * @param buyer         购买人
     * @param tel           购买人电话
     * @param payTypeEnum   付款状态
     * @param orderDate     下单时间
     * @param payDate       支付时间
     * @param orderStateEnum 结算状态
     * @param touristDate   出行时间
     * @return
     * @throws IOException
     */
    @RequestMapping("/orderList")
    @ResponseBody
    public ModelMap orderList(@RequestParam Long supplierId, @RequestParam Integer pageNo, @RequestParam Integer pageSize
            , String orderId, String name, String buyer, String tel, PayTypeEnum payTypeEnum, LocalDate orderDate
            , OrderStateEnum orderStateEnum, LocalDate payDate, LocalDate touristDate) throws IOException{

        TouristSupplier supplier= touristSupplierRepository.findOne(supplierId);

        Page<TouristOrder> orders=orderService.supplierOrders(supplier,new PageRequest(pageNo+1,pageSize),
                orderId, name, buyer, tel, payTypeEnum, orderDate, null, payDate, null, touristDate, orderStateEnum);

        List<TouristOrderModel> touristOrderModels=orderService.touristOrderModelConver(orders.getContent());

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

        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("rows",touristOrderModels);
        modelMap.addAttribute("total",orders.getTotalElements());
        return modelMap;
    }

    /**
     * 获取所有与他相同的线路订单的出行时间以及人数
     * @param id    线路订单ID
     * @return
     * @throws IOException
     */
    @RequestMapping("/getAllOrderTouristDate")
    @ResponseBody
    public ModelMap getAllOrderTouristDate(@RequestParam Long id) throws IOException{

        TouristOrder order=touristOrderRepository.findOne(id);

        List<TouristRoute> routes=touristRouteRepository.findByGood(order.getTouristGood());

        List<TouristRouteModel> touristRouteModels=touristRouteService.touristRouteModelConver(routes);

//        for(TouristRoute route :routes){
//            //排除自己的线路订单
//            if(route.getId().equals(id)){
//                continue;
//            }
//            TouristRouteModel model=new TouristRouteModel();
//            model.setId(route.getId());
//            model.setFromDate(route.getFromDate());
//            model.setRemainPeople(touristRouteService.getRemainPeopleByRoute(route));
//            touristRouteModels.add(model);
//        }

        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("data",touristRouteModels);
        return modelMap;
    }

    /**
     * 修改游客的线路行程
     * @param formerId  原先的线路行程ID
     * @param laterId   之后的线路行程ID
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/modifyOrderTouristDate",method = RequestMethod.POST)
    @ResponseBody
    public ModelMap modifyOrderTouristDate(@RequestParam Long formerId,@RequestParam Long laterId) throws IOException{
        int modifyNumber=travelerRepository.modifyRouteIdByRouteId(laterId,formerId);
        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("data",modifyNumber);
        return modelMap;
    }

    /**
     * 返回某个订单的视图信息
     * @param id    线路订单
     * @return      线路订单视图
     * @throws IOException
     */
    @RequestMapping("/showOrder")
    public String showOrder(@RequestParam Long id,Model model) throws IOException{

        TouristOrder order=touristOrderRepository.findOne(id);

        TouristOrderDetailsModel touristOrderDetailsModel=orderService.touristOrderDetailsModelConver(order);

        model.addAttribute("data",touristOrderDetailsModel);

        return "";

    }

    /**
     * 修改订单备注
     * @param id        线路订单号
     * @param remark    新的备注
     * @return
     * @throws IOException
     */
    @RequestMapping("/modifyRemarks")
    public ModelMap modifyRemarks(@RequestParam Long id,@RequestParam String remark) throws IOException{
        int number=touristOrderRepository.modifyRemarks(remark,id);
        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("data",number);
        return modelMap;
    }



}
