package com.huotu.tourist.repository;

import com.huotu.tourist.entity.CollectionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 收款账户
 * Created by slt on 2016/12/23.
 */
@Repository
public interface CollectionAccountRepository extends JpaRepository<CollectionAccount,Long> {
}
