/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.mall;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author CJ
 */
@Getter
@Setter
public class ResultContent {
    int resultCode;
    String resultMsg;
    Map data;
}
