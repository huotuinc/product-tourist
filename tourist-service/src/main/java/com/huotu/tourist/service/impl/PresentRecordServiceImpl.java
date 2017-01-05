package com.huotu.tourist.service.impl;

import com.huotu.tourist.common.PresentStateEnum;
import com.huotu.tourist.entity.PresentRecord;
import com.huotu.tourist.repository.PresentRecordRepository;
import com.huotu.tourist.service.PresentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

/**
 * Created by lhx on 2017/1/4.
 */
@Service
public class PresentRecordServiceImpl implements PresentRecordService {
    @Autowired
    PresentRecordRepository presentRecordRepository;

    @Override
    public PresentRecord save(PresentRecord data) {
        return presentRecordRepository.saveAndFlush(data);
    }

    @Override
    public PresentRecord getOne(Long aLong) {
        return presentRecordRepository.getOne(aLong);
    }

    @Override
    public void delete(Long aLong) {
        presentRecordRepository.delete(aLong);
    }

    @Override
    public Page<PresentRecord> presentRecordList(String supplierName, PresentStateEnum presentState, LocalDateTime createTime, Pageable pageable) {
        return presentRecordRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            if (!StringUtils.isEmpty(supplierName)) {
                predicate = cb.and(null, cb.like(root.get("settlementSheet").get("touristOrder").get("touristGood")
                                .get("touristSupplier").get("supplierName").as(String.class),
                        supplierName));
            }
            if (presentState != null) {
                predicate = cb.and(predicate, cb.equal(root.get("presentState").as(PresentStateEnum.class),
                        presentState));
            }
            if (createTime != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),
                        createTime));
            }
            return predicate;
        }, pageable);
    }
}
