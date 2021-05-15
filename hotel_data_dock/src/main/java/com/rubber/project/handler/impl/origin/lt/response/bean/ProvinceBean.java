package com.rubber.project.handler.impl.origin.lt.response.bean;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class ProvinceBean {

    private String provinceId;

    private String provinceName;

    private String spellNameShort;

    private List<CityBean> cityList;
}
