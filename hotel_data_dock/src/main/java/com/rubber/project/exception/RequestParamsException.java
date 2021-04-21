package com.rubber.project.exception;

import cn.hutool.luffyu.util.result.IResultHandle;
import cn.hutool.luffyu.util.result.code.ICodeHandle;
import com.rubber.project.model.enums.HotelProjectErrCode;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
public class RequestParamsException extends HotelDataException {

    public RequestParamsException(String msg) {
        super(msg);
    }

    public RequestParamsException(HotelProjectErrCode handle) {
        super(handle);
    }

    public RequestParamsException(ICodeHandle handle, Object data) {
        super(handle, data);
    }

    public RequestParamsException(String code, String msg, Object data) {
        super(code, msg, data);
    }

    public RequestParamsException(ICodeHandle handle, String msg, Object... arguments) {
        super(handle, msg, arguments);
    }
}
