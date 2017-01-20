/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.mall;

import java.io.IOException;

/**
 * 可以从content中获取实际的结果
 *
 * @author CJ
 */
public interface ContentResolver<T> {

    /**
     * @param content 输入content
     * @return 结果
     * @throws IOException
     */
    T fromResultContent(ResultContent content) throws IOException;
}
