package com.huotu.tourist;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import me.jiangcai.lib.test.SpringWebTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * 常用web测试基类
 */
public abstract class WebTest extends SpringWebTest{

    @Autowired
    protected TouristOrderRepository touristOrderRepository;

    @Autowired
    protected TouristSupplierRepository touristSupplierRepository;


    /**
     * 创建一个随机的订单状态
     * @return  订单状态
     */
    protected OrderStateEnum randomOrderStateEnum(){
        int orderStateNo=random.nextInt(7);
        switch (orderStateNo){
            case 0:
                return OrderStateEnum.NotPay;
            case 1:
                return OrderStateEnum.PayFinish;
            case 2:
                return OrderStateEnum.NotFinish;
            case 3:
                return OrderStateEnum.Finish;
            case 4:
                return OrderStateEnum.Invalid;
            case 5:
                return OrderStateEnum.Refunds;
            default:
                return OrderStateEnum.RefundsFinish;
        }
    }

    /**
     * 创建一个随机的支付方式
     * @return  支付方式
     */
    protected PayTypeEnum randomPayTypeEnum(){
        int payTypeNo=random.nextInt(2);
        switch (payTypeNo){
            case 0:
                return PayTypeEnum.Alipay;
            default:
                return PayTypeEnum.WeixinPay;
        }
    }


    /**
     * 创建当前的时间，如果状态是未支付则返回null
     * @param orderStateEnum    支付状态
     * @return                  当前时间
     */
    protected LocalDateTime randomLocalDateTime(OrderStateEnum orderStateEnum){
        if(!OrderStateEnum.NotPay.equals(orderStateEnum)){
            return LocalDateTime.now();
        }else {
            return null;
        }
    }


    /**
     * 创建一个测试线路订单
     * @return  测试的线路订单
     */
    protected TouristOrder createTouristOrder(TouristGood good, TouristBuyer buyer,String orderNo
            ,OrderStateEnum orderState,LocalDateTime payTime,PayTypeEnum payType){
        TouristOrder order=new TouristOrder();
        order.setTouristGood(good);
        order.setTouristBuyer(buyer);
        order.setOrderNo(orderNo);
        order.setOrderState(orderState);
        order.setPayTime(payTime);
        order.setPayType(payType);

        return touristOrderRepository.saveAndFlush(order);
    }

    /**
     * 创建一个供应商
     * @param supplierName
     * @return
     */
    protected TouristSupplier createTouristSupplier(String supplierName){

        TouristSupplier supplier=new TouristSupplier();
        supplier.setSupplierName(supplierName);

        return touristSupplierRepository.saveAndFlush(supplier);

    }


}
