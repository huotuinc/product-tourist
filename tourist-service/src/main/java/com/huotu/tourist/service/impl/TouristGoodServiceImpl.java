package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.Address;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.entity.TouristType;
import com.huotu.tourist.repository.TouristGoodRepository;
import com.huotu.tourist.service.TouristGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class TouristGoodServiceImpl implements TouristGoodService {
    @Autowired
    TouristGoodRepository touristGoodRepository;

    @Override
    public TouristGood save(TouristGood data) {
        return touristGoodRepository.saveAndFlush(data);
    }

    @Override
    public TouristGood getOne(Long aLong) {
        return touristGoodRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        touristGoodRepository.delete(aLong);
    }

    @Override
    public Page<TouristGood> touristGoodList(TouristSupplier supplier, String touristName, String supplierName
            , TouristType touristType, ActivityType activityType, TouristCheckStateEnum touristCheckState, Pageable pageable) {
        return touristGoodRepository.findAll((root, query, cb) -> {
            Predicate predicate = null;
            if (supplier != null) {
                predicate = cb.equal(root.get("touristSupplier").as(TouristSupplier.class), supplier);
            }
            if (!StringUtils.isEmpty(supplierName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristSupplier").get("supplierName").as(String.class),
                        supplierName));
            }
            if (!StringUtils.isEmpty(touristName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristName").as(String.class),
                        touristName));
            }
            if (touristType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("touristType").as(TouristType.class),
                        touristType));
            }
            if (activityType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("activityType").as(ActivityType.class),
                        activityType));
            }
            if (touristCheckState != null) {
                predicate = cb.and(predicate, cb.equal(root.get("touristCheckState").as(TouristCheckStateEnum.class),
                        touristCheckState));
            }
            return predicate;
        }, pageable);
    }

    @Override
    public Page<TouristGood> recommendTouristGoodList(String touristName, String supplierName, TouristType touristType
            , ActivityType activityType, TouristCheckStateEnum touristCheckState, Boolean recommend, Pageable pageable) {
        return touristGoodRepository.findAll((root, query, cb) -> {
            Predicate predicate = null;
            if (!StringUtils.isEmpty(supplierName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristSupplier").get("supplierName").as(String.class),
                        supplierName));
            }
            if (!StringUtils.isEmpty(touristName)) {
                predicate = cb.and(predicate, cb.like(root.get("touristName").as(String.class),
                        touristName));
            }
            if (touristType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("touristType").as(TouristType.class),
                        touristType));
            }
            if (activityType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("activityType").as(ActivityType.class),
                        activityType));
            }
            if (touristCheckState != null) {
                predicate = cb.and(predicate, cb.equal(root.get("touristCheckState").as(TouristCheckStateEnum.class),
                        touristCheckState));
            }
            if (recommend != null) {
                predicate = cb.and(predicate, cb.equal(root.get("recommend").as(Boolean.class),
                        recommend));
            }
            return predicate;
        }, pageable);
    }

    @Override
    public Page<TouristGood> modifySupplierInfo(Long supplierId) {
        // TODO: 2017/1/3
        return null;
    }

    @Override
    public TouristGood saveTouristGood(Long id, String touristName, ActivityType activityType, TouristType touristType
            , String touristFeatures, Address destination, Address placeOfDeparture, Address travelledAddress
            , BigDecimal price, BigDecimal childrenDiscount, BigDecimal rebate, String receptionPerson
            , String receptionTelephone, String eventDetails, String beCareful, String touristImgUri, int maxPeople) {
        TouristGood touristGood = null;
        if (id != null) {
            touristGood = touristGoodRepository.getOne(id);
        } else {
            touristGood = new TouristGood();
        }
        touristGood.setTouristName(touristName);
        touristGood.setActivityType(activityType);
        touristGood.setTouristType(touristType);
        touristGood.setTouristFeatures(touristFeatures);
        touristGood.setDestination(destination);
        touristGood.setPlaceOfDeparture(placeOfDeparture);
        touristGood.setTravelledAddress(travelledAddress);
        touristGood.setPrice(price);
        touristGood.setChildrenDiscount(childrenDiscount);
        touristGood.setRebate(rebate);
        touristGood.setReceptionPerson(receptionPerson);
        touristGood.setReceptionTelephone(receptionTelephone);
        touristGood.setEventDetails(eventDetails);
        touristGood.setBeCareful(beCareful);
        touristGood.setTouristImgUri(touristImgUri);
        touristGood.setMaxPeople(maxPeople);
        return touristGoodRepository.saveAndFlush(touristGood);
    }
}