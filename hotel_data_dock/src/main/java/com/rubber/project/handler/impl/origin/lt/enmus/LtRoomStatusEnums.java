package com.rubber.project.handler.impl.origin.lt.enmus;

/**
 * xc的房态表
 * @author luffyu
 * Created on 2021/4/9
 */
public enum LtRoomStatusEnums {
    /**
     * 房态信息
     */
    OPEN("026001","开启","open"),
    FREEZE("026002","冻结","full"),
    UP("015004","上线","open"),
    DOWN("015005","下线","close"),

    ;
    private String code;

    private String name;

    private String key;

    LtRoomStatusEnums(String code, String name, String key) {
        this.code = code;
        this.name = name;
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public String getKey() {
        return key;
    }

    public static LtRoomStatusEnums getByCode(String code){
        for (LtRoomStatusEnums ltRoomStatusEnums:LtRoomStatusEnums.values()){
            if (ltRoomStatusEnums.getCode().equals(code)){
                return ltRoomStatusEnums;
            }
        }
        return null;
    }


    public static LtRoomStatusEnums getByKey(String key){
        for (LtRoomStatusEnums ltRoomStatusEnums:LtRoomStatusEnums.values()){
            if (ltRoomStatusEnums.getKey().equals(key)){
                return ltRoomStatusEnums;
            }
        }
        return null;
    }
}
