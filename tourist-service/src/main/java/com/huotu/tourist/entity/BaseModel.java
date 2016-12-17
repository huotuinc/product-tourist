package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Column
    private LocalDateTime updateTime;
    /**
     * 是否已删除
     */
    @Column
    private boolean deleted;

}
