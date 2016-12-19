package com.huotu.tourist.service;

import java.io.Serializable;

/**
 * Created by lhx on 2016/12/19.
 */

public interface BaseService<T, ID extends Serializable> {
    /**
     * 创建数据
     *
     * @param data 数据 not null
     * @return 创建的对象
     */
    T create(T data);

    /**
     * 更新数据
     *
     * @param data 实体 not null
     * @return 更新后的对象
     */
    T update(T data);

    /**
     * 获取数据
     *
     * @param id not null
     * @return 存在该id返回对应实体，否则返回null
     */
    T getOne(ID id);

    void delete(ID id);


}
