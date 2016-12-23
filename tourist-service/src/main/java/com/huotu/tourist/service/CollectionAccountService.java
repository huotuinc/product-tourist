package com.huotu.tourist.service;

import com.huotu.tourist.common.CollectionAccountTypeEnum;
import com.huotu.tourist.entity.CollectionAccount;

/**
 * 收款账户服务
 * Created by slt on 2016/12/23.
 */
public interface CollectionAccountService {

    /**
     *
     * 保存收款账户(包括新增，和修改)
     * @param id                收款账户ID(null:新增，有数据：修改)
     * @param accountType       账户类型
     * @param IDCard            身份证号
     * @param aliPayName        支付宝姓名
     * @param aliPayAccount     支付宝账号
     * @param accountName       银行卡户名
     * @param bank              开户银行
     * @param bankBranch        卡户银行
     * @param bankCard          银行卡号
     * @return                  保存的收款账户
     */
    CollectionAccount saveCollectionAccount(Long id, CollectionAccountTypeEnum accountType, String IDCard, String aliPayName
            , String aliPayAccount, String accountName, String bank, String bankBranch, String bankCard);
}
