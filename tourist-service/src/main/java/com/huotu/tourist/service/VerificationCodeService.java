/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.exception.IllegalVerificationCodeException;
import com.huotu.tourist.model.VerificationType;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 验证码服务
 *
 * @author CJ
 */
public interface VerificationCodeService {

    /**
     * 验证
     *
     * @param mobile 手机号码
     * @param code
     * @param type   验证码类型
     * @throws IllegalVerificationCodeException 如果无效
     */
    @Transactional(readOnly = true)
    void verify(String mobile, String code, VerificationType type) throws IllegalVerificationCodeException;

    /**
     * 发送验证码
     *
     * @param mobile 手机号码
     * @param type
     * @throws IOException 如果发送失败
     */
    @Transactional
    void sendCode(String mobile, VerificationType type) throws IOException;

}
