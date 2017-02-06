/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "base_model")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Column(columnDefinition = "datetime")
    private LocalDateTime updateTime;

    /**
     * 是否已删除
     */
    @Column(insertable = false)
    private boolean deleted;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseModel model = (BaseModel) o;
//        if (getId() != null)
//            return Objects.equals(getId(), model.getId());
        return Objects.equals(id, model.id) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(deleted, model.deleted);
    }

    @Override
    public int hashCode() {
//        if (getId() != null)
//            return Objects.hash(getId());
        return Objects.hash(id, createTime, updateTime, deleted);
    }

}
