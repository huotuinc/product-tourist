package com.huotu.tourist.repository;

import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Created by slt on 2016/12/19.
 */
public interface TouristSupplierRepository extends JpaRepository<TouristSupplier,Long> {

    TouristSupplier findByAdminAccount(String adminAccount);
}
