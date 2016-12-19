package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
}
