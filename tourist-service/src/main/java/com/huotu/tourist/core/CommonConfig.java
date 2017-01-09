/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.core;

import com.huotu.huobanplus.sdk.common.CommonClientSpringConfig;
import com.huotu.tourist.Version;
import com.huotu.tourist.entity.SystemString;
import com.huotu.tourist.repository.SystemStringRepository;
import me.jiangcai.lib.jdbc.JdbcSpringConfig;
import me.jiangcai.lib.resource.ResourceSpringConfig;
import me.jiangcai.lib.spring.logging.LoggingConfig;
import me.jiangcai.lib.upgrade.UpgradeSpringConfig;
import me.jiangcai.lib.upgrade.VersionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 核心服务 加载者
 *
 * @author CJ
 */
@Configuration
@Import({ResourceSpringConfig.class, UpgradeSpringConfig.class, JdbcSpringConfig.class, LoggingConfig.class
        , CommonClientSpringConfig.class})
class CommonConfig {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SystemStringRepository systemStringRepository;

    @Bean
    @SuppressWarnings("unchecked")
    public VersionInfoService versionInfoService() {
        final String versionKey = "version.database";
        return new VersionInfoService() {

            @Override
            public <T extends Enum> T currentVersion(Class<T> type) {
                SystemString systemString = systemStringRepository.findOne(versionKey);
                if (systemString == null)
                    return null;
                return (T) Version.valueOf(systemString.getValue());
            }

            @Override
            public <T extends Enum> void updateVersion(T currentVersion) {
                SystemString systemString = systemStringRepository.findOne(versionKey);
                if (systemString == null) {
                    systemString = new SystemString();
                    systemString.setId(versionKey);
                }
                systemString.setValue(currentVersion.name());
                systemStringRepository.save(systemString);
            }
        };
    }


}
