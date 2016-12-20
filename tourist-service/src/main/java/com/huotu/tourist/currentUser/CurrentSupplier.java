package com.huotu.tourist.currentUser;

import lombok.Getter;

/**
 * 当前是供应商身份
 * Created by slt on 2016/12/20.
 */
@Getter
public class CurrentSupplier implements CurrentUserInfo {
    private Long supplierId;

    @Override
    public Long getUserId() {
        return getSupplierId();
    }
}
