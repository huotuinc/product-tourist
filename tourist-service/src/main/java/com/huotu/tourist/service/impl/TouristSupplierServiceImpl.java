package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.repository.LoginRepository;
import com.huotu.tourist.repository.TouristSupplierRepository;
import com.huotu.tourist.service.TouristSupplierService;
import me.jiangcai.lib.resource.service.ResourceService;
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
    @Autowired
    ResourceService resourceService;
    @Autowired
    LoginRepository loginRepository;

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
    public void modifySupplier(Long id, Address address, String contacts, String contactNumber
            , String businessLicenseUri, String remarks, String detailedAddress) {
        TouristSupplier touristSupplier = getOne(id);
        touristSupplier.setAddress(address);
        touristSupplier.setContacts(contacts);
        touristSupplier.setContactNumber(contactNumber);
        try {
            resourceService.deleteResource(touristSupplier.getBusinessLicenseUri());
        } catch (Exception e) {
//            e.printStackTrace();
        }
        touristSupplier.setBusinessLicenseUri(businessLicenseUri);

        touristSupplier.setDetailedAddress(detailedAddress);
        touristSupplier.setRemarks(remarks);

    }

    @Override
    public Page<TouristSupplier> getJurisdictionList(TouristSupplier authSupplier, Pageable pageable) {
        return touristSupplierRepository.findByAuthSupplierAndDeleted(authSupplier,false,pageable);
    }
}
