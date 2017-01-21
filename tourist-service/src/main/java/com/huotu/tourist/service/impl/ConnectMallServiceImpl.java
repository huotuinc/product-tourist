/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.impl;

import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.common.entity.MerchantConfig;
import com.huotu.huobanplus.common.entity.Product;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantConfigRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
import com.huotu.huobanplus.sdk.common.repository.ProductRestRepository;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.exception.NotLoginYetException;
import com.huotu.tourist.repository.SystemStringRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.mall.ContentResolver;
import com.huotu.tourist.service.mall.ResultContent;
import com.huotu.tourist.service.mall.ResultContentResponseHandler;
import com.huotu.tourist.util.SignBuilder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * 使用huobanplus推送商品和订单
 *
 * @author CJ
 */
@Service
public class ConnectMallServiceImpl implements ConnectMallService {

    private static final Log log = LogFactory.getLog(ConnectMallServiceImpl.class);
    /**
     * 线路运行商户--行装
     */
    private final Merchant merchant;
    /**
     * 商户配置--行装配置
     */
    private final MerchantConfig merchantConfig;
    /**
     * 小伙伴升级成为认证采购商必须购买的货品
     */
    private final String qualificationsProductId;
    //商城后台设置自己的secretKey
    private final String secretKey;
    //商家key
    private final String appId;
    //商家token
    private final String token;
    //商城域名
    private final String mallDomain;
//    tourist.appId=tcxe6l3447
//    tourist.token=8662452542f243fc8c26ebea86aeb4
//    tourist.secretKey=1j68kk79
//    tourist.mallDomain = mallapiv2.51flashmall.com/MallApi/Order/Create
//    tourist.qualificationsProductId=22659
//    小伙伴：256421
//    线路货品id:22662

    /**
     * 用于解密HTS1
     */
    private final SecretKey key;
    private String uri = "/MallApi/{0}/{1}";
    @Autowired
    private ProductRestRepository productRestRepository;

    @Autowired
    private Environment environment;

    @SuppressWarnings("unused")//不能省
    @Autowired
    public ConnectMallServiceImpl(Environment environment, MerchantRestRepository merchantRestRepository
            , MerchantConfigRestRepository merchantConfigRestRepository, SystemStringRepository
                                          systemStringRepository, GoodsRestRepository goodsRestRepository,
                                  ProductRestRepository productRestRepository) throws
            IOException, NoSuchAlgorithmException, DecoderException, InvalidKeyException, InvalidKeySpecException {
        String keyCode = environment.getProperty("tourist.mall.des.key", "XjvDhKLvCsm9y7G7");
        key = new SecretKeySpec(keyCode.getBytes("ASCII"), "AES");
        merchant = merchantRestRepository.getOneByPK(
                environment.getProperty("tourist.customerId", environment.acceptsProfiles("test") ? "3447" : "4886")
        );
        merchantConfig = merchant.getConfig();
        appId = environment.getProperty("tourist.appId"
                , environment.acceptsProfiles("test") ? "tcxe6l3447" : null);
        token = environment.getProperty("tourist.token"
                , environment.acceptsProfiles("test") ? "8662452542f243fc8c26ebea86aeb4" : null);
        secretKey = environment.getProperty("tourist.secretKey"
                , environment.acceptsProfiles("test") ? "1j68kk79" : null);
        mallDomain = environment.getProperty("tourist.mallDomain"
                , environment.acceptsProfiles("test") ? "http://api.pdmall.com" : null);
        qualificationsProductId = environment.getProperty("tourist.qualificationsProductId"
                , environment.acceptsProfiles("test") ? "22659" : null);
        log.info("appId=" + appId);
        log.info("token=" + token);
        log.info("secretKey=" + secretKey);
        log.info("mallDomain=" + mallDomain);
        log.info("qualificationsProductId=" + qualificationsProductId);
        if (appId == null || token == null || secretKey == null || mallDomain == null || qualificationsProductId == null) {
            throw new IOException("缺少系统配置，请检查配置");
        }
    }

    private <T> T executeMallAPI(String apiSub, String apiName, ContentResolver<T> resolver, NameValuePair... parameters) throws IOException {
        String uriAPI = MessageFormat.format(mallDomain + uri, apiSub, apiName);
        final String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, Object> logicParameters = new HashMap<>();
        for (NameValuePair pair : parameters) {
            if (!StringUtils.isEmpty(pair.getValue())) {
                logicParameters.put(pair.getName(), pair.getValue());
            }
        }
        logicParameters.put("appId", appId);
        logicParameters.put("token", token);
        logicParameters.put("timestamp", timestamp);
        String sign = SignBuilder.buildSign(new TreeMap<>(logicParameters), null, secretKey);

        List<NameValuePair> toPost = new ArrayList<>(Arrays.asList(parameters));
        toPost.add(new BasicNameValuePair("appId", appId));
        toPost.add(new BasicNameValuePair("token", token));
        toPost.add(new BasicNameValuePair("timestamp", timestamp));
        toPost.add(new BasicNameValuePair("sign", sign));

        try (CloseableHttpClient client = newHttpClient()) {
            HttpPost httpPost = new HttpPost(uriAPI);
            httpPost.setEntity(EntityBuilder.create()
                    .setContentType(ContentType.create("application/x-www-form-urlencoded", Charset.forName("UTF-8")))
                    .setContentEncoding("utf-8")
                    .setParameters(toPost)
                    .build()
            );
            return client.execute(httpPost, new ResultContentResponseHandler<>(resolver));
        }
    }

