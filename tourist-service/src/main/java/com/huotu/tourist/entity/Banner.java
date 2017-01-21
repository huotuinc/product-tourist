package com.huotu.tourist.entity;

import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    public static final List<Selection<PresentRecord, ?>> selections = Arrays.asList(
            new SimpleSelection<PresentRecord, String>("id", "id")
            , new SimpleSelection<PresentRecord, String>("bannerName", "bannerName")
            , new SimpleSelection<PresentRecord, String>("linkUrl", "linkUrl")
            , new SimpleSelection<PresentRecord, String>("bannerImgUri", "createTime")
    );

    @Column(length = 100)
    private String bannerName;

    @Column(length = 200)
    private String bannerImgUri;

    @Column(length = 200)
    private String linkUrl;


}
