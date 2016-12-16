/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.common;

/**
 * 枚举工具类
 *
 * @author CJ
 */
public class EnumUtils {

    /**
     * 按枚举的{@link CommonEnum#getCode() code}获取枚举
     *
     * @param tClass 枚举类
     * @param code   需要获取枚举的code
     * @param <T>    枚举类
     * @return 指定code的枚举实例, 没有找到会返回null
     */
    public static <T extends CommonEnum> T valueOf(Class<T> tClass, Object code) {
        for (T t : tClass.getEnumConstants()) {
            if (t.getCode().equals(code))
                return t;
        }
        return null;
    }

}
