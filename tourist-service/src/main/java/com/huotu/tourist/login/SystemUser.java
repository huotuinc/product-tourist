/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.login;

/**
 * 可登录本系统的所有角色
 * Created by slt on 2016/12/20.
 */
public interface SystemUser {

    boolean isSupplier();

    /**
     * 是否是平台管理
     *
     * @return
     */
    boolean isPlatformUser();

}
