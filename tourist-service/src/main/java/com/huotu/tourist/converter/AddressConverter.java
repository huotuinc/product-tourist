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
import org.springframework.stereotype.Component;

/**
 * 地址转换器
 *  字符串格式如："浙江省/杭州市/滨江区"，使用“/”分割.
 * @author slt
 */
@Component
public class AddressConverter extends AutowireConverter<Address> {

    @Override
    public Address convert(String source) {
        String[] strings=source.split("/");
        Address address=new Address();
        switch (strings.length){
//            case 4:
//                address.setDetailedAddress(strings[3]);
            case 3:
                address.setDistrict(strings[2]);
            case 2:
                address.setTown(strings[1]);
            case 1:
                address.setProvince(strings[0]);
        }
        return address;
    }
}