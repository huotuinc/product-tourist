/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.huotu.tourist.entity.pk.VerificationCodePK;
import com.huotu.tourist.model.VerificationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

/**
 * 就是验证码啦
 *
 * @author CJ
 */
@Entity
@IdClass(VerificationCodePK.class)
@Setter
@Getter
public class VerificationCode {

    @Id
    private String mobile;
    @Id
    private VerificationType type;

    /**
     * 最近的验证码
     */
    @Column(length = 10, nullable = false)
    private String code;
    /**
     * 验证码过期时间
     */
    @Column(columnDefinition = "datetime", nullable = false)
    private LocalDateTime codeExpireTime;

}
