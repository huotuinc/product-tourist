package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristSupplier;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * 操作员服务测试
 * Created by slt on 2017/1/18.
 */
public class SupplierOperatorServiceTest extends ServiceBaseTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveOperatorTest() throws Exception {
        String passward = "123456";
        TouristSupplier sltSupplier = createTouristSupplier("slt");
        TouristSupplier wySupplier = createTouristSupplier("wy");
        String[] auths = {"ABC", "DEF"};
        String[] authss = {"KKK", "FFF"};
        TouristSupplier operatorExp = supplierOperatorService.saveOperator(null, sltSupplier, "shiliting", passward,
                "18069771234", "slt", auths);

        TouristSupplier operatorAct = touristSupplierRepository.findOne(operatorExp.getId());

        Assert.isTrue(operatorExp.equals(operatorAct));

        operatorExp = supplierOperatorService.saveOperator(operatorExp.getId(), wySupplier, "wwyy", passward + "1",
                "18069774321", "wy", authss);

        operatorAct = touristSupplierRepository.findOne(operatorExp.getId());

        Assert.isTrue(operatorExp.equals(operatorAct));

    }


}
