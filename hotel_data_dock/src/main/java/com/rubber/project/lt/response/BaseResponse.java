package com.rubber.project.lt.response;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class BaseResponse {

    /**
     * code值
     */
    private String code;

    /**
     * 描述信息
     */
    private String description;


    /**
     * 执行的方法
     */
    private String actionName;
}
