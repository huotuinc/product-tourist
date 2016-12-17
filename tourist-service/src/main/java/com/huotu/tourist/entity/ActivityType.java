package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 活动类型
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "activity_type")
@Getter
@Setter
public class ActivityType extends BaseModel {

    /**
     * 活动名称
     */
    @Column(name = "activityName", length = 200)
    private String activityName;
}