    private CloseableHttpClient newHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build())
                .build();
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
    public boolean statusNormal(TouristOrder order) throws IOException {
        return false;
    }

    @Override
    public boolean statusNormal(String mallTradeId) throws IOException {
        return false;
    }

    @Override
    public Map getUserDetailByUserId(Long mallUserId) throws IOException {
        return executeMallAPI("User", "getUserDetailByUserId", ResultContent::getData
                , new BasicNameValuePair("userId", String.valueOf(mallUserId)));
    }

    @Override
    public String pushBuyerOrderToMall(TouristBuyer buyer) throws IOException {
        NameValuePair payed = new BasicNameValuePair("payed", "0");
        NameValuePair vault = new BasicNameValuePair("vault", "0");
        NameValuePair cashScore = new BasicNameValuePair("cashScore", "0");
        NameValuePair memo = new BasicNameValuePair("memo", buyer.getBuyerName() + "采购订单");
        NameValuePair remark = new BasicNameValuePair("remark", buyer.getBuyerName() + "采购订单");
        NameValuePair identityCard = new BasicNameValuePair("identityCard", buyer.getIDNo());
        NameValuePair shipName = new BasicNameValuePair("shipName", buyer.getBuyerName());
        NameValuePair shipMobile = new BasicNameValuePair("shipMobile", buyer.getTelPhone());
        NameValuePair memberId = new BasicNameValuePair("memberId", buyer.getId() + "");
        NameValuePair payType = new BasicNameValuePair("payType", buyer.getPayType().getCode() + "");
        Product product = productRestRepository.getOneByPK(qualificationsProductId);
        String orderItem = product.getGoods().getId() + "_" + qualificationsProductId + "_" + 1;

        NameValuePair orderItems = new BasicNameValuePair("orderItems", orderItem);
        return (String) executeMallAPI("Order", "Create", content -> content.getData().get("orderId")
                , payed, vault, cashScore, memo, remark, identityCard, shipName, shipMobile, memberId, payType, orderItems);
    }

    @Override
    public String pushOrderToMall(TouristOrder order) throws IOException {
        NameValuePair payed = new BasicNameValuePair("payed", order.getMallBalance().intValue() + "");
        NameValuePair vault = new BasicNameValuePair("vault", order.getMallCoffers().intValue() + "");
        NameValuePair cashScore = new BasicNameValuePair("cashScore", order.getMallIntegral().intValue() + "");
        NameValuePair memo = new BasicNameValuePair("memo", order.getRemarks());
        NameValuePair remark = new BasicNameValuePair("remark", order.getTravelers().get(0).getRemarks());
        NameValuePair identityCard = new BasicNameValuePair("identityCard", order.getTravelers().get(0).getNumber());
        NameValuePair shipName = new BasicNameValuePair("shipName", order.getTravelers().get(0).getName());
        NameValuePair shipMobile = new BasicNameValuePair("shipMobile", order.getTravelers().get(0).getTelPhone());
        NameValuePair memberId = new BasicNameValuePair("memberId", order.getTouristBuyer().getId() + "");
        NameValuePair payType = new BasicNameValuePair("payType", order.getPayType().getCode() + "");
        Product product = productRestRepository.getOneByPK(order.getTouristGood().getMallProductId());
        String item = product.getGoods().getId() + "_" + order.getTouristGood().getMallProductId() + "_" + order.getTravelers()
                .size();
        NameValuePair orderItems = new BasicNameValuePair("orderItems", item);
        return (String) executeMallAPI("Order", "Create", content -> content.getData().get("orderId")
                , payed, vault, cashScore, memo, remark, identityCard, shipName, shipMobile, memberId, payType, orderItems);
    }

    @Override
    public Map orderDetail(String mallOrderId) throws IOException {
        return executeMallAPI("Order", "Detail", ResultContent::getData
                , new BasicNameValuePair("orderId", String.valueOf(mallOrderId)));
    }


    @Override
    public String getTouristBuyerHeadUrl(TouristBuyer buyer) {
        try {
            Map data = getUserDetailByUserId(buyer.getId());
            return data.get("headUrl").toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Override
    public long currentUserId(HttpServletRequest request) throws NotLoginYetException {
        try {
            String cookieValue = Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equalsIgnoreCase("mem_authcode"))
                    .findAny()
                    .orElseThrow(NotLoginYetException::new).getValue();
            byte[] encryptData = Base64.getDecoder().decode(URLDecoder.decode(cookieValue, "UTF-8"));

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] data = cipher.doFinal(encryptData);
            return Long.parseLong(new String(data));
        } catch (Exception ex) {
            throw new NotLoginYetException(ex);
        }
    }

    @Override
    public Merchant getMerchant() {
        return this.merchant;
    }
}
