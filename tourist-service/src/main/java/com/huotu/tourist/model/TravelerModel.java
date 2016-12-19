package com.huotu.tourist.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 游客model
 * Created by slt on 2016/12/19.
 */
@Getter
@Setter
@EqualsAndHashCode
public class TravelerModel {

    private long id;

    private String name;

    private String sex;

    private int age;

    private String tel;

    private String IDNo;
}
