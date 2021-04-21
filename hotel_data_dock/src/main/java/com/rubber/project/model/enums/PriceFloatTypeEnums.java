package com.rubber.project.model.enums;

/**
 * @author luffyu
 * Created on 2021/4/21
 */

public enum  PriceFloatTypeEnums {

    /**
     * 数据操作
     */
    ADD("add"),
    REDUCE("reduce"),
    ;

    PriceFloatTypeEnums(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return key;
    }
}
