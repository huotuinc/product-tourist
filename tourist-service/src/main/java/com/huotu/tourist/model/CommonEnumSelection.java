/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.model;


/**
 * Created by lhx on 2016/12/20.
 */

public class CommonEnumSelection<T, R> implements Selection<T, R> {
    private final String field;
    private final String name;

    /**
     * @param field 支持以.为分隔符的字段选择
     * @param name
     */
    public CommonEnumSelection(String field, String name) {
        this.field = field;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public R apply(T t) {

        return null;
    }
}
