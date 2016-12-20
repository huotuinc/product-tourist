/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.model;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 在业务层通常我们会获取{@link org.springframework.data.domain.Page}
 * 实际视图显示需求则通常表现为Selection
 *
 * @author CJ
 */
public class PageAndSelection<T> {

    private final Page<T> page;
    private final List<Selection<T, ?>> selectionList;

    public PageAndSelection(Page<T> page, List<Selection<T, ?>> selectionList) {
        this.page = page;
        this.selectionList = selectionList;
    }
}
