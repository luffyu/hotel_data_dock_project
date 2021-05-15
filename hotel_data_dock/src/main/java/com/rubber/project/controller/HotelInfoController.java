package com.rubber.project.controller;

import cn.hutool.luffyu.util.result.ResultMsg;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelResponse;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.service.LtHotelService;
import com.rubber.project.service.XcHotelService;
import com.rubber.project.handler.impl.target.xc.response.XcHotelInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
@RestController
@RequestMapping("/hotels")
public class HotelInfoController {

    @Autowired
    private LtHotelService ltHotelService;

    @Autowired
    private XcHotelService xcHotelService;


    @GetMapping("/info")
    public ResultMsg queryHotelInfo(Integer xcHotelId, String ltHotelId,Integer afterDay){
        LtHotelResponse ltHotel = ltHotelService.getHotelInfoByIdUnEx(ltHotelId,afterDay);
        XcHotelInfoResponse xcHotel = xcHotelService.getHotelInfoByIdUnEx(xcHotelId);
        Map<String,Object> data = new HashMap<>(2);
        data.put("ltHotel",ltHotel);
        data.put("xcHotel",xcHotel);
        if (ltHotel.getHotelId() == null || xcHotel.getHotelId() == null ){
            return ResultMsg.create(HotelProjectErrCode.HOTEL_WAIN,data);
        }else {
            return ResultMsg.success(data);
        }
    }


}
