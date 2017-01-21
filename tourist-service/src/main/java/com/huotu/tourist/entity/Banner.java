package com.huotu.tourist.entity;

import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.lib.resource.service.ResourceService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 首页banner
 * Created by lhx on 2016/12/19.
 */
@Entity
@Table(name = "Banner")
@Getter
@Setter
public class Banner extends BaseModel {

    @Column(length = 100)
    private String bannerName;
    @Column(length = 200)
    private String bannerImgUri;
    @Column(length = 200)
    private String linkUrl;

    public static final List<Selection<Banner, ?>> getDefaultSelections(ResourceService resourceService) {
        return Arrays.asList(
                new SimpleSelection<Banner, Long>("id", "id")
                , new SimpleSelection<Banner, String>("bannerName", "bannerName")
                , new SimpleSelection<Banner, String>("linkUrl", "linkUrl")
                , new Selection<Banner, String>() {
                    @Override
                    public String apply(Banner banner) {
                        try {
                            return resourceService.getResource(banner.getBannerImgUri()).httpUrl().toString();
                        } catch (IOException e) {
                            throw new RuntimeException("" + e);
                        }
                    }

                    @Override
                    public String getName() {
                        return "bannerImgUri";
                    }
                }
        );
    }


}
