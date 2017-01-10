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
import com.huotu.huobanplus.common.entity.Merchant;
import com.huotu.huobanplus.sdk.common.repository.GoodsRestRepository;
import com.huotu.huobanplus.sdk.common.repository.MerchantRestRepository;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.service.ConnectMallService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * 使用huobanplus推送商品和订单
 *
 * @author CJ
 */
@Service
public class ConnectMallServiceImpl implements ConnectMallService {

    private static final Log log = LogFactory.getLog(ConnectMallServiceImpl.class);
    private final Merchant merchant;

    @Autowired
    private GoodsRestRepository goodsRestRepository;
    @Autowired
    private TouristGoodRepository touristGoodRepository;

    @Autowired
    public ConnectMallServiceImpl(Environment environment, MerchantRestRepository merchantRestRepository) throws IOException {
        merchant = merchantRestRepository.getOneByPK(
                environment.getProperty("tourist.customerId", environment.acceptsProfiles("develop") ? "3447" : "4886")
        );
    }

    @Override
    public TouristGood pushGoodToMall(long touristGoodId) throws IOException {
        TouristGood touristGood = touristGoodRepository.getOne(touristGoodId);
        if (touristGood.getMallGoodId() != null)
            return touristGood;
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
}
