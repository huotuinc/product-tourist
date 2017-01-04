package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.CollectionAccountTypeEnum;
import com.huotu.tourist.entity.CollectionAccount;
import com.huotu.tourist.repository.CollectionAccountRepository;
import com.huotu.tourist.service.CollectionAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lhx on 2017/1/4.
 */
@Service
public class CollectionAccountServiceImpl implements CollectionAccountService {
    @Autowired
    CollectionAccountRepository collectionAccountRepository;

    @Override
    public CollectionAccount saveCollectionAccount(Long id, CollectionAccountTypeEnum accountType, String IDCard
            , String aliPayName, String aliPayAccount, String accountName, String bank, String bankBranch, String bankCard) {
        CollectionAccount collectionAccount;
        if (id == null) {
            collectionAccount = new CollectionAccount();
        } else {
            collectionAccount = collectionAccountRepository.getOne(id);
        }
        collectionAccount.setAccountType(accountType);
        collectionAccount.setIDCard(IDCard);
        collectionAccount.setAliPayName(aliPayName);
        collectionAccount.setAliPayAccount(aliPayAccount);
        collectionAccount.setAccountName(accountName);
        collectionAccount.setBank(bank);
        collectionAccount.setBankBranch(bankBranch);
        collectionAccount.setBankCard(bankCard);
        return collectionAccountRepository.saveAndFlush(collectionAccount);
    }
}
