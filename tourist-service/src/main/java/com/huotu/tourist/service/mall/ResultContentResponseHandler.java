/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.mall;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;

import java.io.IOException;

/**
 * @author CJ
 */
public class ResultContentResponseHandler<T> extends AbstractResponseHandler<T> {
    private final ContentResolver<T> resolver;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ResultContentResponseHandler(ContentResolver<T> resolver) {
        this.resolver = resolver;
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        ResultContent result = objectMapper.readValue(entity.getContent(), ResultContent.class);
        if (result.getResultCode() == 2000) {
            return resolver.fromResultContent(result);
        } else {
            throw new IOException(result.getResultMsg());
        }
    }
}
