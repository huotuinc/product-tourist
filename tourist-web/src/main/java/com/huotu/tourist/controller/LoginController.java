/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.controller;

import com.huotu.tourist.login.SupplierOperator;
import com.huotu.tourist.model.PageAndSelection;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.repository.SupplierOperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * 负责提供登录功能的控制器
 *
 * @author CJ
 */
@Controller
public class LoginController {

    @Autowired
    private SupplierOperatorRepository supplierOperatorRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String index() {
        return "view/manage/login.html";
    }


    /**
     * 打开供应商管理员列表
     * @return
     */
    @RequestMapping("/showJurisdiction")
    public String showJurisdiction(){
        return "/view/manage/supplier/jurisdictionList.html";
    }

    /**
     * 返回供应商操作员列表
     * @param pageable      分页信息
     * @return
     * @throws IOException
     */
    @RequestMapping("/getJurisdictionList")
    public PageAndSelection<SupplierOperator> getJurisdictionList(Pageable pageable)
            throws IOException {

        Page<SupplierOperator> supplierManagers=supplierOperatorRepository.findAll(pageable);
        Selection<SupplierOperator,String> loginName=new Selection<SupplierOperator, String>() {
            @Override
            public String getName() {
                return "loginName";
            }

            @Override
            public String apply(SupplierOperator supplierOperator) {
                return supplierOperator.getLoginName();
            }
        };


        return new PageAndSelection<>(supplierManagers,SupplierOperator.selections);
    }

    /**
     * 返回某个供应商操作员的详情页
     * @param id        操作员ID
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/getJurisdiction")
    public String getJurisdiction(Long id, Model model) throws IOException{
        SupplierOperator operator=supplierOperatorRepository.findOne(id);
        model.addAttribute("operator",operator);
        return "/view/manage/supplier/jurisdictionDeails.html";
    }


}
