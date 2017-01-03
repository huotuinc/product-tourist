package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.TouristType;
import com.huotu.tourist.repository.TouristTypeRepository;
import com.huotu.tourist.service.TouristTypeService;
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
public class TouristTypeServiceImpl implements TouristTypeService {
    @Autowired
    TouristTypeRepository touristTypeRepository;

    @Override
    public TouristType save(TouristType data) {
        return touristTypeRepository.saveAndFlush(data);
    }

    @Override
    public TouristType getOne(Long aLong) {
        return touristTypeRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        touristTypeRepository.delete(aLong);
    }

    @Override
    public Page<TouristType> touristTypeList(String name, Pageable pageable) {
        return touristTypeRepository.findAll
                ((root, query, cb) -> {
                    Predicate predicate = null;
                    if (!StringUtils.isEmpty(name)) {
                        predicate = cb.like(root.get("typeName").as(String.class), name);
                    }
                    return predicate;
                }, pageable);
    }
}
