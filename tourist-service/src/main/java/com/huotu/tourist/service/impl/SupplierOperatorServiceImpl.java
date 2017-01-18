package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.service.LoginService;
import com.huotu.tourist.service.SupplierOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by slt on 2017/1/18.
 */
@Service
public class SupplierOperatorServiceImpl implements SupplierOperatorService {
    @Autowired
    private TouristSupplierRepository supplierRepository;
    @Autowired
    private LoginService loginService;
    @Override
    public TouristSupplier saveOperator(Long id, TouristSupplier supplier, String loginName, String password
            , String tel, String name, String[] authorityList) throws IOException {

        TouristSupplier operator;
        if(id==null){
            operator=new TouristSupplier();
            operator.setCreateTime(LocalDateTime.now());
        }else {
            operator=supplierRepository.getOne(id);
        }
        operator.setUpdateTime(LocalDateTime.now());
        operator.setAuthSupplier(supplier);
        operator.setLoginName(loginName);
        operator.setOperatorName(name);
        operator.setOperatorTel(tel);
        operator.setAuthorityList(new HashSet<>(Arrays.asList(authorityList)));

        supplierRepository.save(operator);

        loginService.saveLogin(operator,password);

        return operator;
    }
}
