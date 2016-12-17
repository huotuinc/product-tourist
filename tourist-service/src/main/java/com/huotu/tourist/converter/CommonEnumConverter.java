/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.converter;

import com.huotu.tourist.common.CommonEnum;
import com.huotu.tourist.common.EnumUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * @author CJ
 */
public abstract class CommonEnumConverter<T extends CommonEnum> extends AutowireConverter<T> {

    private final Class<T> enumClass;

    public CommonEnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T convert(String source) {
        if (StringUtils.isEmpty(source))
            return null;
        try {
            return EnumUtils.valueOf(enumClass, NumberUtils.parseNumber(source, Integer.class));
        } catch (NumberFormatException ex) {
            for (T type : enumClass.getEnumConstants()) {
                Enum e = (Enum) type;
                if (e.name().equalsIgnoreCase(source))
                    return type;
            }
        }
        return null;

    }
}
