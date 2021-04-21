package com.rubber.project.exception;

import cn.hutool.luffyu.util.result.IResultHandle;
import cn.hutool.luffyu.util.result.code.ICodeHandle;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
public class HotelDataRequestException extends HotelDataException {

    public HotelDataRequestException(IResultHandle handle) {
        super(handle);
    }

    public HotelDataRequestException(ICodeHandle handle, Object data) {
        super(handle, data);
    }

    public HotelDataRequestException(String code, String msg, Object data) {
        super(code, msg, data);
    }

}
