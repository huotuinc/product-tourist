package com.huotu.tourist.entity;

import java.time.LocalDateTime;

/**
 * Created by lhx on 2016/12/16.
 */

public abstract class BaseModel {
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updataTIme;
    private Boolean delete;

}
