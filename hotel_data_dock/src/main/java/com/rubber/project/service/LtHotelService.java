package com.rubber.project.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.project.exception.HotelDataRequestException;
import com.rubber.project.exception.RequestParamsException;
import com.rubber.project.lt.http.AsmxHttpUtil;
import com.rubber.project.lt.request.LtHotelRoomRequest;
import com.rubber.project.lt.response.LtHotelResponse;
import com.rubber.project.lt.response.LtHotelResult;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.model.enums.SyncStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * lt的房间价格信息表
 * @author luffyu
 * Created on 2021/4/9
 */
@Slf4j
@Service
public class LtHotelService {


    /**
     * 通过酒店id 查询龙腾的接口数据信息
     * @param hotelId 酒店id
     * @return 返回数据信息
     */
    public LtHotelResponse getHotelInfoByIdUnEx(String hotelId,int afterDay)  {
        try {
            return getHotelInfoById(hotelId,afterDay);
        }catch (Exception e){
            log.info("获取携程酒店的信息异常 msg={}",e.getMessage());
            LtHotelResponse xcHotelInfoResponse = new LtHotelResponse();
            xcHotelInfoResponse.setInterfaceStatus(SyncStatus.LT_REQUEST_ERROR.getCode());
            xcHotelInfoResponse.setMsg( e.getMessage());
            return xcHotelInfoResponse;
        }
    }



    public LtHotelResponse getHotelInfoById(String hotelId) throws Exception {
        return getHotelInfoById(hotelId,1);
    }

    /**
     * 通过酒店id 查询龙腾的接口数据信息
     * @param hotelId 酒店id
     * @param afterDay 之后多少天的id
     * @return 返回数据信息
     */
    public LtHotelResponse getHotelInfoById(String hotelId,int afterDay) throws Exception {
        if (StrUtil.isEmpty(hotelId)){
            throw new RequestParamsException(HotelProjectErrCode.REQUEST_PARAM_ERROR,"酒店id不能为空");
        }
        LtHotelRoomRequest request = new LtHotelRoomRequest();
        request.setHotelId(hotelId);
        request.setCheckIn(DateTime.now());
        request.setCheckOut(DateUtil.offsetDay(new Date(), afterDay));
        LtHotelResult ltHotelResult = AsmxHttpUtil.getHotelInfoPrice(request);
        if ( !"30000".equals(ltHotelResult.getCode()) || CollUtil.isEmpty(ltHotelResult.getResponseList())){
            log.error("调用龙腾的接口异常，code= {} msg={}",ltHotelResult.getCode(),ltHotelResult.getDescription());
            String msg = ltHotelResult.getCode() + ":" + ltHotelResult.getDescription();
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,msg);
        }
        return ltHotelResult.getResponseList().get(0);
    }

}
