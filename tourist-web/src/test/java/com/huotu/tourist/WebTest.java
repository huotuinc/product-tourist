package com.huotu.tourist;

import com.huotu.tourist.common.OrderStateEnum;
import me.jiangcai.lib.test.SpringWebTest;

import java.time.LocalDateTime;

/**
 * 常用web测试基类
 */
public abstract class WebTest extends SpringWebTest{


    /**
     * 创建一个随机的支付状态
     * @return  支付状态
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


}
