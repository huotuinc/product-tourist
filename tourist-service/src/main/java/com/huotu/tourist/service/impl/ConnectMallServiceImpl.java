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
import com.huotu.huobanplus.common.entity.GoodsImage;
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.common.entity.MerchantConfig;
import com.huotu.huobanplus.common.entity.Product;
import com.huotu.huobanplus.model.type.MallEmbedResource;
import com.huotu.huobanplus.sdk.common.repository.GoodsImageRestRepository;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantConfigRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
import com.huotu.huobanplus.sdk.common.repository.ProductRestRepository;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.entity.SystemString;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.exception.NotLoginYetException;
import com.huotu.tourist.repository.SystemStringRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.util.SignBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private final String appKey;
    //商家token
    private final String token;
    //商城域名
    private final String mallDomain;
    private final String uri = "/MallApi/{0}/{1}";
    /**
     * 用于解密HTS1
     */
    private final SecretKey key;

    @Autowired
    private GoodsRestRepository goodsRestRepository;
    @Autowired
    private TouristGoodRepository touristGoodRepository;
    @Autowired
    private ProductRestRepository productRestRepository;
    @Autowired
    private GoodsImageRestRepository goodsImageRestRepository;

    @Autowired
    private SystemStringRepository systemStringRepository;
    @Autowired
    private Environment environment;

    @SuppressWarnings("unused")//不能省
    @Autowired
    public ConnectMallServiceImpl(Environment environment, MerchantRestRepository merchantRestRepository
            , MerchantConfigRestRepository merchantConfigRestRepository, SystemStringRepository
                                          systemStringRepository, GoodsRestRepository goodsRestRepository,
                                  ProductRestRepository productRestRepository) throws
            IOException, NoSuchAlgorithmException, DecoderException, InvalidKeyException, InvalidKeySpecException {
        String keyCode = environment.getProperty("tourist.mall.des.key", "0102030405060708");
        key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(Hex.decodeHex(keyCode.toCharArray())));
        merchant = merchantRestRepository.getOneByPK(
                environment.getProperty("tourist.customerId", environment.acceptsProfiles("test") ? "3447" : "4886")
        );
        merchantConfig = merchant.getConfig();
        // TODO: 2017/1/17 获取key和token
        appKey = environment.getProperty("tourist.appKey", environment.acceptsProfiles("test") ? "3447" : "4886");
        token = environment.getProperty("tourist.token", environment.acceptsProfiles("test") ? "3447" : "4886");
        secretKey = environment.getProperty("tourist.secretKey", environment.acceptsProfiles("test") ? "3447" : "4886");
        mallDomain = environment.getProperty("tourist.mallDomain", environment.acceptsProfiles("test") ? "3447" : "4886");

        SystemString qualificationsProductIdSystem = systemStringRepository.findOne("QualificationsProductId");
        if (qualificationsProductIdSystem == null) {
            Goods goods = new Goods();
            goods.setOwner(merchant);
            goods.setCreateTime(new Date());
            goods.setDisabled(false);
            goods.setMarketable(true);
            goods.setTitle("采购商资格产品");
            goods.setPrice(100);
            goods.setCode(UUID.randomUUID().toString().replace("-", ""));
            goods.setGoodsType("行装线路");
            goods.setDescription("行装线路商品");
            goods = goodsRestRepository.insert(goods);
            Product product = new Product();
            product.setName("线路默认");
            product.setMarketable(true);
            product.setMerchant(merchant);
            product.setPrice(100);
            product.setGoods(goods);
            product.setCode(new Date().toString());
            product = productRestRepository.insert(product);
            qualificationsProductId = "" + product.getId();
            qualificationsProductIdSystem = new SystemString();
            qualificationsProductIdSystem.setId("QualificationsProductId");
            qualificationsProductIdSystem.setValue(qualificationsProductId);
            systemStringRepository.save(qualificationsProductIdSystem);
        } else {
            qualificationsProductId = qualificationsProductIdSystem.getValue();
        }

    }

    /**
     * 把数组所有元素排序，并按照“参数参数值”的模式用字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static Map<String, Object> sortMap(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    @PreDestroy
    public void destroy() throws IOException {
        // 在单元测试中 应该清理掉脏数据
        if (environment.acceptsProfiles("unit_test")) {
            Product product = productRestRepository.getOneByPK(qualificationsProductId);
            final Goods goods = product.getGoods();
            productRestRepository.delete(product);
            goodsRestRepository.delete(goods);
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
        goods.setPrice(touristGood.getPrice().doubleValue());
        goods.setCode(UUID.randomUUID().toString().replace("-", ""));
        goods.setGoodsType("行装线路");
        goods.setCost(touristGood.getPrice().doubleValue());
        goods.setDescription("行装线路商品");
        goods = goodsRestRepository.insert(goods);
        log.debug("new Goods:" + goods);

        Product product = new Product();
        product.setName("线路默认");
        product.setMarketable(true);
        product.setMerchant(merchant);
        product.setPrice(touristGood.getPrice().doubleValue());
        product.setGoods(goods);
        product.setCode("" + touristGood.getId() + new Date().toString());
        product = productRestRepository.insert(product);

        GoodsImage goodsImage = new GoodsImage();
        goodsImage.setOrderBy(1);
        goodsImage.setOriginHeight(1);
        goodsImage.setOriginWidth(1);
        goodsImage.setRemote(true);
        goodsImage.setSource(touristGood.getTouristImgUri());
        MallEmbedResource mallEmbedResource = new MallEmbedResource();
        mallEmbedResource.setValue(touristGood.getTouristImgUri());
        goodsImage.setThumbnailPic(mallEmbedResource);
        goodsImage.setSmallPic(mallEmbedResource);
        goodsImage.setBigPic(mallEmbedResource);
        goodsImage = goodsImageRestRepository.insert(goodsImage);
        goodsImage.setGoods(goods);
        List<GoodsImage> list = new ArrayList<>();
        list.add(goodsImage);
        touristGood.setMallGoodId(goods.getId());
        return touristGood;
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
        Map data = new HashMap();
        data.put("userId", mallUserId);
        String uriAPI = String.format(mallDomain + uri, "User", "getUserDetailByUserId");
        HttpPost httpPost = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appKey", appKey));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sign", SignBuilder.buildSign(sortMap(data), null, secretKey)));
        params.add(new BasicNameValuePair("timestamp", LocalDateTimeFormatter.toStr(LocalDateTime.now())));
        try (CloseableHttpClient client = newHttpClient()) {
            httpPost.setEntity(EntityBuilder.create()
                    .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                    .setContentEncoding("UTF-8")
                    .setParameters(params)
                    .build()
            );
            ResultContent result = client.execute(httpPost, new ResultContentResponseHandler());
            if (result.resultCode == 2000) {
                return result.getData();
            } else {
                throw new IOException(result.getResultMsg());
            }
        }

    }

    @Override
    public String pushBuyerOrderToMall(TouristBuyer buyer) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("payed", 0);
        data.put("vault", 0);
        data.put("cashScore", 0);
        data.put("memo", buyer.getBuyerName() + "采购订单");
        data.put("remark", buyer.getBuyerName() + "采购订单");
        data.put("identityCard", buyer.getIDNo());
        data.put("shipName", buyer.getBuyerName());
        data.put("shipMobile", buyer.getTelPhone());
        data.put("memberId", buyer.getId());
        data.put("payType", buyer.getPayType().getCode());
        SystemString systemString = systemStringRepository.getOne("QualificationsProductId");
        List<Map> list = new ArrayList<>();
        Map pro = new HashMap();
        pro.put("bn", systemString.getValue());
        pro.put("num", 1);
        list.add(pro);
        data.put("orderItems", list);
        return pushOrder(data);
    }

    @Override
    public String pushOrderToMall(TouristOrder order) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("payed", order.getMallBalance().intValue());
        data.put("vault", order.getMallCoffers().intValue());
        data.put("cashScore", order.getMallIntegral().intValue());
        data.put("memo", order.getRemarks());
        data.put("remark", order.getTravelers().get(0).getRemarks());
        data.put("identityCard", order.getTravelers().get(0).getIDNo());
        data.put("shipName", order.getTravelers().get(0).getName());
        data.put("shipMobile", order.getTravelers().get(0).getTelPhone());
        data.put("memberId", order.getTouristBuyer().getId());
        data.put("payType", order.getPayType().getCode());
        List<Map> list = new ArrayList<>();
        List<Product> products = productRestRepository.findByGoodsPK(order.getTouristGood().getMallGoodId());
        for (Product p : products) {
            Map pro = new HashMap();
            pro.put("bn", p.getCode());
            pro.put("num", order.getTravelers().size());
            list.add(pro);
            break;
        }
        data.put("orderItems", list);
        return pushOrder(data);
    }

    private String pushOrder(Map<String, Object> data) throws IOException {
        String uriAPI = String.format(mallDomain + uri, "Order", "createOrder");
        HttpPost httpPost = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appKey", appKey));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sign", SignBuilder.buildSign(sortMap(data), null, secretKey)));
        params.add(new BasicNameValuePair("timestamp", LocalDateTimeFormatter.toStr(LocalDateTime.now())));
        try (CloseableHttpClient client = newHttpClient()) {
            httpPost.setEntity(EntityBuilder.create()
                    .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                    .setContentEncoding("UTF-8")
                    .setParameters(params)
                    .build()
            );
            ResultContent result = client.execute(httpPost, new ResultContentResponseHandler());
            if (result.resultCode == 2000) {
                return result.getData().get("orderId").toString();
            } else {
                throw new IOException(result.getResultMsg());
            }
        }
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
                    .filter(cookie -> cookie.getName().equalsIgnoreCase("UserID_HTS1"))
                    .findAny()
                    .orElseThrow(NotLoginYetException::new).getValue();
            byte[] encryptData = Hex.decodeHex(cookieValue.toCharArray());

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] data = cipher.doFinal(encryptData);
            return new DataInputStream(new ByteArrayInputStream(data)).readLong();
        } catch (Exception ex) {
            throw new NotLoginYetException(ex);
        }
    }

    @Getter
    @Setter
    private static class ResultContent {
        int resultCode;
        String resultMsg;
        Map data;
    }

    private class ResultContentResponseHandler extends AbstractResponseHandler<ConnectMallServiceImpl.ResultContent> {
        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public ConnectMallServiceImpl.ResultContent handleEntity(HttpEntity entity) throws IOException {
            return objectMapper.readValue(entity.getContent(), ConnectMallServiceImpl.ResultContent.class);
        }
    }


}
