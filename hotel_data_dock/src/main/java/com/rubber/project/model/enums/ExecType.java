package com.rubber.project.model.enums;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
public enum ExecType {

    /**
     * 执行类型
     */
    AUTO_EXEC(0,"自动执行"),
    MANUAL_EXEC(1,"手动执行"),
    STOP_EXEC(2,"不执行"),

    ;
    private Integer code;

    private String name;

    ExecType(Integer code, String name) {
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
