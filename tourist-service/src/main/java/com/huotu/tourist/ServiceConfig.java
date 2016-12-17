package com.huotu.tourist;

import com.huotu.tourist.config.JpaConfig;
import com.huotu.tourist.config.MVCConfig;
import com.huotu.tourist.config.SecurityConfig;
import me.jiangcai.lib.jdbc.JdbcSpringConfig;
import me.jiangcai.lib.resource.ResourceSpringConfig;
import me.jiangcai.lib.upgrade.UpgradeSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lhx on 2016/12/16.
 */
@Configuration
@ComponentScan({
        "com.huotu.tourist.converter"
        , "com.huotu.tourist.service"
})
@EnableTransactionManagement
@ImportResource({"classpath:spring_dev.xml", "classpath:spring_prod.xml"})
@Import({ResourceSpringConfig.class, UpgradeSpringConfig.class
        , JdbcSpringConfig.class, JpaConfig.class, SecurityConfig.class, MVCConfig.class})
public class ServiceConfig {

}
