package com.rubber.project.xc.enmus;

/**
 * xc的房态表
 * @author luffyu
 * Created on 2021/4/9
 */
public enum XcRoomStatusEnums {
    /**
     * 房态信息
     */
    OPEN(0,"满房","full"),
    FREEZE(1,"销售","open"),
    UP(2,"限量","full"),

    ;
    private Integer code;

    private String name;

    private String key;

    XcRoomStatusEnums(Integer code, String name, String key) {
        this.code = code;
        this.name = name;
        this.key = key;
    }


    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public static XcRoomStatusEnums getByKey(String key){
        for(XcRoomStatusEnums xcRoomStatusEnums:XcRoomStatusEnums.values()){
            if (xcRoomStatusEnums.getKey().equals(key)){
                return xcRoomStatusEnums;
            }
        }
        return null;
    }

    public static XcRoomStatusEnums getByCode(Integer code){
        for(XcRoomStatusEnums xcRoomStatusEnums:XcRoomStatusEnums.values()){
            if (xcRoomStatusEnums.getCode().equals(code)){
                return xcRoomStatusEnums;
            }
        }
        return null;
    }
}
