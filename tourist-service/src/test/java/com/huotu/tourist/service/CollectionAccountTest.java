package com.huotu.tourist.service;

import com.huotu.tourist.common.CollectionAccountTypeEnum;
import com.huotu.tourist.entity.CollectionAccount;
import com.huotu.tourist.repository.CollectionAccountRepository;
import me.jiangcai.dating.ServiceBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 账户测试
 * Created by slt on 2017/2/6.
 */
public class CollectionAccountTest extends ServiceBaseTest {

    @Autowired
    CollectionAccountRepository collectionAccountRepository;

    @Autowired
    CollectionAccountService collectionAccountService;

    @Test
    public void saveCollectionAccountTest() {
        CollectionAccount accountExp = collectionAccountService.saveCollectionAccount(null,
                CollectionAccountTypeEnum.Alipay, "18069775487", "slt", "18069774589", "史利挺", "杭州银行", "滨江支行"
                , "1111111111111111111");

        CollectionAccount accountAct = collectionAccountRepository.findOne(accountExp.getId());
        Assert.isTrue(accountExp.equals(accountAct));
        accountExp = collectionAccountService.saveCollectionAccount(accountExp.getId(), CollectionAccountTypeEnum.BankCard
                , "18098756965", "wy", "18546536987", "修改", "工商银行", "杭州支行", "2222222222222222");
        accountAct = collectionAccountRepository.findOne(accountExp.getId());
        Assert.isTrue(accountExp.equals(accountAct));
    }
}
