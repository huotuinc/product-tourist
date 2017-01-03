/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.util;

import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author CJ
 */
public class ImageHelper {

    /**
     * 以指定格式保存一张图片
     *
     * @param type            类型 比如png,jpg
     * @param resourceService 资源服务
     * @param data            原数据
     * @return 资源path
     */
    public static String storeAsImage(String type, ResourceService resourceService, InputStream data)
            throws IOException {
        String path = UUID.randomUUID().toString() + "." + type;
        storeAsImage(type, resourceService, data, path);
        return path;
    }

    /**
     * 以指定格式保存一张图片,并且保存到指定path
     *
     * @param type            类型 比如png,jpg
     * @param resourceService 资源服务
     * @param data            原数据
     * @param path            资源系统的路径
     */
    public static void storeAsImage(String type, ResourceService resourceService, InputStream data, String path) throws IOException {
        try {
            BufferedImage image = ImageIO.read(data);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(image, type, buffer);
            resourceService.uploadResource(path, new ByteArrayInputStream(buffer.toByteArray()));
        } finally {
            //noinspection ThrowFromFinallyBlock
            data.close();
        }
    }

    public static void assertSame(Resource resource, Resource resource1) {
        try {
            BufferedImage image1 = ImageIO.read(resource.getInputStream());

            BufferedImage image2 = ImageIO.read(resource1.getInputStream());
            assert image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
    }
}
