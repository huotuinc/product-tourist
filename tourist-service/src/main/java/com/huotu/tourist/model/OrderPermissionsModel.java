package com.huotu.tourist.model;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.login.SystemUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 后台订单列表model
 * Created by slt on 2016/12/19.
 */
@Getter
@Setter
public class OrderPermissionsModel {
    /**
     * 系统角色拥有的可修改的状态
     */
    public static OrderStateEnum[] systemPossess = {OrderStateEnum.NotPay, OrderStateEnum.NotFinish, OrderStateEnum.Finish, OrderStateEnum.Invalid};

    /**
     * 采购商角色拥有的可修改的状态
     */
    public static OrderStateEnum[] purchaserPossess = {OrderStateEnum.PayFinish, OrderStateEnum.Finish, OrderStateEnum.Invalid};

    /**
     * 供应商角色拥有的可修改的状态
     */
    public static OrderStateEnum[] supplierPossess = {OrderStateEnum.NotFinish, OrderStateEnum.Refunds, OrderStateEnum.Invalid};

    /**
     * 平台角色拥有的可修改的状态
     */
    public static OrderStateEnum[] platformPossess = {OrderStateEnum.NotFinish, OrderStateEnum.Refunds, OrderStateEnum.RefundsFinish, OrderStateEnum.Invalid};


    /**
     * 将每个订单状态可以修改成其他状态，存储到一个二维数组中，第一维是当前订单的状态，第二维是该订单能修改的其他订单状态
     * OrderStateEnum[0:NotPay][{PayFinish,Invalid}]
     * OrderStateEnum[1:PayFinish][{NotFinish,Refunds,Invalid}]
     * OrderStateEnum[2:NotFinish][{Finish,Invalid}]
     * OrderStateEnum[3:Finish][无法修改]
     * OrderStateEnum[4:Refunds][{RefundsFinish,NotFinish}]
     * OrderStateEnum[5:RefundsFinish][无法修改]
     * OrderStateEnum[6:Invalid][无法修改]
     */
    public static OrderStateEnum[][] revisability = {
            {OrderStateEnum.Finish, OrderStateEnum.Invalid}
            , {OrderStateEnum.NotFinish, OrderStateEnum.Refunds, OrderStateEnum.Invalid}
            , {OrderStateEnum.Finish, OrderStateEnum.Invalid}
            , {}
            , {OrderStateEnum.RefundsFinish, OrderStateEnum.NotFinish}
            , {}
            , {}};

    /**
     * 根据所给角色信息返回该角色能修改的订单状态，如果识别不了则返回空数组
     *
     * @param user 角色信息
     * @return
     */
    public static OrderStateEnum[] getAuthOrderStates(SystemUser user) {
        if (user == null) {
            return systemPossess;
        } else if (user.isSupplier()) {
            return supplierPossess;
        } else if (user.isPlatformUser()) {
            return platformPossess;
        }
        return new OrderStateEnum[]{};
    }
}
