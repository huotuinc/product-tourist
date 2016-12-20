/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.converter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lhx on 2016/12/16
 */
@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {


    /**
     * 字符串转换成LocalDateTime
     *
     * @param text yyyy-MM-dd hh:mm:ss
     * @return
     * @throws ParseException 转换异常
     */
    public static LocalDateTime toLocalDateTime(String text) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(text);
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    /**
     * 日期转换为字符串
     *
     * @param dateTime
     * @return yyyy-MM-dd hh:mm:ss 格式字符
     */
    public static String toStr(LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(text);
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        Instant instant = object.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }
}
