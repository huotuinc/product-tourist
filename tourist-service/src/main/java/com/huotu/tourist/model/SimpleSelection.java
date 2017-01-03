/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author CJ
 */
public class SimpleSelection<T, R> implements Selection<T, R> {

    private final String field;
    private final String name;

    /**
     * @param field 支持以.为分隔符的字段选择
     * @param name
     */
    public SimpleSelection(String field, String name) {
        this.field = field;
        this.name = name;
    }

    private static Object getProperty(Object instance, String fieldName) {
        final String[] fieldNames = fieldName.split("\\.", -1);
        //if using dot notation to navigate for classes
        if (fieldNames.length > 1) {
            final String firstProperty = fieldNames[0];
            final String otherProperties =
                    StringUtils.join(fieldNames, '.', 1, fieldNames.length);
            return getProperty(getProperty(instance, firstProperty), otherProperties);
        }

        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(instance.getClass(), fieldName);
        if (propertyDescriptor == null)
            throw new IllegalArgumentException("can not find " + fieldName + " in " + instance);
        try {
            return propertyDescriptor.getReadMethod().invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R apply(T t) {
        return (R) getProperty(t, field);
    }

}
