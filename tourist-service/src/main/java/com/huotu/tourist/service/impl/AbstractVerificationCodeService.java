/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.VerificationCode;
import com.huotu.tourist.entity.pk.VerificationCodePK;
import com.huotu.tourist.exception.IllegalVerificationCodeException;
import com.huotu.tourist.model.VerificationType;
import com.huotu.tourist.repository.VerificationCodeRepository;
import com.huotu.tourist.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author CJ
 */
public abstract class AbstractVerificationCodeService implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public void verify(String mobile, String code, VerificationType type) throws IllegalVerificationCodeException {
        VerificationCode verificationCode = verificationCodeRepository.findOne(new VerificationCodePK(mobile, type));
        if (verificationCode == null)
            throw new IllegalVerificationCodeException(type);
        if (!verificationCode.getCode().equals(code))
            throw new IllegalVerificationCodeException(type);
        // 过期了
        // c > d
        if (LocalDateTime.now().isAfter(verificationCode.getCodeExpireTime()))
            throw new IllegalVerificationCodeException(type);
    }

    @Override
    public void sendCode(String mobile, VerificationType type) throws IOException {
        // 短时间内不允许 1 分钟?
        // 有效时间 10分钟?
        VerificationCode verificationCode = verificationCodeRepository.findOne(new VerificationCodePK(mobile, type));
        if (verificationCode != null) {
            // c < d - 10 + 1
            if (LocalDateTime.now().isBefore(verificationCode.getCodeExpireTime().minusMinutes(9)))
                throw new IllegalStateException("短时间内不可以重复发送。");
        } else {
            verificationCode = new VerificationCode();
            verificationCode.setMobile(mobile);
            verificationCode.setType(type);
        }

        String code = generateCode(mobile, type);

        // 执行发送
        send(mobile, type.work().apply(code));

        // 保存数据库
        verificationCode.setCode(code);
        verificationCode.setCodeExpireTime(LocalDateTime.now().plusMinutes(10));
        verificationCodeRepository.save(verificationCode);
    }

    /**
     * 实际的发送验证码
     *
     * @param to
     * @param content
     */
    protected abstract void send(String to, String content) throws IOException;

    /**
     * @param mobile
     * @param type
     * @return 生成验证码
     */
    protected abstract String generateCode(String mobile, VerificationType type);
}
