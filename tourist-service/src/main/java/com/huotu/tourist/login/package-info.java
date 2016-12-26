/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 本包包含认证相关的一些类和接口
 * 按照需求规划，本系统的参与角色有
 * <ul>
 * <li>未获得采购商身份的小伙伴</li>
 * <li>已获得采购商身份的小伙伴--线路采购商</li>
 * <li>线路供应商</li>
 * <li>平台管理员，系统初始化时会构建默认的平台管理员帐号</li>
 * </ul>
 * 不同的角色验证方式也不太相同小伙伴的验证应该是根据已登录商城角色(cookie)决定的
 *
 * @author CJ
 */
package com.huotu.tourist.login;