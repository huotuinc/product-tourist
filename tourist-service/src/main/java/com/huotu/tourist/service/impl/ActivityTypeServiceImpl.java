package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.service.ActivityTypeService;
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
public class ActivityTypeServiceImpl implements ActivityTypeService {
    @Autowired
    ActivityTypeRepository activityTypeRepository;

    @Override
    public ActivityType save(ActivityType data) {
        return activityTypeRepository.saveAndFlush(data);
    }

    @Override
    public ActivityType getOne(Long aLong) {
        return activityTypeRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        activityTypeRepository.delete(aLong);
    }

    @Override
    public Page<ActivityType> activityTypeList(String name, Pageable pageable) {
        return activityTypeRepository.findAll((root, query, cb) -> {
            Predicate predicate = null;
            if (!StringUtils.isEmpty(name)) {
                predicate = cb.like(root.get("activityName").as(String.class), name);
            }
            return predicate;
        }, pageable);
    }
}
