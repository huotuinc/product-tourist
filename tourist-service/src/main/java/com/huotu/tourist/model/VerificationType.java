/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.model;

import java.util.function.Function;

/**
 * @author CJ
 */
public enum VerificationType {

    register;

    public Function<String, String> work() {
        if (this == register)
            return code -> "您好，您的验证码是" + code + "，请勿泄露给他人。";

        return Function.identity();
    }
}
