package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {
    @Autowired
    ActivityTypeRepository activityTypeRepository;

    @Autowired
    TouristOrderRepository touristOrderRepository;

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

    @Override
    public List<ActivityType> getHotTypes() {
        List<ActivityType> types=new ArrayList<>();
        Object[] typesObj=touristOrderRepository.searchActivityTypeGruop();
        for(Object o:typesObj){
            Object[] objects=(Object[])o;
            if(objects!=null&&objects[0]!=null){
                types.add((ActivityType)objects[0]);
            }
        }
        return types;
    }
}
