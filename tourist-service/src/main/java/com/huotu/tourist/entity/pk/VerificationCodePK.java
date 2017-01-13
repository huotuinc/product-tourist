/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.entity.pk;

import com.huotu.tourist.model.VerificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author CJ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCodePK implements Serializable {

    private static final long serialVersionUID = 8139423888164671288L;
    @Column(length = 15)
    private String mobile;
    private VerificationType type;
}
