/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.exception;

/**
 * 尚未登录伙伴商城
 *
 * @author CJ
 */
public class NotLoginYetException extends Exception {
    public NotLoginYetException() {
    }

    public NotLoginYetException(String message) {
        super(message);
    }

    public NotLoginYetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoginYetException(Throwable cause) {
        super(cause);
    }
}
