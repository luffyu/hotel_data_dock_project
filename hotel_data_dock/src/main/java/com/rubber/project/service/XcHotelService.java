package com.rubber.project.service;

import com.rubber.project.exception.RequestParamsException;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.model.enums.SyncStatus;
import com.rubber.project.xc.http.XcHttpUtils;
import com.rubber.project.xc.response.XcHotelInfoResponse;
import com.rubber.project.xc.response.XcRoomInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
@Slf4j
@Service
public class XcHotelService {


    public XcHotelInfoResponse getHotelInfoByIdUnEx(Integer hotelId)  {
        try {
            return getHotelInfoById(hotelId);
        }catch (Exception e){
            log.info("获取携程酒店的信息异常 msg={}",e.getMessage());
            XcHotelInfoResponse xcHotelInfoResponse = new XcHotelInfoResponse();
            xcHotelInfoResponse.setInterfaceStatus(SyncStatus.XC_REQUEST_ERROR.getCode());
            xcHotelInfoResponse.setMsg( e.getMessage());
            return xcHotelInfoResponse;
        }
    }


    /**
     * 获取到酒店的详细信息
     * @param hotelId 酒店id
     * @return 返回酒店的详细信息
     * @throws Exception 异常的的信息
     */
    public XcHotelInfoResponse getHotelInfoById(Integer hotelId) throws Exception {
        if (hotelId == null){
            throw new RequestParamsException(HotelProjectErrCode.REQUEST_PARAM_ERROR,"酒店id不能为空");
        }
        XcHotelInfoResponse xcHotelInfoResponse = XcHttpUtils.getHotelInfo(hotelId);
        if (xcHotelInfoResponse == null) {
            xcHotelInfoResponse = new XcHotelInfoResponse();
        }
        xcHotelInfoResponse.setHotelID(hotelId);
        List<XcRoomInfoResponse> hotelRoomStaticInfo = XcHttpUtils.getHotelRoomStaticInfo(hotelId);
        xcHotelInfoResponse.setRoomInfoList(hotelRoomStaticInfo);
        return xcHotelInfoResponse;

    }
}
