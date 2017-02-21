package com.huotu.tourist.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.huobanplus.common.entity.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2017/2/21.
 */
@Getter
@Setter
public class MallGoodsModel {
    private Long id;
    private String code;
    private String productJson;

    public MallGoodsModel(Long id, String code, String productJson) {
        this.id = id;
        this.code = code;
        this.productJson = productJson;
    }

    public static synchronized List<MallGoodsModel> toMallGoodsModels(List<Goods> goodsList) {
        List<MallGoodsModel> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Goods goods : goodsList) {
            try {
                List<Product> products = new ArrayList<>();
                for (com.huotu.huobanplus.common.entity.Product p : goods.getProducts()) {
                    products.add(new Product(p.getId(), p.getCode()));
                }
                MallGoodsModel mallGoodsModel = new MallGoodsModel(goods.getId(), goods.getCode(), objectMapper
                        .writeValueAsString(products.toArray()));
                list.add(mallGoodsModel);
            } catch (Exception ignored) {
            }
        }
        return list;
    }

    @Getter
    @Setter
    private static class Product {
        private Long id;
        private String code;

        public Product(Long id, String code) {
            this.id = id;
            this.code = code;
        }
    }


}
