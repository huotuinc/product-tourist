/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.impl;

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
import com.huotu.tourist.entity.SystemString;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.exception.NotLoginYetException;
import com.huotu.tourist.repository.SystemStringRepository;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.service.ConnectMallService;
import com.huotu.tourist.service.mall.ContentResolver;
import com.huotu.tourist.service.mall.ResultContent;
import com.huotu.tourist.service.mall.ResultContentResponseHandler;
import com.huotu.tourist.util.SignBuilder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private final String appId;
    //商家token
    private final String token;
    //商城域名
    private final String mallDomain;
    /**
     * 用于解密HTS1
     */
    private final SecretKey key;
    private String uri = "/MallApi/{0}/{1}";
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
        appId = environment.getProperty("tourist.appId"
                , environment.acceptsProfiles("test") ? "9blgq84886" : "");
        token = environment.getProperty("tourist.token"
                , environment.acceptsProfiles("test") ? "de7d6b0848534667b7e4a471abf77c" : "");
        secretKey = environment.getProperty("tourist.secretKey"
                , environment.acceptsProfiles("test") ? "iav014i4" : "");
        mallDomain = environment.getProperty("tourist.mallDomain"
                , environment.acceptsProfiles("test") ? "http://api.pdmall.com" : "");
        log.info("appId=" + appId);
        log.info("token=" + token);
        log.info("secretKey=" + secretKey);
        log.info("mallDomain=" + mallDomain);
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
                    .setContentType(ContentType.APPLICATION_FORM_URLENCODED)
                    .setContentEncoding("UTF-8")
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
        product.setCode("" + touristGood.getId() + System.currentTimeMillis());
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
        NameValuePair memberId = new BasicNameValuePair("shipMobile", buyer.getId() + "");
        NameValuePair payType = new BasicNameValuePair("payType", buyer.getPayType().getCode() + "");
        SystemString systemString = systemStringRepository.getOne("QualificationsProductId");
        Product product = productRestRepository.getOneByPK(systemString.getValue());
        String orderItem = product.getGoods().getId() + "_" + product.getId() + "_" + 1;
        NameValuePair orderItems = new BasicNameValuePair("orderItems", orderItem);
        return (String) executeMallAPI("Order", "Create", content -> content.getData().get("orderId")
                , payed, vault, cashScore, memo, remark, identityCard, shipName, shipMobile, memberId, payType, orderItems);
    }

    @Override
    public String pushOrderToMall(TouristOrder order) throws IOException {
        List<Product> products = productRestRepository.findByGoodsPK(order.getTouristGood().getMallGoodId());
        String item = order.getTouristGood().getMallGoodId() + "_" + products.get(0).getId() + "_" + order.getTravelers()
                .size();

        NameValuePair payed = new BasicNameValuePair("payed", order.getMallBalance().intValue() + "");
        NameValuePair vault = new BasicNameValuePair("vault", order.getMallCoffers().intValue() + "");
        NameValuePair cashScore = new BasicNameValuePair("cashScore", order.getMallIntegral().intValue() + "");
        NameValuePair memo = new BasicNameValuePair("memo", order.getRemarks());
        NameValuePair remark = new BasicNameValuePair("remark", order.getTravelers().get(0).getRemarks());
        NameValuePair identityCard = new BasicNameValuePair("identityCard", order.getTravelers().get(0).getIDNo());
        NameValuePair shipName = new BasicNameValuePair("shipName", order.getTravelers().get(0).getName());
        NameValuePair shipMobile = new BasicNameValuePair("shipMobile", order.getTravelers().get(0).getTelPhone());
        NameValuePair memberId = new BasicNameValuePair("memberId", order.getTouristBuyer().getId() + "");
        NameValuePair payType = new BasicNameValuePair("payType", order.getPayType().getCode() + "");
        NameValuePair orderItems = new BasicNameValuePair("orderItems", item);
        return (String) executeMallAPI("Order", "Create", content -> content.getData().get("orderId")
                , payed, vault, cashScore, memo, remark, identityCard, shipName, shipMobile, memberId, payType, orderItems);
    }

    @Override
    public Map orderDetail(String mallOrderId) throws IOException {
        return executeMallAPI("Order", "orderDetail", ResultContent::getData
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

    @Override
    public Merchant getMerchant() {
        return this.merchant;
    }


}
