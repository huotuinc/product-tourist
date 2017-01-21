/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.huobanplus.common.entity.Product;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.ProductRestRepository;
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
    private GoodsRestRepository goodsRestRepository;
    @Autowired
    private ProductRestRepository productRestRepository;

    @Test
    public void newCookie() throws NotLoginYetException {
        assertCookie("FT2UE1TgbNAKuZNu1M0nkA==", 9527);
        assertCookie("iccr3VoVgMJfYuFNl6XPWA==", 1057597);
    }

    private void assertCookie(String encryptCode, long userId) throws NotLoginYetException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("mem_authcode", encryptCode));

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
        TouristGood good = createRandomTouristGood();

        good = connectMallService.pushGoodToMall(good.getId());
        try {
            //小伙伴id
            TouristBuyer buyer = createRandomTouristBuyer(18767101124L);
            Map map = connectMallService.getUserDetailByUserId(buyer.getId());
            assertThat(map).isNotEmpty();
            TouristOrder touristOrder = createRandomTouristOrder(good, buyer);
            String mallOrderId = connectMallService.pushOrderToMall(touristOrder);
            map = connectMallService.orderDetail(mallOrderId);
            assertThat(map).isNotEmpty();

            touristOrder.setMallOrderNo(mallOrderId);
            System.out.println(goodsRestRepository.getOneByPK(good.getMallGoodId()).getOwner().getNickName());

            String buyerOrder = connectMallService.pushBuyerOrderToMall(buyer);
            map = connectMallService.orderDetail(buyerOrder);
            assertThat(map).isNotEmpty();
        } finally {
            for (Product product : productRestRepository.findByGoodsPK(good.getMallGoodId())) {
                productRestRepository.delete(product);
            }
            goodsRestRepository.deleteByPK(good.getMallGoodId());
        }

    }


}