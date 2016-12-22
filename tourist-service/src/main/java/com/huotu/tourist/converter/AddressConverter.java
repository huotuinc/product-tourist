/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.converter;

import com.huotu.tourist.entity.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 地址转换器
 *
 * @author slt
 */
@Component
public class AddressConverter implements Converter<String[],Address> {
    @Override
    public Address convert(String[] source) {
        Address address=new Address();
        address.setProvince(source[0]);
        address.setTown(source[1]);
        address.setDistrict(source[2]);
        return address;
    }
}