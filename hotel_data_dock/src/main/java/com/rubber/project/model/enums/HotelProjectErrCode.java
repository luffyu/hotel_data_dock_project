package com.rubber.project.model.enums;

import cn.hutool.luffyu.util.result.code.ICodeHandle;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
/**
 * @author luffyu
 * Created on 2019-05-13
 *
 *  错误代码模块 code
 *
 *  编号规则： 1-00-00
 *
 *  第一部分： 一位，系统模块
 *  第二部分： 二位，业务模块
 *  第三部分： 二位，友好提示编号
 *  统一的成功Code 为 10100
 *
 *  全局系统模块 1-**-**
 *
 *  后台管理模块 2-**-**
 *
 *  其他的模块 3-**-**
 *
 */
public enum HotelProjectErrCode implements ICodeHandle {

    /**
     * 301** 警告
     *
     * 302** 异常
     */
    HOTEL_WAIN("30100","部分酒店数据不存在"),

    ERROR("30200","未知错误"),
    REQUEST_ERROR("30201","接口请求错误"),
    REQUEST_PARAM_ERROR("30203","接口参数错误"),
    HOTEL_NOT_EXIST("30204","酒店不存在"),
    HOTEL_CONFIG_ID_CANT_CHANGE("30205","酒店的配置Id不能更改"),
    HOTEL_IS_STOP_EXEC("30206","当前酒店已经停止同步"),

    ;
    private String code;
    private String msg;

    private HotelProjectErrCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
