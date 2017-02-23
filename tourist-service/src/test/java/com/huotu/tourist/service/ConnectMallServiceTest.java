/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.ProductRestRepository;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.exception.NotLoginYetException;
import me.jiangcai.dating.ServiceBaseTest;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Repeat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.Cookie;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class ConnectMallServiceTest extends ServiceBaseTest {

    @Autowired
    private ConnectMallService connectMallService;
    @Autowired
    private ProductRestRepository productRestRepository;
    @Autowired
    private Environment environment;
    @Autowired
    private GoodsRestRepository goodsRestRepository;


    @Test
    public void newCookie() throws NotLoginYetException, UnsupportedEncodingException {
        assertCookie("FT2UE1TgbNAKuZNu1M0nkA==", 9527);
        assertCookie("iccr3VoVgMJfYuFNl6XPWA==", 1057597);
    }

    private void assertCookie(String encryptCode, long userId) throws NotLoginYetException, UnsupportedEncodingException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("mem_authcode", URLEncoder.encode(encryptCode, "UTF-8")));

//        http://www.cnblogs.com/lzrabbit/p/3639503.html
//        就用这个吧，取出来，先URLDecoder.decode，再解密
//        cookie的名字叫 mem_authcode

        assertThat(connectMallService.currentUserId(mockHttpServletRequest))
                .isEqualTo(userId);
    }

    //已放弃
    @Ignore
    @Test
    @Repeat(5)
    public void cookie() throws NoSuchAlgorithmException, DecoderException, InvalidKeyException
            , InvalidKeySpecException, IOException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NotLoginYetException {
        final String keyCode = "0102030405060708";
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(Hex.decodeHex(keyCode.toCharArray())));

        long userId = Math.abs(random.nextLong());

        //描述成为byte
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(buffer);
        dataOutputStream.writeLong(userId);
        dataOutputStream.flush();
        dataOutputStream.close();

        //加密它们
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        String encryptString = Hex.encodeHexString(cipher.doFinal(buffer.toByteArray()));

        System.out.println(userId);
        System.out.println(encryptString);

        // 模拟一个cookie
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("UserID_HTS1", encryptString));

        assertThat(connectMallService.currentUserId(mockHttpServletRequest))
                .isEqualTo(userId);

    }

    @Test
    public void go() throws IOException {

        System.out.println(connectMallService.getExchangeRate());
        assertThat(connectMallService.getExchangeRate())
                .isGreaterThanOrEqualTo(0);
        System.out.println(connectMallService.getServiceDays());
        assertThat(connectMallService.getServiceDays())
                .isGreaterThanOrEqualTo(0);
        TouristGood good = createRandomTouristGood(22662L);
        try {
            //小伙伴id
            TouristBuyer buyer = createRandomTouristBuyer(256421L);
            Map map = connectMallService.getUserDetailByUserId(buyer.getId());
            assertThat(map).isNotEmpty();
            TouristOrder touristOrder = createRandomTouristOrder(good, buyer);
            String mallOrderId = connectMallService.pushOrderToMall(touristOrder);
//            map = connectMallService.orderDetail(mallOrderId);
//            assertThat(map).isNotEmpty();

            touristOrder.setMallOrderNo(mallOrderId);
            productRestRepository.findByGoodsPK(21097L);

            String buyerOrder = connectMallService.pushBuyerOrderToMall(buyer);
//            map = connectMallService.orderDetail(buyerOrder);
//            assertThat(map).isNotEmpty();
        } finally {
            if (environment.acceptsProfiles(TouristGoodService.Mall_Resource_Create_Profile))
                productRestRepository.deleteByPK(good.getMallProductId().toString());
        }
    }

    @Test
    public void testCreateOrder() throws IOException {
        TouristGood good = createRandomTouristGood(22662L);
        TouristBuyer buyer = createRandomTouristBuyer(256421L);
        TouristOrder touristOrder = createRandomTouristOrder(good, buyer);
        String mallOrderId = connectMallService.pushOrderToMall(touristOrder);
        System.out.println(mallOrderId);
    }

    @Test
    public void ss() {
        PayTypeEnum payType = PayTypeEnum.valueOf("Alipay");
        PayTypeEnum payType2 = PayTypeEnum.valueOf("0");
    }

    @Test
    public void sss() {
        //小伙伴id
        TouristBuyer buyer = createRandomTouristBuyer(256421L);
        String url = connectMallService.getTouristBuyerHeadUrl(buyer);
        System.out.println(url);
    }

    @Test
    public void ssss() throws IOException {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
        Merchant merchant = connectMallService.getMerchant();
        Page<Goods> goodsPage = goodsRestRepository.searchMarketableByMerchantAndScenes(merchant, 7, pageable);
    }

    @Test
    public void saveBuyerCommissionTest() throws IOException{
        connectMallService.saveBuyerCommission(16012L,100.0,"12346");
    }


}