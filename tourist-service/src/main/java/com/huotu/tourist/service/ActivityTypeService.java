package com.huotu.tourist.service;

import com.huotu.tourist.entity.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 活动类型服务
 * Created by lhx on 2016/12/17.
 */

public interface ActivityTypeService extends BaseService<ActivityType, Long> {

    Page<ActivityType> activityTypeList(String name, Pageable pageable);

    List<ActivityType> getHotTypes();
}
