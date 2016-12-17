package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 线路类型
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Tourist_Type")
@Getter
@Setter
public class TouristType extends BaseModel {

    /**
     * 类型名称
     */
    @Column(length = 50)
    private String typeName;
}
