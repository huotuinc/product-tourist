/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.model;

import java.util.function.Function;

/**
 * @param <T> 实体类型
 * @param <R> 实际选择的数据类型
 * @author CJ
 */
public interface Selection<T, R> extends Function<T, R> {

    String getName();

}
