package com.rubber.project.model.enums;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
public enum SyncStatus {

    /**
     * 执行类型
     */
    INIT(0,"初始化"),
    SUCCESS(1,"成功"),
    FIELD(2,"失败"),
    ERROR(3,"异常"),
    XC_REQUEST_ERROR(4,"携程接口同步异常"),
    LT_REQUEST_ERROR(5,"龙腾接口拉取异常"),

    LT_ROOM_IS_NULL(6,"龙腾房间不存在"),
    LT_ROOM_PRICE_IS_NULL(7,"龙腾房间价格为空"),


    ;
    private Integer code;

    private String name;

    SyncStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
