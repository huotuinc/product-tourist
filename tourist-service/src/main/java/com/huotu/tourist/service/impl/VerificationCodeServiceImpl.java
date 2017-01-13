/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.impl;

import com.huotu.tourist.model.VerificationType;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author CJ
 */
@Service
public class VerificationCodeServiceImpl extends AbstractVerificationCodeService {
    private final String serverUrl;
    private final String account;
    private final String password;

//    		<!--短信接口-->
//		<SMS_SerialNo>hangzhuang</SMS_SerialNo>
//		<SMS_Password>Out123321</SMS_Password>
//		<SMS_Provider>chuanglan</SMS_Provider>
//		<SMS_ServiceUrl></SMS_ServiceUrl>

    @Autowired
    public VerificationCodeServiceImpl(Environment environment) {
        serverUrl = environment.getProperty("tourist.sms.serverUrl", "http://222.73.117.156/msg/HttpBatchSendSM");
        account = environment.getProperty("tourist.sms.account", "hangzhuang");
        password = environment.getProperty("tourist.sms.serverUrl", "Out123321");
    }


    /**
     * @param uri        应用地址，类似于http://ip:port/msg/
     * @param account    账号
     * @param pswd       密码
     * @param mobiles    手机号码，多个号码使用","分割
     * @param content    短信内容
     * @param needstatus 是否需要状态报告，需要true，不需要false
     * @return 返回值定义参见HTTP协议文档
     */
    private String batchSend(String uri, String account, String pswd, String mobiles, String content,
                             boolean needstatus, String product, String extno) throws IOException
            , URISyntaxException {
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultConnectionConfig(ConnectionConfig.custom().build())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(30000)
                        .setSocketTimeout(30000)
                        .setConnectionRequestTimeout(30000)
                        .build())
                .build()) {
            URIBuilder builder = new URIBuilder(serverUrl);
            builder.setParameters(
                    new BasicNameValuePair("account", account)
                    , new BasicNameValuePair("pswd", password)
                    , new BasicNameValuePair("mobile", mobiles)
                    , new BasicNameValuePair("needstatus", String.valueOf(needstatus))
                    , new BasicNameValuePair("msg", content)
                    , new BasicNameValuePair("product", product)
                    , new BasicNameValuePair("extno", extno)
            );

            HttpGet method = new HttpGet(builder.build());

            return client.execute(method, new BasicResponseHandler());
        }
    }

    @Override
    protected void send(String to, String content) throws IOException {
        String text;
        try {
            text = batchSend(serverUrl, account, password, to, content, true, null, null);
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        int code = Integer.parseInt(text.split("\n")[0].split(",")[1]);
        if (code != 0)
            throw new IOException("sent failed, code:" + code);
//        switch (code) {
//            case 0:
//                errMsg = "发送成功";
//                break;
//            case 101:
//                errMsg = "无此用户";
//                break;
//            case 102:
//                errMsg = "密码错";
//                break;
//            case 103:
//                errMsg = "提交过快（提交速度超过流速限制）";
//                break;
//            case 104:
//                errMsg = "系统忙（因平台侧原因，暂时无法处理提交的短信）";
//                break;
//            case 105:
//                errMsg = "敏感短信（短信内容包含敏感词）";
//                break;
//            case 106:
//                errMsg = "消息长度错（>536或<=0）";
//                break;
//            case 107:
//                errMsg = "包含错误的手机号码";
//                break;
//            case 108:
//                errMsg = "手机号码个数错（群发>50000或<=0;单发>200或<=0）";
//                break;
//            case 109:
//                errMsg = "无发送额度（该用户可用短信数已使用完）";
//                break;
//            case 110:
//                errMsg = "不在发送时间内";
//                break;
//            case 111:
//                errMsg = "超出该账户当月发送额度限制";
//                break;
//            case 112:
//                errMsg = "无此产品，用户没有订购该产品";
//                break;
//            case 113:
//                errMsg = "extno格式错（非数字或者长度不对）";
//                break;
//
//            case 115:
//                errMsg = "自动审核驳回";
//                break;
//            case 116:
//                errMsg = "签名不合法，未带签名（用户必须带签名的前提下）";
//                break;
//            case 117:
//                errMsg = "IP地址认证错,请求调用的IP地址不是系统登记的IP地址";
//                break;
//            case 118:
//                errMsg = "用户没有相应的发送权限";
//                break;
//            case 119:
//                errMsg = "用户已过期";
//                break;
//        }
    }

    @Override
    protected String generateCode(String mobile, VerificationType type) {
        return RandomStringUtils.randomNumeric(4);
    }
}
