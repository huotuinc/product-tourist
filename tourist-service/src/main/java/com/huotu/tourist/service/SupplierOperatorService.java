package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristSupplier;

import java.io.IOException;

/**
 * 操作员服务
 * Created by slt on 2017/1/18.
 */
public interface SupplierOperatorService {

    /**
     * 保存供应商的操作员
     * @param id            操作员ID
     * @param supplier      所属供应商
     * @param loginName     用户名
     * @param password      密码
     * @param tel           联系电话
     * @param name          姓名
     * @param authorityList 权限列表
     * @return
     * @throws IOException
     */
    TouristSupplier saveOperator(Long id, TouristSupplier supplier, String loginName
            , String password, String tel, String name, String[] authorityList)throws IOException;
}
