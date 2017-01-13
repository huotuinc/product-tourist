/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.exception;

import com.huotu.tourist.model.VerificationType;
import lombok.Getter;

/**
 * 无效的验证码
 *
 * @author CJ
 */
@Getter
public class IllegalVerificationCodeException extends RuntimeException {

    private final VerificationType type;

    public IllegalVerificationCodeException(VerificationType type) {
        this.type = type;
    }
}
