package com.rubber.project.exception;

import cn.hutool.luffyu.util.result.IResultHandle;
import cn.hutool.luffyu.util.result.code.ICodeHandle;
import com.rubber.admin.core.exceptions.AdminException;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
public class HotelDataException  extends AdminException {

    public HotelDataException(String msg) {
        super(msg);
    }

    public HotelDataException(IResultHandle handle) {
        super(handle);
    }

    public HotelDataException(String code, String msg, Object data) {
        super(code, msg, data);
    }

    public HotelDataException(ICodeHandle handle, Object data) {
        super(handle, data);
    }

    public HotelDataException(ICodeHandle handle) {
        super(handle);
    }

    public HotelDataException(ICodeHandle handle, String msg, Object... arguments) {
        super(handle, msg, arguments);
    }
}
