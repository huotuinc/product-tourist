/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.common.entity.MerchantConfig;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantConfigRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.util.SignBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用huobanplus推送商品和订单
 *
 * @author CJ
 */
@Service
public class ConnectMallServiceImpl implements ConnectMallService {

    private static final Log log = LogFactory.getLog(ConnectMallServiceImpl.class);
    private final Merchant merchant;
    private final MerchantConfig merchantConfig;
    //商城后台设置自己的secretKey
    private final String secretKey;
    //商家key
    private final String appKey;
    //商家token
    private final String token;
    //商城域名
    private final String mallDomain;
    private final String uri = "/MallApi/{0}/{1}";


    @Autowired
    private GoodsRestRepository goodsRestRepository;
    @Autowired
    private TouristGoodRepository touristGoodRepository;


    @SuppressWarnings("unused")//不能省
    @Autowired
    public ConnectMallServiceImpl(Environment environment, MerchantRestRepository merchantRestRepository
            , MerchantConfigRestRepository merchantConfigRestRepository) throws IOException {
        merchant = merchantRestRepository.getOneByPK(
                environment.getProperty("tourist.customerId", environment.acceptsProfiles("test") ? "3447" : "4886")
        );
        merchantConfig = merchant.getConfig();
        // TODO: 2017/1/17 获取key和token
        appKey = environment.getProperty("tourist.appKey", environment.acceptsProfiles("test") ? "3447" : "4886");
        token = environment.getProperty("tourist.token", environment.acceptsProfiles("test") ? "3447" : "4886");
        secretKey = environment.getProperty("tourist.secretKey", environment.acceptsProfiles("test") ? "3447" : "4886");
        mallDomain = environment.getProperty("tourist.mallDomain", environment.acceptsProfiles("test") ? "3447" : "4886");
    }

    /**
     * 把数组所有元素排序，并按照“参数参数值”的模式用字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static Map sortMap(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        Map result = new HashMap();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    @Override
    public int getExchangeRate() {
        return merchantConfig.getExchangeRate();
    }

    @Override
    public long getServiceDays() {
        return merchantConfig.getServiceDays() + merchantConfig.getReceiveDays();
    }

    @Override
    public TouristGood pushGoodToMall(long touristGoodId) throws IOException {
        TouristGood touristGood = touristGoodRepository.getOne(touristGoodId);
        if (touristGood.getMallGoodId() != null)
            return touristGood;
        //  营销类型（需要跟普通商品做出区别）
        Goods goods = new Goods();
        goods.setOwner(merchant);
        goods.setCreateTime(new Date());
        goods.setDisabled(false);
        goods.setMarketable(true);
        goods.setTitle(touristGood.getTouristName());
        goods = goodsRestRepository.insert(goods);
        log.debug("new Goods:" + goods);
        touristGood.setMallGoodId(goods.getId());
        return touristGood;
    }

    @Override
    public boolean statusNormal(TouristOrder order) throws IOException {

        return false;
    }

    @Override
    public long getMallUserIntegralBalanceCoffers(Long mallUserId, int accountType) {
        return 0;
    }

    @Override
    public long setMallUserIntegralBalanceCoffers(Long mallUserId, int accountType, int amount) {
        String uriAPI = String.format(mallDomain + uri, "User", "UpdateUserAccount");
        HttpPost httpPost = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("timestamp", LocalDateTimeFormatter.toStr(LocalDateTime.now())));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
            }
        } catch (IOException e) {

        }
        return 0;
    }

    @Override
    public String pushOrderToMall(TouristOrder order) throws IOException {
        Map data = new HashMap();
        data.put("payed", order.getMallBalance().intValue());
        data.put("vault", order.getMallCoffers().intValue());
        data.put("cashScore", order.getMallIntegral().intValue());
        data.put("memo", order.getRemarks());
        data.put("remark", order.getTravelers().get(0).getRemarks());
        data.put("identityCard", order.getTravelers().get(0).getIDNo());
        data.put("shipName", order.getTravelers().get(0).getName());
        data.put("shipMobile", order.getTravelers().get(0).getTelPhone());
        data.put("memberId", order.getTouristBuyer().getId());
        data.put("payType", order.getPayType());
        String uriAPI = String.format(mallDomain + uri, "Order", "createOrder");
        HttpPost httpPost = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appKey", appKey));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sign", SignBuilder.buildSign(sortMap(data), null, secretKey)));
        params.add(new BasicNameValuePair("timestamp", LocalDateTimeFormatter.toStr(LocalDateTime.now())));
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            ObjectMapper objectMapper = new ObjectMapper();
            ResultContent result = objectMapper.readValue(inputStream, ResultContent.class);
            if (result.resultCode == 2000) {
                String mallOrderId = result.getData().get("orderId").toString();
                return mallOrderId;
            } else {
                throw new IOException(result.getResultMsg());
            }
        }
        throw new IOException("同步订单出错，请稍后重试");
    }

    @Override
    public String getTouristBuyerHeadUrl(TouristBuyer buyer) {
        return "";
    }

    @Getter
    @Setter
    static class ResultContent {
        int resultCode;
        String resultMsg;
        Map data;
    }
}
