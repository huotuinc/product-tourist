package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.service.TouristSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class TouristSupplierServiceImpl implements TouristSupplierService {
    @Autowired
    TouristSupplierRepository touristSupplierRepository;

    @Override
    public TouristSupplier save(TouristSupplier data) {
        return touristSupplierRepository.saveAndFlush(data);
    }

    @Override
    public TouristSupplier getOne(Long aLong) {
        return touristSupplierRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        touristSupplierRepository.delete(aLong);
    }

    @Override
    public Page<TouristSupplier> supplierList(String name, Pageable pageable) {
        return touristSupplierRepository.findAll((root, query, cb) -> {
            Predicate predicate = null;
            if (!StringUtils.isEmpty(name)) {
                predicate = cb.like(root.get("supplierName").as(String.class), name);
            }
            return predicate;
        }, pageable);
    }

    @Override
    public TouristSupplier modifySupplier(Long id, Address address, String contacts, String contactNumber
            , String businessLicenseUri, String remarks) {
        TouristSupplier touristSupplier = getOne(id);
        touristSupplier.setAddress(address);
        touristSupplier.setContacts(contacts);
        touristSupplier.setContactNumber(contactNumber);
        // TODO: 2017/1/3 删除原有图片
        touristSupplier.setBusinessLicenseUri(businessLicenseUri);
        touristSupplier.setRemarks(remarks);
        return save(touristSupplier);
    }
}
