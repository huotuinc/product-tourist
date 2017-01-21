/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.controller;

import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import com.huotu.tourist.common.BuyerAuthentication;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.exception.NotLoginYetException;
import com.huotu.tourist.repository.TouristBuyerRepository;
import com.huotu.tourist.service.ConnectMallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 负责提供登录功能的控制器
 *
 * @author CJ
 */
@Controller
public class LoginController {

    SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    @Autowired
    private UserRestRepository userRestRepository;
    @Autowired
    private TouristBuyerRepository touristBuyerRepository;
    @Autowired
    private ConnectMallService connectMallService;
    @Autowired
    private Environment environment;
    private SecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    @Transactional
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try{
            long userId = connectMallService.currentUserId(request);
            HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
            SecurityContext context = httpSessionSecurityContextRepository.loadContext(holder);
            TouristBuyer buyer=touristBuyerRepository.findOne(userId);
            if (buyer==null){
                buyer=new TouristBuyer();
                buyer.setId(userId);
                buyer.setCreateTime(LocalDateTime.now());
                buyer = touristBuyerRepository.save(buyer);
            }

            BuyerAuthentication buyerAuthentication=new BuyerAuthentication(buyer);
            context.setAuthentication(buyerAuthentication);
            httpSessionSecurityContextRepository.saveContext(context,request,response);
            successHandler.onAuthenticationSuccess(request,response,buyerAuthentication);
        } catch (NotLoginYetException ex) {

        }

        return "view/manage/login.html";
    }

}
